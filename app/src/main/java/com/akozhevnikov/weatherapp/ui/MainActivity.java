package com.akozhevnikov.weatherapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.gcm.RegistrationIntentService;
import com.akozhevnikov.weatherapp.utils.Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static com.akozhevnikov.weatherapp.network.NetworkUtils.CITY_KEY;

public class MainActivity extends AppCompatActivity
		implements FragmentManager.OnBackStackChangedListener {
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();

		if (fragmentManager.getBackStackEntryCount() == 0) {
			FragmentTransaction initTransaction = fragmentManager.beginTransaction();
			String cityName = Settings.getDefaultCity(this);
			Fragment fragment;
			if (cityName.isEmpty()) {
				fragment = new ChooseCityFragment();
			} else {
				fragment = new CityWeatherFragment();
				Bundle bundle = new Bundle();
				bundle.putString(CITY_KEY, cityName);
				fragment.setArguments(bundle);
			}
			initTransaction.add(R.id.content_frame, fragment);
			initTransaction.commit();
		}

		if (checkPlayServices()) {
			boolean sentToken = Settings.getServerRegistrationStatus(this);
			if (!sentToken) {
				Intent intent = new Intent(this, RegistrationIntentService.class);
				startService(intent);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				FragmentManager fm = getSupportFragmentManager();
				if (fm.getBackStackEntryCount() > 0) {
					fm.popBackStack();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackStackChanged() {
		shouldDisplayHomeUp();
	}

	public void shouldDisplayHomeUp() {
		boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
		getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
	}

	@Override
	public boolean onSupportNavigateUp() {
		getSupportFragmentManager().popBackStack();
		return true;
	}

	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
				finish();
			}
			return false;
		}
		return true;
	}
}
