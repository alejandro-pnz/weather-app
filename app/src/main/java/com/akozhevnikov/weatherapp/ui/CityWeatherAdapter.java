package com.akozhevnikov.weatherapp.ui;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.WeatherDayTimestamp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherHolder> {
    private List<WeatherDayTimestamp> weatherByDay;
    private Context context;

    public CityWeatherAdapter(Context context, List<WeatherDayTimestamp> weatherByDay) {
        this.context = context;
        this.weatherByDay = weatherByDay;
    }

    @Override
    public CityWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_city_weather, parent, false);
        return new CityWeatherHolder(context, view);
    }

    @Override
    public void onBindViewHolder(CityWeatherHolder holder, int position) {
        holder.init(weatherByDay.get(position));
    }

    @Override
    public int getItemCount() {
            return weatherByDay.size();
    }
}
