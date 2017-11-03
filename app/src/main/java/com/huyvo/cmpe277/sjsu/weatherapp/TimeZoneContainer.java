package com.huyvo.cmpe277.sjsu.weatherapp;

import java.util.HashMap;

/**
 * Created by Huy Vo on 11/3/17.
 */

public class TimeZoneContainer {


    private final HashMap<String, String> mfHashMap = new HashMap<>();

    private TimeZoneContainer(){

    }

    private static TimeZoneContainer instance = new TimeZoneContainer();

    public String getTimeZoneId(String location){
        return mfHashMap.get(location);
    }

    public void put(String location, String timeZoneId){
        mfHashMap.put(location, timeZoneId);
    }

    public static synchronized TimeZoneContainer getInstance(){
        return instance;
    }

}
