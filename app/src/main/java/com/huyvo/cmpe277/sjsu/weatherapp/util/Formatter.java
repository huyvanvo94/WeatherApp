package com.huyvo.cmpe277.sjsu.weatherapp.util;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

/**
 * By default, the network fetches imperial.
 * code below will reflect so.
 */

public class Formatter {
    public String formatTemperature(float temp){
        if(Configurations.isImperial()){
            return String.valueOf( (int) temp) + "\u00B0" + " F";
        }else{
            return String.valueOf((int) convertFtoC(temp)) + "\u00B0" + " C";

        }
        /**

        if(Configurations.isImperial()){
            return String.valueOf((int)temp) + "\u00B0" + " F";
        }else{
            return String.valueOf((int)temp) + "\u00B0" + " C";
        }*/
    }

    public String formatMaxTemperature(float temp_max){

        if(Configurations.isImperial()){
            return "Max: " + String.valueOf( (int) temp_max) + "\u00B0" + " F";
        }else {
            return "Max: " +String.valueOf ( (int) convertFtoC(temp_max))+ "\u00B0" + " C";
        }
        /**
        if(Configurations.isImperial()){
            return "Max: " + String.valueOf((int)temp_max) + "\u00B0" + " F";
        }else{
            return "Max: " + String.valueOf((int)temp_max) + "\u00B0" + " C";
        }*/
    }

    public String formatMinTemperature(float temp_min){
        if(Configurations.isImperial()){
            return "Min: " + String.valueOf( (int) temp_min ) + "\u00B0" + " F";
        }else{
            return "Min: " +String.valueOf( (int) convertFtoC(temp_min)) + "\u00B0" + " C";
        }

        /**
         *
         * if(Configurations.isImperial()){
         return "Min: " + String.valueOf((int)temp_min) + "\u00B0" + " F";
         }else{
         return "Min: " + String.valueOf((int)temp_min) + "\u00B0" + " C";
         }
         */
    }

    public String formatTemperatureRange(float temp_min, float temp_max){

        if(Configurations.isImperial()){
            return String.valueOf( (int) temp_min) + " F - " + String.valueOf( (int) temp_max )+ " F";
        }else{
            return String.valueOf( (int) convertFtoC(temp_min)) + " C - " + String.valueOf( (int) convertFtoC(temp_max) ) + " C";
        }
        /**
        if(Configurations.isImperial()){
            return String.valueOf((int) temp_min) + " F - " + String.valueOf((int) temp_max) + " F";
        }else{
            return String.valueOf((int) temp_min) + " C - " + String.valueOf((int) temp_max) + " C";
        }*/
    }

    public String formatHumidity(int humidity){
        return humidity + " %";
    }

    public String formatPressure(int pressure){
        return pressure + " hPa";
    }

    public String formatWindSpeed(float windSpeed){
        return (int) windSpeed + " mph";

        /**
         *  if(Configurations.isImperial){
         return String.valueOf((int) windSpeed) + " mph";
         }else{
         return String.valueOf((int) windSpeed) + " m/sec";
         }
         */
    }

    public String formatDate(WeatherModel weatherModel){
        return weatherModel.getDayOfTheWeekWithTimeZone() + " " + weatherModel.getDateWithTimeZone();
    }


    public double convertFtoC(double f){
        return (f - 32.0)/1.8;
    }

    public double convertCtoF(double c){
        return (c * 1.8) + 32;
    }
}
