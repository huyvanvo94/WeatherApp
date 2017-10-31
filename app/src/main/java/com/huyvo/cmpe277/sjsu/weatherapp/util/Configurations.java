package com.huyvo.cmpe277.sjsu.weatherapp.util;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class Configurations {
    private static boolean isImperial = true;


    public synchronized static void setUnit(int value){
        if(value == PreferenceManager.Units.IMPERIAL){
            isImperial = true;
        }else{
            isImperial = false;
        }
    }

    public synchronized static void setUnit(boolean b){
        isImperial = b;
    }

    public static synchronized boolean isImperial(){
        return isImperial;
    }

    public synchronized static String getUnit(){
        if(isImperial()){
            return "imperial";
        }
        return "metric";
    }

    public synchronized static String getWeatherRestUrl(String location){
        return "http://api.openweathermap.org/data/2.5/weather?"
                +location+"&units="+getUnit()+"&appid=b54f500d4a53fdfc96813a4ba9210417";
    }

    public synchronized static String getForcastRestUrl(String location){
        return "http://api.openweathermap.org/data/2.5/forecast/daily?"
                +location+"&mode=json&units="+getUnit()+"&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";
    }


}
