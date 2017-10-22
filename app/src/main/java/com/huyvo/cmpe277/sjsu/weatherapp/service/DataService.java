package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LatLngModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/5/17.
 */

public interface DataService {
    void getForecast(LatLngModel latLng, FutureTaskListener<WeatherModel> listener);
    void getWeather(LatLngModel latLng, FutureTaskListener<WeatherModel> listener);
    void getWeather(String location, FutureTaskListener<WeatherModel> listener);
    void getForecast(String location, FutureTaskListener<ArrayList<WeatherModel>> listener);
}
