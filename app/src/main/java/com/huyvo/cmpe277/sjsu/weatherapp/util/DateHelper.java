package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.location.Address;
import android.location.Geocoder;

import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Created by Huy Vo on 10/9/17.
 */

public class DateHelper {
    private DateHelper() {}

    public static String getCity(double lat, double lng) throws IOException {
        Geocoder gcd = new Geocoder(WeatherApp.getInstance().getApplicationContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
        if (addresses.size() > 0) {
           return addresses.get(0).getLocality();
        }
        else {
            return null;
        }
    }

    public static long getTimeStamp(){
        return System.currentTimeMillis() / 1000L;
    }

    public static String getLocalTime(long unixTime, String format){

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone(format));

        String localTime = sdf.format(new Date(unixTime * 1000));
        return localTime;

    }

    public static String getLocalDay(long unixTime, String timeZoneId){
       return DateHelper.getDate(unixTime, timeZoneId, "EEE");
    }


    public static String getDate(long unixTime, String gmtTime, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(gmtTime));
        return simpleDateFormat.format(new Date(unixTime * 1000));
    }

    public static boolean isTomorrow(long unixTime, String gmtTime) {
        return numberOfDayFromToday(unixTime, gmtTime) == 1;
    }

    public static int numberOfDayFromToday(long unixTime, String gmtTime) {

        if (System.currentTimeMillis() > unixTime * 1000) return -1;

        String today = getDate(System.currentTimeMillis() / 1000, gmtTime, "EEE");
        int todayAsInteger = convertWeekDayToInteger(today);
        String dayOfTheWeek = getDate(unixTime, gmtTime, "EEE");
        int dayOfTheWeekAsInteger = convertWeekDayToInteger(dayOfTheWeek);
        int difference = dayOfTheWeekAsInteger - todayAsInteger;
        return difference >= 0 ? difference : difference + 7;
    }

    public static int convertWeekDayToInteger(String weekDay)
    {
        int result = -1;
        if ("Sun".equals(weekDay))
        {
            result = 1;
        }
        else if ("Mon".equals(weekDay))
        {
            result = 2;
        }
        else if ("Tue".equals(weekDay))
        {
            result = 3;
        }
        else if ("Wed".equals(weekDay))
        {
            result = 4;
        }
        else if ("Thu".equals(weekDay))
        {
            result = 5;
        }
        else if ("Fri".equals(weekDay))
        {
            result = 6;
        }
        else if ("Sat".equalsIgnoreCase(weekDay))
        {
            result = 7;
        }

        return result;
    }
}
