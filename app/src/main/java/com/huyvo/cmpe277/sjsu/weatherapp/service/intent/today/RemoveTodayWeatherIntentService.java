package com.huyvo.cmpe277.sjsu.weatherapp.service.intent.today;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.TodayWeatherContainer;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class RemoveTodayWeatherIntentService extends IntentService {

    public final static String TAG = RemoveTodayWeatherIntentService.class.getSimpleName();
    public final static String DELETE = "com.huyvo.cmpe277.delete";
    public RemoveTodayWeatherIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
      try{
            final String location = intent.getStringExtra(DELETE);
            if (location != null) {
                TodayWeatherContainer container = TodayWeatherContainer.getInstance();
                container.remove(location);
            }
        }catch (NullPointerException exception){

        }
    }
}
