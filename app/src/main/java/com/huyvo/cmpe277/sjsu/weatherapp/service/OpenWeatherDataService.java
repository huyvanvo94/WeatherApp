package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LatLngModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

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
    public void getWeather(LatLngModel latLng, FutureTaskListener<WeatherModel> listener) {
        String url = "http://api.openweathermap.org/data/2.5/weather?"+latLng.toString()+
                "&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";
    }

    @Override
    public void getForecast(LatLngModel latLng, FutureTaskListener<WeatherModel> listener) {

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
