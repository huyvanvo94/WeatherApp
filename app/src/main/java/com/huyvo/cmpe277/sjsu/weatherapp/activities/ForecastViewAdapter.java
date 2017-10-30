package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Formatter;


import java.util.List;

/**
 * Created by kgade on 10/29/2017.
 */

public class ForecastViewAdapter extends ArrayAdapter<WeatherModel> {


    public ForecastViewAdapter(Context context, List<WeatherModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        WeatherModel weatherModel = getItem(position);
        Formatter formatter = new Formatter();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_forecast_view, parent, false);
        }

        TextView dayTextView = (TextView) convertView.findViewById(R.id.text_view_day);
        TextView dayTempTextView = (TextView) convertView.findViewById(R.id.text_view_day_temp);
        TextView tempRangeTextView = (TextView) convertView.findViewById(R.id.text_view_temp_range);

        dayTextView.setText(weatherModel.getDayOfTheWeek());
        dayTempTextView.setText(formatter.formatTemperature(weatherModel.temp_day));
        tempRangeTextView.setText(formatter.formatTemperatureRange(weatherModel.temp_min, weatherModel.temp_max));

        return convertView;

    }

}
