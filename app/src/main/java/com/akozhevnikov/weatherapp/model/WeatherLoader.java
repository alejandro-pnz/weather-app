package com.akozhevnikov.weatherapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.akozhevnikov.weatherapp.network.City;
import com.akozhevnikov.weatherapp.network.CityWeather;
import com.akozhevnikov.weatherapp.network.NetworkUtils;
import com.akozhevnikov.weatherapp.network.OpenWeatherService;
import com.akozhevnikov.weatherapp.network.WeatherHourTimestamp;
import com.akozhevnikov.weatherapp.network.WeatherTimestampDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherLoader extends BaseLoader {
    private final OpenWeatherService service;
    private final String cityName;

    public WeatherLoader(Context context, String city) {
        super(context);

        this.cityName = city;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherHourTimestamp.class, new WeatherTimestampDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtils.SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(OpenWeatherService.class);
    }

    @Override
    protected void onForceLoad() {
        Call<CityWeather> call = service.getWeather(cityName, NetworkUtils.APP_ID, NetworkUtils.METRIC_UNITS);
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Response<CityWeather> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    CityWeather cityWeather = response.body();
                    City city = cityWeather.getCity();

                    Cursor cityCursor = getContext().getContentResolver().query(
                            WeatherProvider.CITY_CONTENT_URI,
                            new String[]{WeatherHelper.COLUMN_ID, WeatherHelper.COLUMN_NAME},
                            WeatherHelper.COLUMN_NAME + " LIKE ? ",
                            new String[]{city.getName()},
                            null);

                    if (cityCursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put(WeatherHelper.COLUMN_ID, city.getId());
                        values.put(WeatherHelper.COLUMN_NAME, city.getName());

                        getContext().getContentResolver().insert(WeatherProvider.CITY_CONTENT_URI, values);
                    }

                    //2nd add weather timestamp for this city
                    for (WeatherHourTimestamp hourWeather : response.body().getWeatherTimestampList()) {
                        Cursor checkWeatherCursor = getContext().getContentResolver().query(
                                WeatherProvider.WEATHER_CONTENT_URI,
                                null,
                                WeatherHelper.COLUMN_CITY + " LIKE ? AND " + WeatherHelper.COLUMN_TIMESTAMP + " LIKE ? ",
                                new String[]{String.valueOf(city.getId()), String.valueOf(hourWeather.getTime())},
                                WeatherHelper.COLUMN_TIMESTAMP + " DESC");

                        ContentValues values = new ContentValues();
                        values.put(WeatherHelper.COLUMN_CITY, city.getId());
                        values.put(WeatherHelper.COLUMN_TEMPERATURE, hourWeather.getTemperature());
                        values.put(WeatherHelper.COLUMN_ICON, hourWeather.getIcon());
                        values.put(WeatherHelper.COLUMN_TIMESTAMP, hourWeather.getTime());
                        values.put(WeatherHelper.COLUMN_HUMIDITY, hourWeather.getHumidity());

                        if (checkWeatherCursor.getCount() == 0) {
                            getContext().getContentResolver().insert(WeatherProvider.WEATHER_CONTENT_URI, values);
                        } else {
                            String selectionClause = WeatherHelper.COLUMN_CITY + " LIKE ? AND " +
                                    WeatherHelper.COLUMN_TIMESTAMP + " LIKE ? AND (" +
                                    WeatherHelper.COLUMN_ICON + " <> ? OR " +
                                    WeatherHelper.COLUMN_HUMIDITY + " <> ? OR " +
                                    WeatherHelper.COLUMN_TEMPERATURE + " <> ?);";

                            String[] selectionArgs = new String[]{
                                    String.valueOf(city.getId()),
                                    String.valueOf(hourWeather.getTime()),
                                    hourWeather.getIcon(),
                                    String.valueOf(hourWeather.getHumidity()),
                                    String.valueOf(hourWeather.getTemperature())};

                            getContext().getContentResolver().update(
                                    WeatherProvider.WEATHER_CONTENT_URI,
                                    values,
                                    selectionClause,
                                    selectionArgs);
                        }
                    }
                    deliverResult(getCursorResult(city));

                } else {
                    deliverResult(null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Cursor cityCursor = getContext().getContentResolver().query(
                        WeatherProvider.CITY_CONTENT_URI,
                        null,
                        "lower(" + WeatherHelper.COLUMN_NAME + ") LIKE ? ",
                        new String[]{cityName},
                        null);

                if (cityCursor != null && cityCursor.moveToFirst()) {
                    if (!cityCursor.moveToFirst()) {
                        return;
                    }

                    String cityName = cityCursor.getString(cityCursor.getColumnIndex(WeatherHelper.COLUMN_NAME));
                    int id = cityCursor.getInt(cityCursor.getColumnIndex(WeatherHelper.COLUMN_ID));

                    City city = new City();
                    city.setId(id);
                    city.setName(cityName);
                    deliverResult(getCursorResult(city));
                } else {
                    deliverResult(null);
                }
            }

            private Cursor getCursorResult(City city) {
                int currentUnixTime = (int) (Calendar
                        .getInstance(TimeZone.getTimeZone("GMT"))
                        .getTimeInMillis() / 1000L);

                getContext().getContentResolver().delete(
                        WeatherProvider.WEATHER_CONTENT_URI,
                        WeatherHelper.COLUMN_CITY + " LIKE ? AND " + WeatherHelper.COLUMN_TIMESTAMP + " < ?",
                        new String[]{String.valueOf(city.getId()), String.valueOf(currentUnixTime)});

                return getContext().getContentResolver().query(
                        WeatherProvider.WEATHER_CONTENT_URI,
                        null,
                        WeatherHelper.COLUMN_CITY + " LIKE ? ",
                        new String[]{String.valueOf(city.getId())},
                        null);
            }
        });

    }
}
