package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Formatter;

import java.util.List;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class CityViewAdapter extends ArrayAdapter<WeatherModel>{


    public CityViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<WeatherModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Formatter formatter = new Formatter();

        WeatherModel model = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city_view, parent, false);
        }

        if(model.city != null) {
            LinearLayout ListItemLayout = (LinearLayout) convertView.findViewById(R.id.list_item);
            TextView cityNameTextView = (TextView) convertView.findViewById(R.id.city_name);
            TextView currentTempTextView = (TextView) convertView.findViewById(R.id.current_temp);
            TextView localTimeTextView = (TextView) convertView.findViewById(R.id.local_time);

            int[] backgroundColors = getContext().getResources().getIntArray(R.array.backgroundcolors);

            int index = WeatherApp.getLatLngList().indexOf(model.getKey());
            int itemColor = backgroundColors[index % 9 ];
            ListItemLayout.setBackgroundColor(itemColor);

            cityNameTextView.setText(model.city);
            currentTempTextView.setText(formatter.formatTemperature(model.temp));

            /**
             * Set Icon by Main value
             */
            setIcon(model, cityNameTextView);
            if(model.timeZoneId != null){
                localTimeTextView.setText(model.getLocalTime().replaceFirst("^0*", ""));
            }

        }
        return convertView;
    }

    @Override
    public void add(WeatherModel model) {
        super.add(model);
        notifyDataSetChanged();
    }

    private void setIcon(WeatherModel model, TextView cityNameTextView){

        switch(model.icon) {
            case ("01d"): case "01": //sunny cond
                Drawable icon_sunny = getContext().getDrawable(R.drawable.icon_sunny);
                icon_sunny.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_sunny, null);
                break;
            case ("02d"): case "02": //cloudy cond
                Drawable icon_partly_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_partly_cloudy.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_partly_cloudy, null);
                break;
            case ("03d"): case "03": //cloudy cond
                Drawable icon_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_cloudy.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_cloudy, null);
                break;
            case ("04d"): case "04": //cloudy cond
                Drawable icon_broken_clouds = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_broken_clouds.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_broken_clouds, null);
                break;
            case ("09d"): case "09": //shower cond
                Drawable icon_shower = getContext().getDrawable(R.drawable.icon_rain);
                icon_shower.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_shower, null);
                break;
            case ("10d"): case "10": //rain cond
                Drawable icon_rain = getContext().getDrawable(R.drawable.icon_rain);
                icon_rain.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_rain, null);
                break;
            case ("11d"): case "11": //lightning cond
                Drawable icon_thunder = getContext().getDrawable(R.drawable.icon_thunder);
                icon_thunder.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_thunder, null);
            case ("13d"): case "13": //snow cond
                Drawable icon_snowy = getContext().getDrawable(R.drawable.icon_snowy);
                icon_snowy.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_snowy, null);
                break;
            case ("50d"): case "50": //mist cond
                Drawable icon_mist = getContext().getDrawable(R.drawable.icon_mist);
                icon_mist.setBounds(0, 0, 400, 400);
                cityNameTextView.setCompoundDrawables(null, null, icon_mist, null);
                break;
        }
    }


}