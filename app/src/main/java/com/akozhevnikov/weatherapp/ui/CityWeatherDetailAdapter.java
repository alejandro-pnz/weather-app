package com.akozhevnikov.weatherapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;

import java.util.List;

public class CityWeatherDetailAdapter extends RecyclerView.Adapter<CityWeatherDetailHolder> {
    private List<WeatherHourTimestamp> weatherByHour;
    private Context context;

    public CityWeatherDetailAdapter(Context context, List<WeatherHourTimestamp> weatherByHour) {
        this.context = context;
        this.weatherByHour = weatherByHour;
    }

    @Override
    public CityWeatherDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_city_weather_detail, parent, false);
        return new CityWeatherDetailHolder(context, view);
    }

    @Override
    public void onBindViewHolder(CityWeatherDetailHolder holder, int position) {
        holder.init(weatherByHour.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherByHour.size();
    }
}
