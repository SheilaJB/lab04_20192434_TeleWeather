package com.example.teleweather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("/v1/search.json")
    Call<List<LocationResult>> searchLocation(
            @Query("key") String apiKey,
            @Query("q") String query
    );

    @GET("/v1/forecast.json")
    Call<ForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String locationId,
            @Query("days") int days
    );
    @GET("/v1/sports.json")
    Call<DeporteResponse> getSports(
            @Query("key") String apiKey,
            @Query("q") String location
    );


}
