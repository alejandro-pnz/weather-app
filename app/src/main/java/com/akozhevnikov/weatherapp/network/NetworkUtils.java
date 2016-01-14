package com.akozhevnikov.weatherapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtils {
	public static final String SERVICE_URL = "http://api.openweathermap.org";
	public static final String APP_ID = "1c64dca2858e08bd1652cf1dc8cc40fb";
	public static final String CITY_KEY = "city";
	public static final String DAY_KEY = "day";
	public static final String METRIC_UNITS = "metric";

	private NetworkUtils(){}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
