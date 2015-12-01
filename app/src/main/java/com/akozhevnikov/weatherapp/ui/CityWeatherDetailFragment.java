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
import android.view.MenuItem;
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

public class CityWeatherDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int DETAIL_LOADER_WEATHER_ID = 2;

    private CityWeatherDetailAdapter adapter;
    private RecyclerView weatherDetailRecycler;
    private String city;
    private int dayOfTheYear;
    private ProgressBar progressBar;
    private TextView cityTextView;
    private TextView temperatureTextView;
    private List<WeatherHourTimestamp> weatherByHours;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather_detail, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.detail_progress_bar);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dayOfTheYear = bundle.getInt(NetworkUtils.DAY_KEY);
        }

        city = Settings.getDefaultCity(getActivity());
        cityTextView = (TextView) view.findViewById(R.id.detail_city_text);
        cityTextView.setText(city);

        temperatureTextView = (TextView) view.findViewById(R.id.detail_city_temperature);
        temperatureTextView.setText(DateFormatter.getDayOfTheYear(dayOfTheYear));

        weatherByHours = new ArrayList<>();
        adapter = new CityWeatherDetailAdapter(getContext(), weatherByHours);

        weatherDetailRecycler = (RecyclerView) view.findViewById(R.id.weather_detail_recycler);
        weatherDetailRecycler.setAdapter(adapter);
        weatherDetailRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getActivity()
                .getSupportLoaderManager()
                .initLoader(DETAIL_LOADER_WEATHER_ID, Bundle.EMPTY, this);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity()
                .getSupportLoaderManager()
                .destroyLoader(DETAIL_LOADER_WEATHER_ID);
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
            weatherDetailRecycler.setVisibility(View.VISIBLE);
            temperatureTextView.setVisibility(View.VISIBLE);
            cityTextView.setVisibility(View.VISIBLE);

            if (data != null && data.moveToFirst()) {
                if (!data.moveToFirst()) {
                    return;
                }
                try {
                    do {
                        WeatherHourTimestamp timestamp = WeatherTable.fromCursor(data);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp.getTime() * 1000L);
                        int timestampDay = calendar.get(Calendar.DAY_OF_YEAR);
                        if (timestampDay == dayOfTheYear)
                            weatherByHours.add(timestamp);
                    } while (data.moveToNext());
                    data.close();
                } finally {
                    data.close();
                }
                adapter.notifyDataSetChanged();
            }
        }
        getLoaderManager().destroyLoader(id);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
