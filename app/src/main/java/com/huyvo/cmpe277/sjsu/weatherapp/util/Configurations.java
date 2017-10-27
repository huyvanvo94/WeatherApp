package com.huyvo.cmpe277.sjsu.weatherapp.util;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class Configurations {
    public static boolean isImperial = true;

    public static String getUnit(){
        if(isImperial){
            return "imperial";
        }

        return "metric";
    }
}
