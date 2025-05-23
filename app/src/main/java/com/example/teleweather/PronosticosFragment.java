package com.example.teleweather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PronosticosFragment extends Fragment implements SensorEventListener {

    private EditText editTextLocationId;
    private EditText editTextDays;
    private Button buttonSearch;
    private RecyclerView recyclerViewPronosticos;
    private ProgressBar progressBar;
    private TextView textViewTitle;
    private PronosticosAdapter adapter;
    private List<ForecastResponse.ForecastDay> forecastDays = new ArrayList<>();
    private String locationName = "";
    private String locationId = "";

    // Variables para detección de sacudidas
    private static final float ACCELERATION_THRESHOLD = 20.0f; // 20 m/s²
    private long lastShakeTime = 0;
    private static final long SHAKE_COOLDOWN = 1000;

    // Sensor Manager
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pronosticos, container, false);

        // Inicializar vistas
        editTextLocationId = view.findViewById(R.id.editTextLocationId);
        editTextDays = view.findViewById(R.id.editTextDays);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewPronosticos = view.findViewById(R.id.recyclerViewPronosticos);
        progressBar = view.findViewById(R.id.progressBar);
        textViewTitle = view.findViewById(R.id.textViewTitle);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView
        adapter = new PronosticosAdapter(forecastDays, locationName, locationId);
        recyclerViewPronosticos.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewPronosticos.setAdapter(adapter);

        // Obtener argumentos si los hay
        if (getArguments() != null) {
            locationId = getArguments().getString("locationId", "");
            locationName = getArguments().getString("locationName", "");

            if (!locationId.isEmpty()) {
                editTextLocationId.setText(locationId);
                editTextDays.setText("7"); // Por defecto 7 días
                // Buscar pronósticos automáticamente
                getForecast(locationId, 7);
            }
        }

        // Configurar botón de búsqueda
        buttonSearch.setOnClickListener(v -> {
            String id = editTextLocationId.getText().toString().trim();
            String daysStr = editTextDays.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese un ID de ubicación", Toast.LENGTH_SHORT).show();
                return;
            }

            int days = 7; // Por defecto 7 días
            if (!daysStr.isEmpty()) {
                try {
                    days = Integer.parseInt(daysStr);
                    if (days < 1 || days > 14) {
                        Toast.makeText(requireContext(), "Los días deben estar entre 1 y 14", Toast.LENGTH_SHORT).show();
                        days = 7;
                        editTextDays.setText("7");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Ingrese un número válido de días", Toast.LENGTH_SHORT).show();
                    editTextDays.setText("7");
                }
            } else {
                editTextDays.setText("7");
            }

            // Ocultar teclado
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            // Buscar pronósticos
            getForecast(id, days);
        });

        // Inicializar el sensor del acelerómetro
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }
    /*
    * Promt: Detectar sacudidas del dispositivo para limpiar los pronósticos*/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Obtener valores del acelerómetro (x, y, z)
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calcular la aceleración total usando la raíz cuadrada de la suma de los cuadrados
            float acceleration = (float) Math.sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH;

            // Si la aceleración es mayor que el umbral
            if (acceleration > ACCELERATION_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                // Verificar el período de enfriamiento para evitar activaciones múltiples
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN) {
                    lastShakeTime = currentTime;
                    // Mostrar diálogo de confirmación
                    showClearForecastsDialog();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se necesita implementación
    }

    private void showClearForecastsDialog() {
        // Verificar que el fragmento esté adjunto para evitar excepciones
        if (!isAdded()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar acción");
        builder.setMessage("¿Desea eliminar los pronósticos obtenidos?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Limpiar los pronósticos
            if (adapter != null) {
                forecastDays.clear();
                adapter.updateData(forecastDays, locationName, locationId);
                textViewTitle.setText("Pronósticos");
                Toast.makeText(requireContext(), "Pronósticos eliminados", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Registrar el sensor cuando el fragmento está visible
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Desregistrar el sensor cuando el fragmento no está visible para ahorrar batería
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void getForecast(String id, int days) {
        // Mostrar indicador de carga
        progressBar.setVisibility(View.VISIBLE);

        // Verificar conexión a internet
        ConnectivityManager connMgr = (ConnectivityManager) requireContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Realizar la solicitud a la API
            String query = "id:" + id;
            WeatherApiClient.getService().getForecast(
                    WeatherApiClient.getApiKey(),
                    query,
                    days
            ).enqueue(new Callback<ForecastResponse>() {
                @Override
                public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        ForecastResponse forecastResponse = response.body();

                        // Actualizar título con nombre de ubicación
                        if (forecastResponse.getLocation() != null) {
                            locationName = forecastResponse.getLocation().getName();
                            textViewTitle.setText("Pronósticos para " + locationName);
                        }

                        // Actualizar datos del adaptador
                        if (forecastResponse.getForecast() != null &&
                                forecastResponse.getForecast().getForecastday() != null) {
                            forecastDays = forecastResponse.getForecast().getForecastday();
                            adapter.updateData(forecastDays, locationName, id);

                            if (forecastDays.isEmpty()) {
                                Toast.makeText(requireContext(), "No se encontraron pronósticos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "No se encontraron datos de pronóstico", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ForecastResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
        }
    }
}