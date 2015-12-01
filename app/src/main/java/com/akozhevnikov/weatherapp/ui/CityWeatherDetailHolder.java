package com.akozhevnikov.weatherapp.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;
import com.akozhevnikov.weatherapp.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CityWeatherDetailHolder extends RecyclerView.ViewHolder {
    private final static String DATE_FORMAT = "HH:mm";

    private final static String W01D = "01d";
    private final static String W01N = "01n";
    private final static String W02D = "02d";
    private final static String W02N = "02n";
    private final static String W03D = "03d";
    private final static String W03N = "03n";
    private final static String W04D = "04d";
    private final static String W04N = "04n";
    private final static String W09D = "09d";
    private final static String W09N = "09n";
    private final static String W10D = "10d";
    private final static String W10N = "10n";
    private final static String W11D = "11d";
    private final static String W11N = "11n";
    private final static String W13D = "13d";
    private final static String W13N = "13n";
    private final static String W50D = "50d";
    private final static String W50N = "50n";

    private TextView hourText;
    private TextView temperatureText;
    private TextView humidityText;
    private ImageView weatherIcon;
    private Context context;

    public CityWeatherDetailHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        hourText = (TextView) itemView.findViewById(R.id.hour_weather_detail_text);
        temperatureText = (TextView) itemView.findViewById(R.id.temp_weather_detail_text);
        weatherIcon = (ImageView) itemView.findViewById(R.id.icon_weather_detail_image);
        humidityText = (TextView) itemView.findViewById(R.id.humid_weather_detail_text);
    }

    public void init(WeatherHourTimestamp weather) {
        Date date = new Date(weather.getTime() * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        hourText.setText(dateFormat.format(date));
        temperatureText.setText(DateFormatter.formatTemperature(weather.getTemperature()));
        humidityText.setText(weather.getHumidity() + "%");
        switch (weather.getIcon()) {
            case W01D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w01d));
                break;
            case W01N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w01n));
                break;
            case W02D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w02d));
                break;
            case W02N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w02n));
                break;
            case W03D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w03d));
                break;
            case W03N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w03n));
                break;
            case W04D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w04d));
                break;
            case W04N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w04n));
                break;
            case W09D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w09d));
                break;
            case W09N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w09n));
                break;
            case W10D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w10d));
                break;
            case W10N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w10n));
                break;
            case W11D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w11d));
                break;
            case W11N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w11n));
                break;
            case W13D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w13d));
                break;
            case W13N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w13n));
                break;
            case W50D:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w50d));
                break;
            case W50N:
                weatherIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w50n));
                break;
        }
    }
}
