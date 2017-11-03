package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
    public final static String WHO = "com.huyvo.cmpe277.weatherapp.who";
    public final static String FETCH_WEATHER = "com.huyvo.cmpe277.weatherapp.fetch_weather";

    public FetchForecastIntentService(){
        this(TAG);
    }
    public FetchForecastIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Logger.d(TAG, "onHandleIntent");
        final String location = intent.getStringExtra(FETCH_WEATHER);
        final Bundle bundle = intent.getExtras();

        if(location != null){

            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(final ArrayList<WeatherModel> results) {
                    if(results != null){

                        /**
                        String timeZoneId = TimeZoneContainer.getInstance().getTimeZoneId(location);

                        Log.d("OK", timeZoneId);

                        String timeZoneId = result.timeZoneId;

                        if(DateHelper.numberOfDayFromToday(results.get(0).dt, timeZoneId) < 1){
                            results.remove(0);
                        }else{
                            results.remove(results.size()-1);
                        }

                        for(WeatherModel model: results){
                            model.timeZoneId = timeZoneId;
                        }


                         */
                        WeatherForecastContainer.getInstance().put(location, results);
                        try {
                            Messenger messenger = (Messenger) bundle.get(WHO);
                            Message msg = Message.obtain();
                            bundle.putString(FETCH_WEATHER, location);
                            msg.setData(bundle);
                            if (messenger != null) {
                                messenger.send(msg);
                            }
                        } catch (RemoteException e) {
                            Logger.e(TAG, "Error");
                        }

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
