package com.akozhevnikov.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateFormatter {
    private final static String CELSIUS_DEGREE = "℃";
    private final static String DATE_FORMAT = "EEE, d MMM";
    private final static String GMT_TIME_ZONE = "GMT";

    public static String getDayOfTheYear(int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT_TIME_ZONE));
        calendar.set(Calendar.DAY_OF_YEAR, day);
        return dateFormat.format(calendar.getTime());
    }

    public static String formatTemperature(float temperature){
        return String.valueOf((int)temperature + CELSIUS_DEGREE);
    }
}
