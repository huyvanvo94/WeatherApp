package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.GooglePlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.PlaceService;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class ConditionTester {

    public static void testThree(){

        final String location = "lat=37.7652065&lon=-122.2416355";

        DataService dataService = new OpenWeatherDataService();
        dataService.getWeatherThreeHoursLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
            @Override
            public void onCompletion(final ArrayList<WeatherModel> results) {

                PlaceService service = new GooglePlaceService();

                service.getLocalTime("37.7652065,-122.2416355", new FutureTaskListener<LocalTimeModel>() {
                    @Override
                    public void onCompletion(LocalTimeModel result) {

                         for (WeatherModel w: results){
                             w.timeZoneId = result.timeZoneId;
                             Log.d("OK", w.getFutureTime());
                             Log.d("OK", w.getDayOfTheWeekWithTimeZone());
                             Log.d("OK", w.getDateWithTimeZone());

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

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }


    public static void testWeather(){
        String location = "lat=37.7652065&lon=-122.2416355";
        DataService service = new OpenWeatherDataService();
        service.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
            @Override
            public void onCompletion(WeatherModel result) {

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
            public void onCompletion(final ArrayList<WeatherModel> results) {

                PlaceService service = new GooglePlaceService();

                service.getLocalTime("37.7652065,-122.2416355", new FutureTaskListener<LocalTimeModel>() {
                    @Override
                    public void onCompletion(LocalTimeModel result) {

                        for (WeatherModel w: results){
                            w.timeZoneId = result.timeZoneId;
                            Log.d("OK", w.dt+"");
                            Log.d("OK", w.getDayOfTheWeekWithTimeZone());
                            Log.d("OK", w.getDateWithTimeZone());

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

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onProgress(float progress) {

            }
        });

    }

}
