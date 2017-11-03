package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

/**
 * Created by Huy Vo on 11/2/17.
 */

public class GooglePlaceService implements PlaceService {
    public final static String TAG = GooglePlaceService.class.getSimpleName();

    @Override
    public void getLocalTime(String location, final FutureTaskListener<LocalTimeModel> listener) {

        String url = "https://maps.googleapis.com/maps/api/timezone/json?location="
                +location+"&timestamp="
                + System.currentTimeMillis() / 1000L
                +"&key=AIzaSyCYI2a3fQhifXLGvgZz2HJVhB0Vd8hvIaM";

        Logger.d(TAG, "url: "+url);
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