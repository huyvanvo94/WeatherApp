package com.huyvo.cmpe277.sjsu.weatherapp.model;

import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherModel implements BaseModel {
    public static final int NO_RESOURCE = -1;
    public long dt;
    public int colorIndex;
    public int humidity;
    public int pressure;
    public float windSpeed;

    public float temp_max;
    public float temp_min;
    public float temp;
    public float temp_day;
    public float temp_night;
    public float temp_eve;
    public float temp_morn;

    public double lat;
    public double lon;

    public String main;
    public String icon;
    public float degree;

    public String country;
    public String city;

    public String description;

    public String timeZoneId;

    public String getKey(){
        String lat = String.format("%.2f", this.lat);
        String lon = String.format("%.2f", this.lon);

        return "lat="+ lat +"&lon="+lon;
    }


    public String getDate() {
        return DateHelper.getDate(dt, "GMT-4", "MMM d");
    }

    public String getDayOfTheWeek() {
        return DateHelper.getDate(dt, "GMT-4", "EEE");
    }

    @Override
    public String toString() {
        return "WeatherModel{" +
                "dt=" + dt +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                ", temp_max=" + temp_max +
                ", temp_min=" + temp_min +
                ", temp=" + temp +
                ", temp_day=" + temp_day +
                ", temp_night=" + temp_night +
                ", temp_eve=" + temp_eve +
                ", temp_morn=" + temp_morn +
                ", main='" + main + '\'' +
                ", icon='" + icon + '\'' +
                ", degree=" + degree +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
