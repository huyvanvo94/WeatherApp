package com.huyvo.cmpe277.sjsu.weatherapp;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class WeatherApp extends Application {

    public static final String TAG = "WeatherApp";
    private static Application mApplication;
    private RequestQueue mRequestQueue;
    private static List<String> mLatLngList;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static synchronized Application getInstance() {
        return mApplication;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public static synchronized List<String> getLatLngList(){

        if(mLatLngList == null){
            mLatLngList = new ArrayList<>();
        }
        Logger.d(TAG, mLatLngList.toString());
        return mLatLngList;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
