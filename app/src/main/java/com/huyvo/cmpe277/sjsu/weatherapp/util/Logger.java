package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.service.GooglePlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.intent.FetchForecastIntentService;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.CityListViewActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.MainActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.VolleyNetworkService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Huy Vo on 10/5/17.
 */

public final class Logger {
    private Logger() {}

    private static final List<String> CLASS_SIMPLE_NAMES = Arrays.asList(
            MainActivity.TAG,
            CityListViewActivity.TAG,
            WeatherFragment.TAG,
            WeatherApp.TAG,
            WeatherForecastContainer.TAG,
            FetchForecastIntentService.TAG,
            OpenWeatherDataService.TAG,
            VolleyNetworkService.TAG,
            GooglePlaceService.TAG
    );

    public static synchronized void d(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.d(tag, msg);
        }
    }

    public static synchronized void e(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.e(tag, msg);
        }
    }
}