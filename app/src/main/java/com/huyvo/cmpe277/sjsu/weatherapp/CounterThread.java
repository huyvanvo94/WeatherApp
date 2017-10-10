package com.huyvo.cmpe277.sjsu.weatherapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Huy Vo on 10/9/17.
 */

public class CounterThread extends Thread{
    private Handler mHandler;
    private final MainActivity mainActivity;
    public CounterThread(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        mHandler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){


                int count = msg.what;

                CounterThread.this.mainActivity.updateText(String.valueOf(count));
            }
        };


    }

    @Override
    public void run(){

        for(int i = 0; i < 1000; i++){
            Log.d("O", String.valueOf(i));
            Message message = Message.obtain();
            message.what = i;

            mHandler.sendMessage(message);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
