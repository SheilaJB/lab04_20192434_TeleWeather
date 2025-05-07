package com.example.teleweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class DeporteAdapter extends RecyclerView.Adapter<DeporteAdapter.FootballViewHolder> {

    private List<Object> sports = new ArrayList<>();
    private Context context;

    public DeporteAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<DeporteResponse.FootballMatch> newMatches) {
        this.sports.clear();
        if (newMatches != null) {
            this.sports.addAll(newMatches);
        }
        notifyDataSetChanged();
    }

    public void updateAllSports(DeporteResponse response) {
        this.sports.clear();

        // Añadir partidos de fútbol
        List<DeporteResponse.FootballMatch> football = response.getFootball();
        if (football != null && !football.isEmpty()) {
            this.sports.addAll(football);
        }

        // Añadir partidos de cricket
        List<DeporteResponse.CricketMatch> cricket = response.getCricket();
        if (cricket != null && !cricket.isEmpty()) {
            this.sports.addAll(cricket);
        }

        // Añadir partidos de golf
        List<DeporteResponse.GolfMatch> golf = response.getGolf();
        if (golf != null && !golf.isEmpty()) {
            this.sports.addAll(golf);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deporte, parent, false);
        return new FootballViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FootballViewHolder holder, int position) {
        Object sportObj = sports.get(position);

        String type = "Desconocido";
        if (sportObj instanceof DeporteResponse.FootballMatch) {
            type = "Fútbol";
        } else if (sportObj instanceof DeporteResponse.CricketMatch) {
            type = "Cricket";
        } else if (sportObj instanceof DeporteResponse.GolfMatch) {
            type = "Golf";
        }

        // Todos heredan de FootballMatch, así que podemos hacer un cast seguro
        DeporteResponse.FootballMatch match = (DeporteResponse.FootballMatch) sportObj;
        holder.bind(match, type);
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    static class FootballViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewType;
        private TextView textViewMatch;
        private TextView textViewStadium;
        private TextView textViewCountry;
        private TextView textViewTournament;
        private TextView textViewStart;

        public FootballViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewMatch = itemView.findViewById(R.id.textViewMatch);
            textViewStadium = itemView.findViewById(R.id.textViewStadium);
            textViewCountry = itemView.findViewById(R.id.textViewCountry);
            textViewTournament = itemView.findViewById(R.id.textViewTournament);
            textViewStart = itemView.findViewById(R.id.textViewStart);
        }

        public void bind(DeporteResponse.FootballMatch match, String type) {
            textViewType.setText(type);
            textViewMatch.setText(match.getMatch());
            textViewStadium.setText("Estadio: " + match.getStadium());
            textViewCountry.setText("País: " + match.getCountry());
            textViewTournament.setText("Torneo: " + match.getTournament());

            // Formatear la fecha para mejor legibilidad
            String formattedDate = formatDate(match.getStart());
            textViewStart.setText("Fecha: " + formattedDate);
        }

        private String formatDate(String dateTimeString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(dateTimeString);
                return outputFormat.format(date);
            } catch (Exception e) {
                return dateTimeString; // En caso de error, devuelve el string original
            }
        }
    }
}