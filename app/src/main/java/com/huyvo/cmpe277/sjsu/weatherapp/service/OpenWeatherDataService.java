package com.huyvo.cmpe277.sjsu.weatherapp.service;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class OpenWeatherDataService implements DataService{

    private final NetworkService mfNetworkService;

    public OpenWeatherDataService(){
        mfNetworkService = new VolleyNetworkService();
    }

    /*Can fetch weather using imperial or metric*/
    @Override
    public void getWeatherByLatLng(String location,final FutureTaskListener<WeatherModel> listener) {
        String url = "http://api.openweathermap.org/data/2.5/weather?"+location+"&units=imperial&appid=b54f500d4a53fdfc96813a4ba9210417";

        Logger.d("OpenWeatherDataService", "getWeather");

        mfNetworkService.getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                Log.d("OpenWeatherDataSercive", "result = " + result);
                listener.onCompletion(JsonParser.parseWeather(result));
            }

            @Override
            public void onError(String errorMessage) {
                listener.onError(errorMessage);
            }

            @Override
            public void onProgress(float progress) {

            }}

        );

    }

    @Override
    public void getForecastByLatLng(String latLng, final FutureTaskListener<ArrayList<WeatherModel>> listener) {

        String url = "http://api.openweathermap.org/data/2.5/forecast?"
                +latLng
                +
                "&mode=json&units=imperial&cnt=7&appid="
                +
                "b54f500d4a53fdfc96813a4ba9210417";

        mfNetworkService.getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                ArrayList<WeatherModel> weatherModels = JsonParser.parseForecast(result);
                if (result == null) {
                    listener.onError("Json error");
                } else {
                    listener.onCompletion(weatherModels);
                }
            }

            @Override
            public void onError(String errorMessage) {
                listener.onError(errorMessage);
            }

            @Override
            public void onProgress(float progress) {

            }
        });

    }
    @Override
    public void getWeather(String location, FutureTaskListener<WeatherModel> listener) {

    }

    @Override
    public void getForecast(String location, FutureTaskListener<ArrayList<WeatherModel>> listener) {

    }
}
