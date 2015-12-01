package com.akozhevnikov.weatherapp.network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OpenWeatherService {
    @GET("/data/2.5/forecast")
    Call<CityWeather> getWeather(@Query("q") String cityName,
                                 @Query("APPID") String key,
                                 @Query("units") String units);
}
