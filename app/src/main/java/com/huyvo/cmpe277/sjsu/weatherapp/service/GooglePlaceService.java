package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class GooglePlaceService implements PlaceService {
    @Override
    public void getLocalTime(String location, final FutureTaskListener<LocalTimeModel> listener) {
        String url = "https://maps.googleapis.com/maps/api/timezone/json?location="
                +location+"&timestamp="
                + System.currentTimeMillis() / 1000L
                +"&key=AIzaSyCYI2a3fQhifXLGvgZz2HJVhB0Vd8hvIaM";
        VolleyNetworkService.getInstance().getString(url, "GooglePlaceService", new FutureTaskListener<String>() {
            @Override
            public void onCompletion(String result) {
                LocalTimeModel timeModel = JsonParser.parse(result);
                if(result==null){
                    listener.onError("JsonError");

                }else{
                    listener.onCompletion(timeModel);
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
