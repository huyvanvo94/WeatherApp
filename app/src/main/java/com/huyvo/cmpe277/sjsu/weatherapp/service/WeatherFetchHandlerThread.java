package com.huyvo.cmpe277.sjsu.weatherapp.service;

import android.os.HandlerThread;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherFetchHandlerThread extends HandlerThread{
    private static final String TAG = "WeatherFetchHandlerThread";

    public WeatherFetchHandlerThread(){
        this(TAG);
    }
    public WeatherFetchHandlerThread(String name) {
        super(name);
    }
}
