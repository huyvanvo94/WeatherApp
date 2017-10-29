package com.huyvo.cmpe277.sjsu.weatherapp;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import java.util.HashMap;

import static com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer.REQUEST_WEATHER_FIVE_MINUTES;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class TodayWeatherContainer {
    public final String TAG = TodayWeatherContainer.class.getSimpleName();
    private HashMap<String, WeatherPackage> mfHashMap;
    private static TodayWeatherContainer instance;

    private TodayWeatherContainer(){
        mfHashMap = new HashMap<>();

    }

    public static synchronized TodayWeatherContainer getInstance(){
        if(instance == null){
            instance = new TodayWeatherContainer();
        }
        return instance;
    }

    public void put(String location, WeatherModel model){
        WeatherPackage wp = new WeatherPackage(location);
        wp.model = model;
        mfHashMap.put(location, wp);
    }

    public WeatherModel getWeatherModel(String location){
        return mfHashMap.get(location).model;
    }

    public boolean shouldRequestFetchWeather(String location){
        if(mfHashMap.get(location) == null){
            return false;
        }

        long timestamp = mfHashMap.get(location).mTimestamp;

        if(System.currentTimeMillis() - timestamp >= REQUEST_WEATHER_FIVE_MINUTES){
            return true;
        }

        return false;
    }

    public void remove(String location){
        mfHashMap.remove(location);
    }

    class WeatherPackage{

        public String mLocation;
        public long mTimestamp;
        public WeatherModel model;

        public WeatherPackage(String location){
            mLocation = location;
            mTimestamp = System.currentTimeMillis();
        }
    }

}
