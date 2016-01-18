package com.akozhevnikov.weatherapp.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by alejandro on 15.01.16.
 */
public class WeatherIDListenerService extends InstanceIDListenerService {

	@Override
	public void onTokenRefresh() {
		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
	}
}
