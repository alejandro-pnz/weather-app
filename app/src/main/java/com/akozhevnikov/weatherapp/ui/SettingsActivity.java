package com.akozhevnikov.weatherapp.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.model.WeatherLoader;
import com.akozhevnikov.weatherapp.utils.Settings;

public class SettingsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, new PreferenceFragment())
				.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PreferenceFragment extends PreferenceFragmentCompat
			implements Preference.OnPreferenceChangeListener,
			SharedPreferences.OnSharedPreferenceChangeListener {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);

			bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location)));
		}

		private void bindPreferenceSummaryToValue(Preference preference) {
			// Set the listener to watch for value changes.
			preference.setOnPreferenceChangeListener(this);

			// Set the preference summaries
			setPreferenceSummary(preference,
					PreferenceManager
							.getDefaultSharedPreferences(preference.getContext())
							.getString(preference.getKey(), ""));
		}

		@Override
		public void onCreatePreferences(Bundle bundle, String s) {
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			setPreferenceSummary(preference, value);
			return true;
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(getString(R.string.pref_location))) {
				Settings.setServerStatus(getContext(), WeatherLoader.SERVER_STATUS_UNKNOWN);
				setPreferenceSummary(findPreference(key), Settings.getDefaultCity(getContext()));
			}
		}

		@Override
		public void onResume() {
			PreferenceManager.getDefaultSharedPreferences(getContext())
					.registerOnSharedPreferenceChangeListener(this);
			super.onResume();
		}

		@Override
		public void onPause() {
			PreferenceManager.getDefaultSharedPreferences(getContext())
					.unregisterOnSharedPreferenceChangeListener(this);
			super.onPause();
		}

		private void setPreferenceSummary(Preference preference, Object value) {
			String stringValue = value.toString();
			String key = preference.getKey();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int prefIndex = listPreference.findIndexOfValue(stringValue);
				if (prefIndex >= 0) {
					preference.setSummary(listPreference.getEntries()[prefIndex]);
				}
			} else if (key.equals(getString(R.string.pref_location))) {
				@WeatherLoader.LocationStatus int status = Settings.getServerStatus(getContext());
				switch (status) {
					case WeatherLoader.SERVER_STATUS_UNKNOWN:
						preference.setSummary(getString(R.string.pref_location_unknown_description, value.toString()));
						break;
					case WeatherLoader.SERVER_STATUS_INVALID:
						preference.setSummary(getString(R.string.pref_location_error_description, value.toString()));
						break;
					default:
						preference.setSummary(stringValue);
				}
			}
		}
	}
}