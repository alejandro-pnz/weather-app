package com.akozhevnikov.weatherapp.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.model.WeatherLoader;
import com.akozhevnikov.weatherapp.model.WeatherTable;
import com.akozhevnikov.weatherapp.network.NetworkUtils;
import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;
import com.akozhevnikov.weatherapp.utils.DateFormatter;
import com.akozhevnikov.weatherapp.utils.Settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityWeatherDetailFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	private final int DETAIL_LOADER_WEATHER_ID = 2;
	private static final Calendar CALENDAR = Calendar.getInstance();

	private CityWeatherDetailAdapter adapter;
	private String city;
	private int dayOfTheYear;

	@Bind(R.id.weather_detail_recycler)
	RecyclerView weatherDetailRecycler;

	@Bind(R.id.detail_progress_bar)
	ProgressBar progressBar;

	@Bind(R.id.detail_city_text)
	TextView cityTextView;

	@Bind(R.id.detail_city_date)
	TextView dateTextView;

	@Bind(R.id.no_detail_info_available)
	TextView noInfoAvailableTextView;

	private List<WeatherHourTimestamp> weatherByHours;
	private LoaderManager loaderManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(false);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			dayOfTheYear = bundle.getInt(NetworkUtils.DAY_KEY);
		}

		city = Settings.getDefaultCity(getContext());

		weatherByHours = new ArrayList<>();
		loaderManager = getActivity().getSupportLoaderManager();

		loaderManager.initLoader(DETAIL_LOADER_WEATHER_ID, Bundle.EMPTY, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_city_weather_detail, container, false);
		ButterKnife.bind(this, view);

		cityTextView.setText(city);
		weatherDetailRecycler.setNestedScrollingEnabled(false);

		dateTextView.setText(DateFormatter.getDayOfTheYear(dayOfTheYear));

		adapter = new CityWeatherDetailAdapter(getContext(), weatherByHours);

		weatherDetailRecycler.setAdapter(adapter);
		weatherDetailRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();

		loaderManager.destroyLoader(DETAIL_LOADER_WEATHER_ID);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case DETAIL_LOADER_WEATHER_ID:
				return new WeatherLoader(getContext(), city);
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		int id = loader.getId();
		if (id == DETAIL_LOADER_WEATHER_ID) {
			progressBar.setVisibility(View.GONE);

			if (data != null && data.moveToFirst()) {
				weatherDetailRecycler.setVisibility(View.VISIBLE);
				dateTextView.setVisibility(View.VISIBLE);
				noInfoAvailableTextView.setVisibility(View.GONE);

				if (!data.moveToFirst()) {
					return;
				}
				try {
					do {
						WeatherHourTimestamp timestamp = WeatherTable.fromCursor(data);
						CALENDAR.setTimeInMillis(timestamp.getTime() * 1000L);
						int timestampDay = CALENDAR.get(Calendar.DAY_OF_YEAR);
						if (timestampDay == dayOfTheYear) {
							weatherByHours.add(timestamp);
						}
					} while (data.moveToNext());
					data.close();
				} finally {
					data.close();
				}
				adapter.notifyDataSetChanged();
			} else if (!NetworkUtils.isNetworkAvailable(getContext())) {
				noInfoAvailableTextView.setText(getResources().getString(R.string.no_info_available_no_network));
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
}
