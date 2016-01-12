package com.akozhevnikov.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.NetworkUtils;
import com.akozhevnikov.weatherapp.utils.Settings;

public class MainActivity extends AppCompatActivity
		implements FragmentManager.OnBackStackChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();

		fragmentManager.addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();

		if(fragmentManager.getBackStackEntryCount() == 0) {
			FragmentTransaction initTransaction = fragmentManager.beginTransaction();
			String cityName = Settings.getDefaultCity(this);
			Fragment fragment;
			if (cityName.isEmpty()) {
				fragment = new ChooseCityFragment();
			} else {
				fragment = new CityWeatherFragment();
				Bundle bundle = new Bundle();
				bundle.putString(NetworkUtils.CITY_KEY, cityName);
				fragment.setArguments(bundle);
			}
			initTransaction.add(R.id.content_frame, fragment);
			initTransaction.commit();
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
}
