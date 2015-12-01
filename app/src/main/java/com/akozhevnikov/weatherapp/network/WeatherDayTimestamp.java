package com.akozhevnikov.weatherapp.network;

public class WeatherDayTimestamp {
    private int day;
    private float averageTemperature;

    public float getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(float averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
