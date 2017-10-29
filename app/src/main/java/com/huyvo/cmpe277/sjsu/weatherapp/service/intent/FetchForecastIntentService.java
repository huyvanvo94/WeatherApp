package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/26/17.
 */

public class FetchForecastIntentService extends IntentService {

    public final static String TAG = "FetchForecastIntentService";

    public final static String FETCH_WEATHER = "com.huyvo.cmpe277.weatherapp.fetch_weather";

    public FetchForecastIntentService(){
        this(TAG);
    }
    public FetchForecastIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.d(TAG, "onHandleIntent");
        final String location = intent.getStringExtra(FETCH_WEATHER);

        if(location != null){
            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(final ArrayList<WeatherModel> result) {
                    if(result != null){
                        //Logger.d(TAG, "onHandleIntent"+result.toString());

                        WeatherForecastContainer.getInstance().put(location, result);

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
