package com.huyvo.cmpe277.sjsu.weatherapp.model;

import com.huyvo.cmpe277.sjsu.weatherapp.util.Configurations;
import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;

import java.util.Comparator;

// A simple city model class
public class CityModel implements BaseModel, Comparator<CityModel> {
    public String cityName;
    public String currentTemp;
    public String latlng;

    public String location;

    public String timeZoneId;

    public String getLocalTime(){
        long unixTime = System.currentTimeMillis() / 1000L;
        return DateHelper.getLocalTime(unixTime, timeZoneId);
    }

    public String getCurrentTemp(){
        if(Configurations.isImperial){
            return currentTemp + "F";
        }
        return currentTemp+"C";
    }

    @Override
    public int compare(CityModel a, CityModel b) {
        if(a.cityName.compareTo(b.cityName) < 0){
            return -1;
        }else if(a.cityName.compareTo(b.cityName) > 0){
            return 1;
        }
        return 0;
    }
}
