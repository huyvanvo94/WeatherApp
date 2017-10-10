package com.huyvo.cmpe277.sjsu.weatherapp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by Huy Vo on 10/9/17.
 */

public class WeatherHandlerThread extends HandlerThread {
    private Handler mHandler;
    public WeatherHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message message){

            }
        };
    }

    private void handleRequest(){

    }
}
