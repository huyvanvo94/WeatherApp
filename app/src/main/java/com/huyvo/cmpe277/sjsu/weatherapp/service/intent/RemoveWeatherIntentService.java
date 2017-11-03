package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.ThreeHourWeatherContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.TodayWeatherContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer;

/**
 * Created by Huy Vo on 10/28/17.
 */

public class RemoveWeatherIntentService extends IntentService {
    public final static String REMOVE_LOCATION = "com.huyvo.cmpe277.sjsu.remove_location";
    public final static String TAG = RemoveWeatherIntentService.class.getSimpleName();

    public RemoveWeatherIntentService(){
        this(TAG);
    }

    public RemoveWeatherIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            final String location = intent.getStringExtra(REMOVE_LOCATION);
            if (location != null) {
                WeatherForecastContainer.getInstance().remove(location);
                TodayWeatherContainer.getInstance().remove(location);
                ThreeHourWeatherContainer.getInstance().remove(location);
                WeatherApp.getLatLngList().remove(location);
            }
        }catch (NullPointerException exception){

        }

    }
}
