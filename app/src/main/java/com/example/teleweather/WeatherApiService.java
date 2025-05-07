package com.example.teleweather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("search.json")
    Call<List<LocationResult>> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String query
    );
}
