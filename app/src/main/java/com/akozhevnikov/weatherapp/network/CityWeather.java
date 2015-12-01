package com.akozhevnikov.weatherapp.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityWeather {
    private City city;

    @SerializedName("list")
    private List<WeatherHourTimestamp> weatherTimestampList;

    public City getCity(){
        return city;
    }

    public List<WeatherHourTimestamp> getWeatherTimestampList(){
        return weatherTimestampList;
    }
}
