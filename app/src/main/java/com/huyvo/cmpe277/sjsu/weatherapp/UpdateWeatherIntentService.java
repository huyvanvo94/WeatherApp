package com.huyvo.cmpe277.sjsu.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

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
 * Created by Huy Vo on 10/27/17.
 */

public class UpdateWeatherIntentService extends IntentService {
    public final static String TAG = "UpdateWeatherIntentService";

    public final static String UPDATE = "com.huyvo.cmpe277.sjsu.update";
    public final static String LOCATION = "com.huyvo.cmpe277.sjsu.location";

    public UpdateWeatherIntentService(){
        this(TAG);
    }

    public UpdateWeatherIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.d("OK", "onHandleIntent");
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

                            String url = "https://maps.googleapis.com/maps/api/timezone/json?location=" + weatherModel.lat + "," + weatherModel.lon + "&timestamp=" + DateHelper.getTimeStamp() + "&key=" + getString(R.string.google_timezone_key);
                            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                result.get(0).timeZoneId = JsonHelper.getString(response, "timeZoneId");
                                                WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                                                weatherForecastContainer.put(location, result);

                                                Bundle b = intent.getExtras();
                                                b.putString(LOCATION, location);
                                                msg.setData(b);
                                                Log.d("OK", "Yes!");
                                                try {
                                                    messenger.send(msg);
                                                } catch (RemoteException e) {
                                                    Log.i("error", "error");
                                                }
                                            } catch (Exception e) {
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
}
