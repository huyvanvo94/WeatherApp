package com.huyvo.cmpe277.sjsu.weatherapp.service.intent.today;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.huyvo.cmpe277.sjsu.weatherapp.TodayWeatherContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.GooglePlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.PlaceService;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class FetchTodayWeatherIntentService extends IntentService {
    public final static String FETCH_WEATHER = "com.huyvo.cmpe277.weatherapp.fetch_weather";
    public final static String CITY = "com.huyvo.city";
    public final static String WHO = "com.huyvo.cmpe277.weatherapp.who";

    public final static String TAG = FetchTodayWeatherIntentService.class.getSimpleName();

    public FetchTodayWeatherIntentService(){
        this(TAG);
    }
    public FetchTodayWeatherIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final String location = intent.getStringExtra(FETCH_WEATHER);

            if(location != null){
                DataService service = new OpenWeatherDataService();
                service.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
                    @Override
                    public void onCompletion(final WeatherModel result) {

                        PlaceService placeService = new GooglePlaceService();
                        placeService.getLocalTime(result.lat+","+result.lon, new FutureTaskListener<LocalTimeModel>() {
                            @Override
                            public void onCompletion(LocalTimeModel resultLocalTime) {
                                result.location = location;
                                result.city = WeatherApp.findCity(result.location);
                                result.timeZoneId = resultLocalTime.timeZoneId;

                                String city = intent.getStringExtra(CITY);
                                if(city != null) {
                                    result.city = city;
                                }

                                TodayWeatherContainer todayWeatherContainer = TodayWeatherContainer.getInstance();
                                todayWeatherContainer.put(location, result);

                                Messenger messenger = (Messenger) bundle.get(WHO);
                                Message msg = Message.obtain();
                                Bundle b = intent.getExtras();
                                b.putString(FETCH_WEATHER, location);
                                msg.setData(b);
                                try {
                                    messenger.send(msg);
                                } catch (RemoteException e) {}
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }

                            @Override
                            public void onProgress(float progress) {

                            }
                        });
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
