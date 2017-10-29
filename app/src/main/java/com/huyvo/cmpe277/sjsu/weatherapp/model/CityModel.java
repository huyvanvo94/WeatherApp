package com.huyvo.cmpe277.sjsu.weatherapp.model;

import com.huyvo.cmpe277.sjsu.weatherapp.util.Configurations;
import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;

import java.util.Comparator;

// A simple city model class
public class CityModel implements BaseModel, Comparator<CityModel> {
    public String cityName;
    public String currentTemp;
    public String latlng;
    public String icon;

    public String location;

    public String timeZoneId;

    public String getLocalTime(){
        long unixTime = System.currentTimeMillis() / 1000L;
        return DateHelper.getLocalTime(unixTime, timeZoneId);
    }

    public static CityModel makeFrom(WeatherModel weatherModel){
        CityModel cityModel = new CityModel();
        cityModel.timeZoneId = weatherModel.timeZoneId;
        cityModel.cityName = weatherModel.city;
        cityModel.icon = weatherModel.icon;
        cityModel.latlng = weatherModel.lat+","+weatherModel.lon;


        if(weatherModel.temp_day != 0.0){
            cityModel.currentTemp = String.valueOf((int) weatherModel.temp_day);
        }else{
            cityModel.currentTemp = String.valueOf((int) weatherModel.temp);
        }


        cityModel.location = "lat="+weatherModel.lat+"&lon="+weatherModel.lon;
        return cityModel;
    }

    public String getCurrentTemp(){
        if(Configurations.isImperial){
            return currentTemp + " F";
        }
        return currentTemp+" C";
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
