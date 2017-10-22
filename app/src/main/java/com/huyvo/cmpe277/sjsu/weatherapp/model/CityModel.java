package com.huyvo.cmpe277.sjsu.weatherapp.model;

// A simple city model class
public class CityModel implements BaseModel {
    public String cityName;
    public String localTime;
    public String currentTemp;
    public String latlng;


    public CityModel(String cityName, String localTime, String currentTemp, String latlng){
        this.cityName = cityName;
        this.localTime = localTime;
        this.currentTemp = currentTemp;
        this.latlng = latlng;
    }

}
