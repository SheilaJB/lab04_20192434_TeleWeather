package com.example.teleweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class LocationFragment extends Fragment implements LocationAdapter.OnLocationClickListener {

    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewLocation;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    private LocationAdapter adapter;
    private List<LocationResult> locationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        // Inicializar vistas
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewLocation = view.findViewById(R.id.recyclerViewLocation);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSearchButton();

        // Mostrar mensaje de estado vacío inicialmente
        tvEmptyState.setVisibility(View.VISIBLE);
        recyclerViewLocation.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        adapter = new LocationAdapter(locationList, this); // Ahora "this" es válido porque implementamos la interfaz
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewLocation.setAdapter(adapter);
    }

    private void setupSearchButton() {
        buttonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchLocation(query);
            } else {
                Toast.makeText(requireContext(), "Ingrese una ubicación para buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocation(String query) {
        // Ocultar teclado
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);

        // Mostrar estado de carga
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        recyclerViewLocation.setVisibility(View.GONE);

        // Verificar conexión a internet
        ConnectivityManager connMgr = (ConnectivityManager) requireContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Realizar la búsqueda
            WeatherApiClient.getService().searchLocation(
                    WeatherApiClient.getApiKey(),
                    query
            ).enqueue(new Callback<List<LocationResult>>() {
                @Override
                public void onResponse(Call<List<LocationResult>> call, Response<List<LocationResult>> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        locationList = response.body();
                        adapter.updateData(locationList);

                        // Mostrar mensaje si no hay resultados
                        if (locationList.isEmpty()) {
                            tvEmptyState.setText("No se encontraron ubicaciones para: " + query);
                            tvEmptyState.setVisibility(View.VISIBLE);
                            recyclerViewLocation.setVisibility(View.GONE);
                        } else {
                            tvEmptyState.setVisibility(View.GONE);
                            recyclerViewLocation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show();
                        tvEmptyState.setText("Error al buscar ubicaciones.");
                        tvEmptyState.setVisibility(View.VISIBLE);
                        recyclerViewLocation.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<LocationResult>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    tvEmptyState.setText("Error de conexión. Intente nuevamente.");
                    tvEmptyState.setVisibility(View.VISIBLE);
                    recyclerViewLocation.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            tvEmptyState.setText("No hay conexión a Internet. Verifique su conexión e intente nuevamente.");
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerViewLocation.setVisibility(View.GONE);
        }
    }

    // Implementación del método de la interfaz OnLocationClickListener
    @Override
    public void onLocationClick(LocationResult location) {
        // Aquí manejas el clic en una ubicación
        Toast.makeText(requireContext(), "Ubicación seleccionada: " + location.getName(), Toast.LENGTH_SHORT).show();

        // Si deseas, puedes navegar a otro fragmento con información más detallada
        // usando el ID de la ubicación o cualquier otra información relevante

        // Ejemplo de navegación usando Navigation Component:
        // Bundle args = new Bundle();
        // args.putString("locationId", location.getId());
        // Navigation.findNavController(requireView()).navigate(R.id.action_locationFragment_to_detailFragment, args);
    }
}