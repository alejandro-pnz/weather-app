package com.akozhevnikov.weatherapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherHelper extends SQLiteOpenHelper {
    public static final String WEATHER_TABLE = "weather";
    public static final String CITY_TABLE = "city";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CITY = "city_id";
    public static final String COLUMN_TEMPERATURE = "temp";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_HUMIDITY = "humidity";

    private static final String CREATE_WEATHER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + WEATHER_TABLE + " ( " +
            COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CITY + " INTEGER NOT NULL, " +
            COLUMN_TEMPERATURE + " REAL, " +
            COLUMN_TIMESTAMP + " INTEGER, " +
            COLUMN_HUMIDITY + " INTEGER, " +
            COLUMN_ICON + " TEXT);";

    private static final String CREATE_CITY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CITY_TABLE + " ( " +
            COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COLUMN_NAME + " TEXT);";
    private static final String DB_NAME = "weather.db";
    private static final int DB_VERSION = 1;

    public WeatherHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER_TABLE);
        db.execSQL(CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE);
        onCreate(db);
    }
}
