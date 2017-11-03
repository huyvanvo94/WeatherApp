package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/5/17.
 */

public interface DataService {
    void getForecastByLatLng(String location, FutureTaskListener<ArrayList<WeatherModel>> listener);
    void getWeatherByLatLng(String location, FutureTaskListener<WeatherModel> listener);
    void getWeatherThreeHoursLatLng(String location, FutureTaskListener<ArrayList<WeatherModel>> listener);
}
