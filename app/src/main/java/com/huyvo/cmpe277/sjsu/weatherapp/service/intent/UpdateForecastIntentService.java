package com.huyvo.cmpe277.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
 * Created by Huy Vo on 10/27/17.
 */

public class UpdateForecastIntentService extends IntentService {
    public final static String TAG = "UpdateForecastIntentService";

    public final static String UPDATE = "com.huyvo.cmpe277.sjsu.update";
    public final static String LOCATION = "com.huyvo.cmpe277.sjsu.key";

    public UpdateForecastIntentService(){
        this(TAG);
    }

    public UpdateForecastIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Messenger messenger = (Messenger) bundle.get(UPDATE);
            final Message msg = Message.obtain();
            final String location = bundle.getString(LOCATION);

            if (location != null) {
                DataService service = new OpenWeatherDataService();
                service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                    @Override
                    public void onCompletion(final ArrayList<WeatherModel> result) {
                        if (result != null) {
                            Logger.d(TAG, "onHandleIntent" + result.toString());

                            WeatherModel weatherModel = result.get(0);
                            PlaceService placeService = new GooglePlaceService();
                            placeService.getLocalTime(weatherModel.lat + "," + weatherModel.lon, new FutureTaskListener<LocalTimeModel>() {
                                @Override
                                public void onCompletion(LocalTimeModel resultLocalTime) {
                                    try {
                                        Logger.d(TAG, resultLocalTime.timeZoneId);
                                        result.get(0).timeZoneId = resultLocalTime.timeZoneId;
                                        WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                                        weatherForecastContainer.put(location, result);

                                        Bundle b = intent.getExtras();
                                        b.putString(LOCATION, location);
                                        msg.setData(b);
                                        try {
                                            messenger.send(msg);
                                        } catch (RemoteException e) {

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
}
