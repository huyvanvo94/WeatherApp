package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import java.util.List;

/**
 * Created by Huy Vo on 11/2/17.
 */

public class ThreeHoursViewAdapter extends ArrayAdapter<WeatherModel> {

    private String mTimeZoneId;

    public ThreeHoursViewAdapter(Context context, List<WeatherModel> objects, String timeZoneId){
        super(context, 0, objects);
        mTimeZoneId = timeZoneId;
    }
}
