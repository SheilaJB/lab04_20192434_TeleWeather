package com.example.teleweather;

import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PronosticosAdapter extends RecyclerView.Adapter<PronosticosAdapter.PronosticoViewHolder>  {

    private List<ForecastResponse.ForecastDay> forecastDays;
    private String locationName;
    private String locationId;

    public PronosticosAdapter(List<ForecastResponse.ForecastDay> forecastDays, String locationName, String locationId) {
        this.forecastDays = forecastDays;
        this.locationName = locationName;
        this.locationId = locationId;
    }

    @NonNull
    @Override
    public PronosticoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pronostico, parent, false);
        return new PronosticoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PronosticoViewHolder holder, int position) {
        ForecastResponse.ForecastDay forecastDay = forecastDays.get(position);
        holder.bind(forecastDay, locationName, locationId);
    }

    @Override
    public int getItemCount() {
        return forecastDays != null ? forecastDays.size() : 0;
    }

    public void updateData(List<ForecastResponse.ForecastDay> forecastDays, String locationName, String locationId) {
        this.forecastDays = forecastDays;
        this.locationName = locationName;
        this.locationId = locationId;
        notifyDataSetChanged();
    }

    static class PronosticoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocation;
        private TextView tvLocationId;
        private TextView tvDate;
        private TextView tvMaxTemp;
        private TextView tvMinTemp;
        private TextView tvCondition;
        private TextView tvHumidity;
        private TextView tvPrecipitation;

        public PronosticoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvLocationId = itemView.findViewById(R.id.tvLocationId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvPrecipitation = itemView.findViewById(R.id.tvPrecipitation);
        }

        public void bind(ForecastResponse.ForecastDay forecastDay, String locationName, String locationId) {
            tvLocation.setText("Ubicación: " + locationName);
            tvLocationId.setText("ID: " + locationId);

            /*
            *  Promt: Formatear la fecha de la siguiente manera*/

            // Formatear la fecha
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(forecastDay.getDate());
                tvDate.setText("Fecha: " + outputFormat.format(date));
                Log.d("PronosticosAdapter", "Fecha original: " + forecastDay.getDate());

            } catch (ParseException e) {
                tvDate.setText("Fecha: " + forecastDay.getDate());
            }
            /*
            *  Promt: Formatear los valores de temperatura, humedad y precipitación*/
            tvMaxTemp.setText(String.format("Máx: %.1f°C", forecastDay.getDay().getMaxtemp_c()));
            tvMinTemp.setText(String.format("Mín: %.1f°C", forecastDay.getDay().getMintemp_c()));
            tvCondition.setText("Condición: " + forecastDay.getDay().getCondition().getText());
            tvHumidity.setText(String.format("Humedad: %.0f%%", forecastDay.getDay().getAvghumidity()));
            tvPrecipitation.setText(String.format("Precipitación: %.1f mm", forecastDay.getDay().getTotalprecip_mm()));
            Log.d("PronosticosAdapter", "Datos del pronóstico: " + forecastDay.toString());
            Log.d("PronosticosAdapter", "Nombre de la ubicación: " + locationName);
            Log.d("PronosticosAdapter", "ID de la ubicación: " + locationId);

        }
    }
}
