package com.example.teleweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeportesFragment extends Fragment {
    private EditText editTextLocationId;
    private Button buttonSearch;
    private RecyclerView recyclerViewPronosticos;
    private DeporteAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deportes, container, false);

        // Inicializar vistas
        editTextLocationId = view.findViewById(R.id.editTextLocationId);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewPronosticos = view.findViewById(R.id.recyclerViewPronosticos);
        progressBar = view.findViewById(R.id.progressBar);

        // Configurar RecyclerView
        recyclerViewPronosticos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeporteAdapter(getContext());
        recyclerViewPronosticos.setAdapter(adapter);

        // Configurar el botón de búsqueda
        buttonSearch.setOnClickListener(v -> {
            String location = editTextLocationId.getText().toString().trim();
            if (!location.isEmpty()) {
                searchSports(location);
            } else {
                Toast.makeText(getContext(), "Por favor ingrese una ubicación", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void searchSports(String location) {
        showLoading(true);

        WeatherApiService apiService = WeatherApiClient.getService();
        String apiKey = WeatherApiClient.getApiKey();

        Call<DeporteResponse> call = apiService.getSports(apiKey, location);
        call.enqueue(new Callback<DeporteResponse>() {
            @Override
            public void onResponse(Call<DeporteResponse> call, Response<DeporteResponse> response) {
                showLoading(false);
                Log.d("DeportesFragment", "Respuesta recibida: " + response.toString());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DeportesFragment", "Respuesta exitosa: " + response.body().toString());
                    DeporteResponse deporteResponse = response.body();

                    // Actualizar el adaptador con todos los deportes
                    adapter.updateAllSports(deporteResponse);

                    // Contar el total de eventos deportivos
                    int totalEvents = deporteResponse.getFootball().size() +
                            deporteResponse.getCricket().size() +
                            deporteResponse.getGolf().size();

                    if (totalEvents > 0) {
                        Toast.makeText(getContext(),
                                "Se encontraron " + totalEvents + " eventos deportivos",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),
                                "No se encontraron eventos deportivos para esta ubicación",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    adapter.updateAllSports(new DeporteResponse());
                    Toast.makeText(getContext(),
                            "Error al obtener datos: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeporteResponse> call, Throwable t) {
                showLoading(false);
                adapter.updateAllSports(new DeporteResponse());
                Toast.makeText(getContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("DeportesFragment", "Error de conexión", t);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerViewPronosticos.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}