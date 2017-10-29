package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.GooglePlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.PlaceService;
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

                        WeatherModel weatherModel = result.get(0);
                        PlaceService placeService = new GooglePlaceService();
                        placeService.getLocalTime(weatherModel.lat+","+weatherModel.lon, new FutureTaskListener<LocalTimeModel>() {
                            @Override
                            public void onCompletion(LocalTimeModel resultLocalTime) {
                                //Logger.d(TAG, resultLocalTime.timeZoneId);
                                result.get(0).timeZoneId = resultLocalTime.timeZoneId;
                                WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                                weatherForecastContainer.put(location, result);


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
