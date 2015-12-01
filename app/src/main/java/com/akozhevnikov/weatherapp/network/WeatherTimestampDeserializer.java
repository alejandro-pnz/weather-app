package com.akozhevnikov.weatherapp.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class WeatherTimestampDeserializer implements JsonDeserializer<WeatherHourTimestamp> {

    private static final String TIMESTAMP_TAG = "dt";
    private static final String MAIN_TAG = "main";
    private static final String WEATHER_TAG = "weather";
    private static final String TEMPERATURE_TAG = "temp";
    private static final String ICON_TAG = "icon";
    private static final String HUMIDITY_TAG = "humidity";

    @Override
    public WeatherHourTimestamp deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final int time = jsonObject.get(TIMESTAMP_TAG)
                .getAsInt();

        final float temperature = jsonObject.get(MAIN_TAG)
                .getAsJsonObject()
                .get(TEMPERATURE_TAG)
                .getAsFloat();

        final String icon = jsonObject.get(WEATHER_TAG)
                .getAsJsonArray()
                .get(0).getAsJsonObject()
                .get(ICON_TAG)
                .getAsString();

        final int humidity = jsonObject.get(MAIN_TAG)
                .getAsJsonObject()
                .get(HUMIDITY_TAG)
                .getAsInt();

        final WeatherHourTimestamp timestamp = new WeatherHourTimestamp();
        timestamp.setTime(time);
        timestamp.setTemperature(temperature);
        timestamp.setIcon(icon);
        timestamp.setHumidity(humidity);
        return timestamp;
    }
}
