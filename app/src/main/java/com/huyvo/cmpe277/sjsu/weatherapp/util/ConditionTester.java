package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class ConditionTester {

    public static void testWeather(){
        String location = "lat=37.7652065&lon=-122.2416355";
        DataService service = new OpenWeatherDataService();
        service.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
            @Override
            public void onCompletion(WeatherModel result) {

                Log.d("Test", result.getDate());
                Log.d("Test", result.description);


            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    public static void testForcast(){

        String location = "lat=37.7652065&lon=-122.2416355";

        DataService service = new OpenWeatherDataService();
        service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
            @Override
            public void onCompletion(ArrayList<WeatherModel> result) {

                for(WeatherModel weatherModel:result){
                    Log.d("Test", weatherModel.toString());
                }

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onProgress(float progress) {

            }
        });

    }

}
