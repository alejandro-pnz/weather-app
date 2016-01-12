package com.akozhevnikov.weatherapp.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.model.WeatherLoader;
import com.akozhevnikov.weatherapp.model.WeatherTable;
import com.akozhevnikov.weatherapp.network.NetworkUtils;
import com.akozhevnikov.weatherapp.network.WeatherDayTimestamp;
import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;
import com.akozhevnikov.weatherapp.utils.Settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityWeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int LOADER_WEATHER_ID = 1;
	private static final long MILLIS_IN_SECOND = 1000L;

	private CityWeatherAdapter adapter;
	private String city;
	private List<WeatherDayTimestamp> weatherByDays;
	private LoaderManager loaderManager;

	@Bind(R.id.city_weather_progress_bar)
	ProgressBar progressBar;

	@Bind(R.id.no_city_info_available)
	TextView noInfoAvailableTextView;

	@Bind(R.id.city_weather_title)
	TextView cityTextView;

	@Bind(R.id.weather_recycler)
	RecyclerView weatherRecycler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			city = bundle.getString(NetworkUtils.CITY_KEY);
		}

		loaderManager = getActivity().getSupportLoaderManager();

		loaderManager.initLoader(LOADER_WEATHER_ID, Bundle.EMPTY, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
		ButterKnife.bind(this, view);

		weatherByDays = new ArrayList<>();
		adapter = new CityWeatherAdapter(getContext(), weatherByDays);

		weatherRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
		weatherRecycler.setAdapter(adapter);

		cityTextView.setText(city);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.menu_city_weather, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_city_weather_change) {
			FragmentManager manager = CityWeatherFragment.this.getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(R.id.content_frame, new ChooseCityFragment());
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (item.getItemId() == R.id.menu_city_weather_update) {
			//TODO Update
			weatherByDays.clear();
			adapter.notifyDataSetChanged();
			progressBar.setVisibility(View.VISIBLE);
			weatherRecycler.setVisibility(View.GONE);
			noInfoAvailableTextView.setVisibility(View.GONE);
			loaderManager.restartLoader(LOADER_WEATHER_ID, Bundle.EMPTY, this);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		super.onStop();

		loaderManager.destroyLoader(LOADER_WEATHER_ID);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case LOADER_WEATHER_ID:
				return new WeatherLoader(getContext(), city);
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		int id = loader.getId();
		if (id == LOADER_WEATHER_ID) {
			progressBar.setVisibility(View.GONE);
			weatherRecycler.setVisibility(View.VISIBLE);

			List<WeatherHourTimestamp> weatherByHours = new ArrayList<>();

			if (data != null && data.moveToFirst()) {
				if (!data.moveToFirst()) {
					return;
				}
				Settings.setDefaultCity(city, getActivity());
				try {
					do {
						weatherByHours.add(WeatherTable.fromCursor(data));
					} while (data.moveToNext());
				} finally {
					data.close();
				}
				getAverageDayData(weatherByHours);
				adapter.notifyDataSetChanged();
			} else if (!NetworkUtils.isNetworkAvailable(getContext())) {
				noInfoAvailableTextView.setText(getResources().getString(R.string.no_info_available_no_internet));
				noInfoAvailableTextView.setVisibility(View.VISIBLE);
			} else {
				noInfoAvailableTextView.setVisibility(View.VISIBLE);
			}
		}

		loaderManager.destroyLoader(id);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		loaderManager.destroyLoader(loader.getId());
	}

	private void getAverageDayData(List<WeatherHourTimestamp> hourTimestamps) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(hourTimestamps.get(0).getTime() * MILLIS_IN_SECOND);
		int currentDay = cal.get(Calendar.DAY_OF_YEAR);

		for (int i = 0; i < hourTimestamps.size(); ) {
			cal.setTimeInMillis(hourTimestamps.get(i).getTime() * MILLIS_IN_SECOND);

			float averageTemperature = 0.0f;
			int day = cal.get(Calendar.DAY_OF_YEAR);
			WeatherDayTimestamp dayTimestamp = new WeatherDayTimestamp();

			int dayTimestampCount = 0;
			for (; day == currentDay && i < hourTimestamps.size(); i++) {
				cal.setTimeInMillis(hourTimestamps.get(i).getTime() * MILLIS_IN_SECOND);
				day = cal.get(Calendar.DAY_OF_YEAR);
				averageTemperature += hourTimestamps.get(i).getTemperature();
				dayTimestampCount++;
			}

			dayTimestamp.setDay(currentDay);
			averageTemperature /= dayTimestampCount;
			dayTimestamp.setAverageTemperature(averageTemperature);
			weatherByDays.add(dayTimestamp);
			currentDay = day;
		}
	}
}
