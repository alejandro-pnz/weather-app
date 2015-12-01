package com.akozhevnikov.weatherapp.model;

import android.database.Cursor;

import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;

public class WeatherTable {
    public static WeatherHourTimestamp fromCursor(Cursor cursor) {
        int time = cursor.getInt(cursor.getColumnIndex(WeatherHelper.COLUMN_TIMESTAMP));
        float temperature = cursor.getFloat(cursor.getColumnIndex(WeatherHelper.COLUMN_TEMPERATURE));
        String icon = cursor.getString(cursor.getColumnIndex(WeatherHelper.COLUMN_ICON));
        int humidity = cursor.getInt(cursor.getColumnIndex(WeatherHelper.COLUMN_HUMIDITY));

        WeatherHourTimestamp timestamp = new WeatherHourTimestamp();
        timestamp.setTime(time);
        timestamp.setTemperature(temperature);
        timestamp.setIcon(icon);
        timestamp.setHumidity(humidity);
        return timestamp;
    }
}
