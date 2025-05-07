package com.example.teleweather;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {
    private static final String BASE_URL = "https://api.weatherapi.com";
    private static final String API_KEY = "ec24b1c6dd8a4d528c1205500250305";

    private static WeatherApiService service;

    public static WeatherApiService getService() {

        Log.d("WeatherApiClient", "Getting service instance");
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(WeatherApiService.class);
            Log.d("WeatherApiClient", "Service instance created");
        } else {
            Log.d("WeatherApiClient", "Service instance already exists");
        }
        return service;
    }

    public static String getApiKey() {
        Log.d("WeatherApiClient", "Getting API key");
        return API_KEY;
    }
}
