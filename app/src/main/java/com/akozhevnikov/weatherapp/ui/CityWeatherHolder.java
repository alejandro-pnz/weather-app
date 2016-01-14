package com.akozhevnikov.weatherapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.WeatherDayTimestamp;
import com.akozhevnikov.weatherapp.utils.DateFormatter;

import static com.akozhevnikov.weatherapp.network.NetworkUtils.DAY_KEY;

public class CityWeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	private Context context;
	private TextView dayTextView;
	private TextView tempTextView;
	private int dayOfTheYear;

	public CityWeatherHolder(Context context, View itemView) {
		super(itemView);

		this.context = context;

		dayTextView = (TextView) itemView.findViewById(R.id.day_city_weather_text);
		tempTextView = (TextView) itemView.findViewById(R.id.temp_city_weather_text);

		itemView.setOnClickListener(this);
	}

	public void init(WeatherDayTimestamp weather) {
		dayOfTheYear = weather.getDay();
		dayTextView.setText(DateFormatter.getDayOfTheYear(dayOfTheYear));
		tempTextView.setText(DateFormatter.formatTemperature(weather.getAverageTemperature()));
	}

	@Override
	public void onClick(View v) {
		FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment = new CityWeatherDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(DAY_KEY, dayOfTheYear);
		fragment.setArguments(bundle);
		transaction.add(R.id.content_frame, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
