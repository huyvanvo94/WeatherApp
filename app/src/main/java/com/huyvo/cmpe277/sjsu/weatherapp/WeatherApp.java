package com.huyvo.cmpe277.sjsu.weatherapp;

import android.app.Application;

import com.huyvo.cmpe277.sjsu.weatherapp.service.VolleyNetworkService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherApp extends Application {

    public static final String TAG = "WeatherApp";
    private static Application mApplication;
    private static List<String> mLatLngList;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        VolleyNetworkService.getInstance();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }



    public static synchronized Application getInstance() {
        return mApplication;
    }

    public static synchronized List<String> getLatLngList(){
        if(mLatLngList == null){
            mLatLngList = new ArrayList<>();
        }
        return mLatLngList;
    }

    public void cancelAll(){
       VolleyNetworkService.getInstance().cancelAll();
    }
}
