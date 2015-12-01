package com.akozhevnikov.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private static final String CITY_KEY = "defaultCity";

    public static final String getDefaultCity(Context context) {
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(CITY_KEY, "");
    }

    public static void setDefaultCity(String cityName, Context context) {
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(CITY_KEY, cityName).apply();
    }
}
