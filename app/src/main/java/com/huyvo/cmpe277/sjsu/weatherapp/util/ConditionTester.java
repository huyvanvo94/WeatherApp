package com.huyvo.cmpe277.sjsu.weatherapp.util;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class ConditionTester {

    public void loadTester(){

        if(true){
            String location = "lat=37.7652065&lon=-122.2416355";

            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {

                    for(WeatherModel weatherModel:result){
                        Logger.d(TAG, weatherModel.toString());
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

}
