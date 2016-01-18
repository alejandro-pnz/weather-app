package com.akozhevnikov.weatherapp.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.utils.Settings;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by alejandro on 15.01.16.
 */
public class RegistrationIntentService extends IntentService {
	private static final String TAG = "RegIntentService";

	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		InstanceID instanceID = InstanceID.getInstance(this);
		try {
			String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging
					.INSTANCE_ID_SCOPE, null);
			Settings.setServerRegistrationStatus(this, true);
			sendRegistrationToServer(token);
		} catch (IOException e) {
			e.printStackTrace();
			Settings.setServerRegistrationStatus(this, false);
		}
	}

	private void sendRegistrationToServer(String token) {
		Log.d("LISTENER", "GCM Registration Token: " + token);
	}
}
