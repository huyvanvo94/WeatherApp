package com.huyvo.cmpe277.sjsu.weatherapp;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.BaseActivityWithFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivityWithFragment{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=alameda&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";

        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
    }
}