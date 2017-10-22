package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huyvo.cmpe277.sjsu.weatherapp.Singleton;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class VolleyNetworkService extends Singleton implements NetworkService {
    private static final int VOLLEY_TIME_OUT = 3000;
    private static final int NUMBER_OF_RETRY = 0;

    private final RequestQueue mfRequestQueue;

    public VolleyNetworkService(){

        mfRequestQueue = Volley.newRequestQueue(WeatherApp.getInstance());
    }
    @Override
    public void cancel(String tag) {

        mfRequestQueue.cancelAll(tag);
    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void getString(String url, String tag, FutureTaskListener<String> listener) {

    }

    @Override
    protected void shutdown() {

    }
}
