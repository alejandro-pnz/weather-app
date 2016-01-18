package com.akozhevnikov.weatherapp.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.ui.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by alejandro on 18.01.16.
 */
public class WeatherGcmListenerService extends GcmListenerService {
	@Override
	public void onMessageReceived(String from, Bundle data) {
		if (!data.isEmpty()) {

			String senderId = getString(R.string.gcm_defaultSenderId);

			if (senderId.equals(from)) {
				sendNotification("Text");
			}

			Log.d("LISTENER", "Received: " + data.toString());
		}
	}

	private void sendNotification(String text) {
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent =
				PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notif);
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_notif)
						.setLargeIcon(largeIcon)
						.setContentTitle(getString(R.string.notif_message))
						.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
						.setContentText(text)
						.setPriority(NotificationCompat.PRIORITY_HIGH);
		builder.setContentIntent(contentIntent);
		notificationManager.notify(1, builder.build());
	}
}
