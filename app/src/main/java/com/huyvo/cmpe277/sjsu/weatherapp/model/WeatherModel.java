package com.huyvo.cmpe277.sjsu.weatherapp.model;

import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherModel implements BaseModel {
    public static final int NO_RESOURCE = -1;
    public long dt;
    public int maxTemperature;
    public int minTemperature;
    public int humidity;
    public int pressure;
    public float windSpeed;
    public float degree;
    public String main;
    public String icon;

    public String getDate() {
        return DateHelper.getDate(dt, "GMT-4", "MMM d");
    }

    public String getDayOfTheWeek() {
        return DateHelper.getDate(dt, "GMT-4", "EEE");
    }

    @Override
    public String toString() {
        return "dt = " + dt + "\nmaxTemperature = " + maxTemperature + "\nminTemperature = " + minTemperature
                + "\nicon = " + icon + "\nhumidity = " + humidity + "\nPressure = " + pressure + "\nwindSpeed = "
                + windSpeed + "\ndegree = " + degree;
    }
}
