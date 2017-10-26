package com.huyvo.cmpe277.sjsu.weatherapp.model;

import java.util.Comparator;

// A simple city model class
public class CityModel implements BaseModel, Comparator<CityModel> {
    public String cityName;
    public String localTime;
    public String currentTemp;
    public String latlng;


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
