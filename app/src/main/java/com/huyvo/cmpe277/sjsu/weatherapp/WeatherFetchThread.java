package com.huyvo.cmpe277.sjsu.weatherapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Huy Vo on 10/9/17.
 */

public class WeatherFetchThread extends Thread {
    private Handler mHandler;

    public WeatherFetchThread(){
        Looper.prepare();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){

            }
        };

        Looper.loop();
    }
}
