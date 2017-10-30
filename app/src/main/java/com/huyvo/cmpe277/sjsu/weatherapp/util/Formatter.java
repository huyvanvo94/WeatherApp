package com.huyvo.cmpe277.sjsu.weatherapp.util;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class Formatter {
    public String formatTemperature(float temp){
        if(Configurations.isImperial){
            return String.valueOf((int)temp) + "\u00B0" + " F";
        }else{
            return String.valueOf((int)temp) + "\u00B0" + " C";
        }
    }

    public String formatMaxTemperature(float temp_max){
        if(Configurations.isImperial){
            return "Max: " + String.valueOf((int)temp_max) + "\u00B0" + " F";
        }else{
            return "Max: " + String.valueOf((int)temp_max) + "\u00B0" + " C";
        }
    }

    public String formatMinTemperature(float temp_min){
        if(Configurations.isImperial){
            return "Min: " + String.valueOf((int)temp_min) + "\u00B0" + " F";
        }else{
            return "Min: " + String.valueOf((int)temp_min) + "\u00B0" + " C";
        }
    }

    public String formatHumidity(int humidity){
        return String.valueOf(humidity) + " %";
    }

    public String formatPressure(int pressure){
        return String.valueOf(pressure) + " hPa";
    }

    public String formatWindSpeed(float windSpeed){
        if(Configurations.isImperial){
            return String.valueOf((int) windSpeed) + " mph";
        }else{
            return String.valueOf((int) windSpeed) + " m/sec";
        }
    }

    public String formatDate(WeatherModel weatherModel){
        return weatherModel.getDayOfTheWeek() + " " + weatherModel.getDate();
    }
}
