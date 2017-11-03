package com.huyvo.cmpe277.sjsu.weatherapp.model;

import android.location.Address;

import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherModel implements BaseModel {
    public String location;
    public static final int NO_RESOURCE = -1;
    public long dt;
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

    public String timeLocationFormat(){
        return lat+","+lon;
    }

    public String getLocationFormat(){
        return "lat="+lat+"&lon="+lon;
    }

    public String getKey(){
        String lat = String.format("%.2f", this.lat);
        String lon = String.format("%.2f", this.lon);

        return "lat="+ lat +"&lon="+lon;
    }

    public float getTemp(){
        if(temp == 0.0){
            return temp_day;
        }
        return temp;
    }

    public boolean isMyLocation(Address address){
        if (address == null){
            return false;
        }
        return country.equals(address.getCountryCode()) && city.equals(address.getLocality());
    }

    public String getFutureTime(){
        return DateHelper.getLocalTime(dt, timeZoneId);
    }

    public String getLocalTime(){
        long unixTime = System.currentTimeMillis() / 1000L;
        return DateHelper.getLocalTime(unixTime, timeZoneId);
    }


    public String getDayOfTheWeekWithTimeZone(){
        return DateHelper.getDate(dt, timeZoneId, "EEE");
    }

    public String getDateWithTimeZone(){
        return DateHelper.getDate(dt, timeZoneId, "MMM d");
    }

    @Override
    public String toString() {
        return "WeatherModel{" +
                "location='" + location + '\'' +
                ", dt=" + dt +
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
                ", lat=" + lat +
                ", lon=" + lon +
                ", main='" + main + '\'' +
                ", icon='" + icon + '\'' +
                ", degree=" + degree +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", timeZoneId='" + timeZoneId + '\'' +
                '}';
    }
}
