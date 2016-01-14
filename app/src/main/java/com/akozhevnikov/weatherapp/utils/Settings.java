package com.akozhevnikov.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.model.WeatherLoader;

public final class Settings {
	public static final String getDefaultCity(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(context.getString(R.string.pref_location), "");
	}

	public static void setDefaultCity(Context context, String cityName) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString(context.getString(R.string.pref_location), cityName).apply();
	}

	@SuppressWarnings("ResourceType")
	public static @WeatherLoader.LocationStatus int getServerStatus(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(context.getString(R.string.pref_server_status), WeatherLoader
				.SERVER_STATUS_UNKNOWN);
	}

	public static void setServerStatus(Context context, @WeatherLoader.LocationStatus int locationStatus) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt(context.getString(R.string.pref_server_status), locationStatus).apply();
	}
}
