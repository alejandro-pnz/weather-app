package com.akozhevnikov.weatherapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.akozhevnikov.weatherapp.model.WeatherHelper;
import com.akozhevnikov.weatherapp.model.WeatherProvider;

import java.util.Calendar;

public class WeatherProviderTest extends ProviderTestCase2<WeatherProvider> {

    public WeatherProviderTest() {
        super(WeatherProvider.class, WeatherProvider.AUTHORITY);
    }

    public void testGetWeatherByCity() {
        final int time = Calendar.getInstance().get(Calendar.SECOND);
        final int testId = 124;
        final float temperature = 30.0f;
        final float anotherTemperature = 20.0f;

        final String cityName = "Moscow";
        final String icon = "01n";
        final String anotherIcon = "02n";

        ContentValues city = new ContentValues();
        city.put(WeatherHelper.COLUMN_ID, testId);
        city.put(WeatherHelper.COLUMN_NAME, cityName);
        Uri resultCityUri = getMockContentResolver().insert(WeatherProvider.CITY_CONTENT_URI, city);
        assertNotNull(resultCityUri);

        ContentValues weather = new ContentValues();
        weather.put(WeatherHelper.COLUMN_CITY, testId);
        weather.put(WeatherHelper.COLUMN_TIMESTAMP, time);
        weather.put(WeatherHelper.COLUMN_TEMPERATURE, temperature);
        weather.put(WeatherHelper.COLUMN_ICON, icon);

        ContentValues anotherWeather = new ContentValues(weather);
        anotherWeather.put(WeatherHelper.COLUMN_ICON, anotherIcon);

        Uri resultUri = getMockContentResolver().insert(WeatherProvider.WEATHER_CONTENT_URI, weather);
        assertNotNull(resultUri);

        Cursor cursor = getMockContentResolver().query(
                WeatherProvider.WEATHER_CONTENT_URI, null,
                null, null, WeatherHelper.COLUMN_TIMESTAMP + " ASC");
        assertNotNull(cursor);

        String selectionClause = WeatherHelper.COLUMN_CITY + " LIKE ? AND " +
                WeatherHelper.COLUMN_TIMESTAMP + " LIKE ? AND (" +
                WeatherHelper.COLUMN_ICON + " <> ? OR " +
                WeatherHelper.COLUMN_TEMPERATURE + " <> ?);";

        String[] selectionArgs = new String[]{
                String.valueOf(testId),
                String.valueOf(time),
                anotherIcon,
                String.valueOf(anotherTemperature)};

        int updated = getMockContentResolver().update(
                WeatherProvider.WEATHER_CONTENT_URI,
                anotherWeather,
                selectionClause,
                selectionArgs);

        assertEquals(1, updated);

        Cursor cityCursor = getMockContentResolver().query(
                WeatherProvider.CITY_CONTENT_URI, null,
                null, null, null);
        assertNotNull(cityCursor);
    }
}