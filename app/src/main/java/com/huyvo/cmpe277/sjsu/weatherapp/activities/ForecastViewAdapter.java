package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
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


    private String mTimeZoneId;
    public ForecastViewAdapter(Context context, List<WeatherModel> objects, String timeZoneId) {
        super(context, 0, objects);

        mTimeZoneId = timeZoneId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        WeatherModel weatherModel = getItem(position);
        weatherModel.timeZoneId = mTimeZoneId;
        Formatter formatter = new Formatter();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_forecast_view, parent, false);
        }



        TextView dayTextView = (TextView) convertView.findViewById(R.id.text_view_day);
        TextView dayTempTextView = (TextView) convertView.findViewById(R.id.text_view_day_temp);
        TextView tempRangeTextView = (TextView) convertView.findViewById(R.id.text_view_temp_range);

        dayTextView.setText(weatherModel.getDayOfTheWeekWithTimeZone());
        dayTempTextView.setText(formatter.formatTemperature(weatherModel.temp_day));
        tempRangeTextView.setText(formatter.formatTemperatureRange(weatherModel.temp_min, weatherModel.temp_max));
        setIcon(weatherModel, tempRangeTextView);

        return convertView;

    }


    private void setIcon(WeatherModel model, TextView dayTempTextView){

        switch(model.icon) {
            case ("01d"): case "01": //sunny cond
                Drawable icon_sunny = getContext().getDrawable(R.drawable.icon_sunny);
                icon_sunny.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_sunny, null, null, null);
                break;
            case ("01n"):
                Drawable icon_moon = getContext().getDrawable(R.drawable.icon_moon);
                icon_moon.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_moon, null, null, null);
            case ("02d"): case "02":case "02n"://cloudy cond
                Drawable icon_partly_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_partly_cloudy.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_partly_cloudy, null, null, null);
                break;
            case ("03d"): case "03":case "03n"://scattered clouds cond
                Drawable icon_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_cloudy.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_cloudy, null, null, null);
                break;
            case ("04d"): case "04":case "04n"://cloudy cond
                Drawable icon_broken_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_broken_cloudy .setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_broken_cloudy , null, null, null);
                break;
            case ("09d"): case "09":case "09n": //shower cond
                Drawable icon_shower = getContext().getDrawable(R.drawable.icon_rain);
                icon_shower.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_shower, null, null, null);
                break;
            case ("10d"): case "10":case "10n": //rain cond
                Drawable icon_rain = getContext().getDrawable(R.drawable.icon_rain);
                icon_rain.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_rain, null, null, null);
                break;
            case ("11d"): case "11":case "11n"://lightning cond
                Drawable icon_thunder = getContext().getDrawable(R.drawable.icon_thunder);
                icon_thunder.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_thunder, null, null, null);
            case ("13d"): case "13":case "13n": //snow cond
                Drawable icon_snowy = getContext().getDrawable(R.drawable.icon_snowy);
                icon_snowy.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_snowy, null, null, null);
                break;
            case ("50d"): case "50":case "50n": //mist cond
                Drawable icon_mist = getContext().getDrawable(R.drawable.icon_mist);
                icon_mist.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_mist, null, null, null);
                break;
            default:
                Drawable icon_unknown = getContext().getDrawable(R.drawable.icon_unknown);
                icon_unknown.setBounds(0, 0, 150, 150);
                dayTempTextView.setCompoundDrawables(icon_unknown, null, null, null);
                break;
        }
    }

}


