package com.example.teleweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {


    private List<LocationResult> locationList;
    private OnLocationClickListener listener;

    // Interfaz para manejar los clicks en los items
    public interface OnLocationClickListener {
        void onLocationClick(LocationResult location);
    }

    public LocationAdapter(List<LocationResult> locationList, OnLocationClickListener listener) {
        this.locationList = locationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationResult location = locationList.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void updateData(List<LocationResult> newLocations) {
        this.locationList = newLocations;
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvRegion;
        private TextView tvCountry;
        private TextView tvLocationId;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRegion = itemView.findViewById(R.id.tvRegion);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvLocationId = itemView.findViewById(R.id.tvLocationId);

            // Configurar el click listener en el itemView
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onLocationClick(locationList.get(position));
                }
            });
        }

        public void bind(LocationResult location) {
            tvName.setText(location.getName());
            tvRegion.setText("Región: " + location.getRegion());
            tvCountry.setText("País: " + location.getCountry());
            tvLocationId.setText("ID: " + location.getId());
        }
    }

}
