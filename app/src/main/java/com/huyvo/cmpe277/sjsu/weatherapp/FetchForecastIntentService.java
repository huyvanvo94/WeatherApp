package com.huyvo.cmpe277.sjsu.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonHelper;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Huy Vo on 10/26/17.
 */

public class FetchForecastIntentService extends IntentService {

    public final static String TAG = "FetchForecastIntentService";

    public final static String FETCH_WEATHER = "com.huyvo.cmpe277.weatherapp.fetch_weather";

    public FetchForecastIntentService(){
        this(TAG);
    }
    public FetchForecastIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.d(TAG, "onHandleIntent");
        final String location = intent.getStringExtra(FETCH_WEATHER);

        if(location != null){
            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(final ArrayList<WeatherModel> result) {
                    if(result != null){
                        Logger.d(TAG, "onHandleIntent"+result.toString());

                        WeatherModel weatherModel = result.get(0);

                        String url = "https://maps.googleapis.com/maps/api/timezone/json?location="+weatherModel.lat+","+weatherModel.lon+"&timestamp="+ DateHelper.getTimeStamp()+"&key="+getString(R.string.google_timezone_key);
                        ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            result.get(0).timeZoneId = JsonHelper.getString(response, "timeZoneId");
                                            WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                                            weatherForecastContainer.put(location, result);


                                        }catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                            }
                        }));


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
