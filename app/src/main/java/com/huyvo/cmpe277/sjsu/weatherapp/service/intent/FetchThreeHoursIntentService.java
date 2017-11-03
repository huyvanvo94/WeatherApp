package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.ThreeHourWeatherContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 11/2/17.
 */

public class FetchThreeHoursIntentService extends IntentService {
    public final static String TAG = FetchThreeHoursIntentService.class.getSimpleName();
    public final static String FETCH_THREE_HOURS = "com.huyvo.fetch";
    public final static String WHO = "com.huyvo.who";

    public FetchThreeHoursIntentService(){
        this(TAG);
    }
    public FetchThreeHoursIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        final String location = intent.getStringExtra(FETCH_THREE_HOURS);
        if(location != null){
            DataService service = new OpenWeatherDataService();
            service.getWeatherThreeHoursLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {

                    ThreeHourWeatherContainer container = ThreeHourWeatherContainer.getInstance();
                    container.put(location, result);

                    Bundle bundle = intent.getExtras();
                    final Messenger messenger = (Messenger) bundle.get(WHO);
                    if(messenger != null) {
                        final Message msg = Message.obtain();
                        Bundle b = intent.getExtras();
                        b.putString(FETCH_THREE_HOURS, location);
                        msg.setData(b);
                        try {
                            messenger.send(msg);
                        } catch (RemoteException e) {

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
