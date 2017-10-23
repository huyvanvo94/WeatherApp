package com.huyvo.cmpe277.sjsu.weatherapp.service;

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
    public void getWeatherByLatLng(String latLng,final FutureTaskListener<WeatherModel> listener) {
        String url = "http://api.openweathermap.org/data/2.5/weather?"+latLng+
                "&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";


        mfNetworkService.getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                Logger.e("OpenWeatherDataSercive", "result = " + result);
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
    public void getForecastByLatLng(String latLng, FutureTaskListener<WeatherModel> listener) {

        String url = "http://api.openweathermap.org/data/2.5/forecast?"+latLng.toString()+
                "&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";
    }
    @Override
    public void getWeather(String location, FutureTaskListener<WeatherModel> listener) {

    }

    @Override
    public void getForecast(String location, FutureTaskListener<ArrayList<WeatherModel>> listener) {

    }
}
