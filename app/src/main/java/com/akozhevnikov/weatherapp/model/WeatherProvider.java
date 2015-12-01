package com.akozhevnikov.weatherapp.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class WeatherProvider extends ContentProvider {
    public static final String AUTHORITY = "com.akozhevnikov.weatherapp.WeatherProvider";
    public static final Uri WEATHER_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + WeatherHelper.WEATHER_TABLE);
    public static final Uri CITY_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + WeatherHelper.CITY_TABLE);

    private static final int WEATHER = 1;
    private static final int WEATHER_STAMP_ID = 2;
    private static final int CITY = 3;
    private static final int CITY_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, WeatherHelper.WEATHER_TABLE, WEATHER);
        sUriMatcher.addURI(AUTHORITY, WeatherHelper.WEATHER_TABLE + "/#", WEATHER_STAMP_ID);

        sUriMatcher.addURI(AUTHORITY, WeatherHelper.CITY_TABLE, CITY);
        sUriMatcher.addURI(AUTHORITY, WeatherHelper.CITY_TABLE + "/#", CITY_ID);
    }

    private WeatherHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new WeatherHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case WEATHER_STAMP_ID:
                queryBuilder.setTables(WeatherHelper.WEATHER_TABLE);
                queryBuilder.appendWhere(WeatherHelper.COLUMN_ID + " = "
                        + uri.getLastPathSegment());
                break;
            case WEATHER:
                queryBuilder.setTables(WeatherHelper.WEATHER_TABLE);
                break;
            case CITY_ID:
                queryBuilder.setTables(WeatherHelper.CITY_TABLE);
                queryBuilder.appendWhere(WeatherHelper.COLUMN_ID + " = "
                        + uri.getLastPathSegment());
                break;
            case CITY:
                queryBuilder.setTables(WeatherHelper.CITY_TABLE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case WEATHER:
                id = db.insert(WeatherHelper.WEATHER_TABLE,
                        null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(WeatherHelper.WEATHER_TABLE + "/" + id);
            case CITY:
                id = db.insert(WeatherHelper.CITY_TABLE,
                        null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(WeatherHelper.CITY_TABLE + "/" + id);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case WEATHER:
                rowsDeleted = db.delete(WeatherHelper.WEATHER_TABLE,
                        selection,
                        selectionArgs);
                break;
            case WEATHER_STAMP_ID:
                String weatherId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(WeatherHelper.WEATHER_TABLE,
                            WeatherHelper.COLUMN_ID + "=" + weatherId,
                            null);
                } else {
                    rowsDeleted = db.delete(WeatherHelper.WEATHER_TABLE,
                            WeatherHelper.COLUMN_ID + "=" + weatherId + " and " + selection,
                            selectionArgs);
                }
                break;
            case CITY:
                rowsDeleted = db.delete(WeatherHelper.CITY_TABLE,
                        selection,
                        selectionArgs);
                break;
            case CITY_ID:
                String cityId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(WeatherHelper.CITY_TABLE,
                            WeatherHelper.COLUMN_ID + "=" + cityId,
                            null);
                } else {
                    rowsDeleted = db.delete(WeatherHelper.WEATHER_TABLE,
                            WeatherHelper.COLUMN_ID + "=" + cityId + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        int rowsUpdated = 0;

        switch (uriType) {
            case WEATHER:
                rowsUpdated = sqlDB.update(WeatherHelper.WEATHER_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case WEATHER_STAMP_ID:
                String weatherId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(WeatherHelper.WEATHER_TABLE,
                                    values,
                                    WeatherHelper.COLUMN_ID + "=" + weatherId,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(WeatherHelper.WEATHER_TABLE,
                                    values,
                                    WeatherHelper.COLUMN_ID + "=" + weatherId
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            case CITY:
                rowsUpdated = sqlDB.update(WeatherHelper.CITY_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CITY_ID:
                String cityId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(WeatherHelper.CITY_TABLE,
                                    values,
                                    WeatherHelper.COLUMN_ID + "=" + cityId,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(WeatherHelper.CITY_TABLE,
                                    values,
                                    WeatherHelper.COLUMN_ID + "=" + cityId
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}