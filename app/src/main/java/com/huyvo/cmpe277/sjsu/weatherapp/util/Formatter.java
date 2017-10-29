package com.huyvo.cmpe277.sjsu.weatherapp.util;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class Formatter {
    public String formatTemperature(float temp){
        if(Configurations.isImperial){
            return String.valueOf(temp) + "\u00B0" + " F";
        }else{
            return String.valueOf(temp) + "\u00B0" + " C";
        }
    }

    public String formatDate(WeatherModel weatherModel){
        return weatherModel.getDayOfTheWeek() + " " + weatherModel.getDate();
    }
}
