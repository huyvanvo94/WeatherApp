package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class OpenWeatherDataService implements DataService{
    public final static String TAG = OpenWeatherDataService.class.getSimpleName();


    public OpenWeatherDataService(){
    }

    /*Can fetch weather using imperial or metric*/
    @Override
    public void getWeatherByLatLng(String location,final FutureTaskListener<WeatherModel> listener) {
        String url = "http://api.openweathermap.org/data/2.5/weather?"+location+"&units=imperial&appid=b54f500d4a53fdfc96813a4ba9210417";


        Logger.d(TAG, "getWeather: " + url);

        VolleyNetworkService.getInstance().getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                WeatherModel model = JsonParser.parseWeather(result);
                listener.onCompletion(model);
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
    public void getWeatherThreeHoursLatLng(String location, final FutureTaskListener<ArrayList<WeatherModel>> listener) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?"
                +location+
                "&mode=json&units=imperial&cnt=8&appid=b54f500d4a53fdfc96813a4ba9210417";

        Logger.d(TAG, "get weather three hours: " + url);
        VolleyNetworkService.getInstance().getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                if(result==null){
                    listener.onError("Error");
                }

                ArrayList<WeatherModel> weatherModels = JsonParser.parseThreeHourWeather(result);
                listener.onCompletion(weatherModels);

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
    public void getForecastByLatLng(String latLng, final FutureTaskListener<ArrayList<WeatherModel>> listener) {

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?"+latLng+"&mode=json&units=imperial&cnt=6&appid="+"b54f500d4a53fdfc96813a4ba9210417";
        Logger.d(TAG, "getForecast: "+url );
        VolleyNetworkService.getInstance().getString(url, "OpenWeatherDataService", new FutureTaskListener<String>() {
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


}
