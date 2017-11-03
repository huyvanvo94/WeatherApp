package com.huyvo.cmpe277.sjsu.weatherapp;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Huy Vo on 11/2/17.
 */

public class ThreeHourWeatherContainer {
    public final String TAG = ThreeHourWeatherContainer.class.getSimpleName();
    private HashMap<String, ThreeHourWeatherPackage> mfHashMap;
    private static ThreeHourWeatherContainer instance;

    private ThreeHourWeatherContainer(){
        mfHashMap = new HashMap<>();

    }

    public static synchronized ThreeHourWeatherContainer getInstance(){
        if(instance == null){
            instance = new ThreeHourWeatherContainer();
        }
        return instance;
    }

    public List<WeatherModel> getWeatherModels(String location){
        return mfHashMap.get(location).models;
    }

    public void remove(String location){
        mfHashMap.remove(location);
    }


    public void put(String location, List<WeatherModel> models){
        ThreeHourWeatherPackage threeHourWeatherPackage = new ThreeHourWeatherPackage(location);
        threeHourWeatherPackage.models = models;
        mfHashMap.put(location, threeHourWeatherPackage );
    }

    class ThreeHourWeatherPackage{

        public String mLocation;
        public long mTimestamp;
        public List<WeatherModel> models;

        public ThreeHourWeatherPackage(String location){
            mLocation = location;
            mTimestamp = System.currentTimeMillis();
        }
    }


}
