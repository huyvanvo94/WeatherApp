package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.model.CityModel;

import java.util.List;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class CityViewAdapter extends ArrayAdapter<CityModel>{

    public CityViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CityModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CityModel cityModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city_view, parent, false);
        }

        if(cityModel.cityName != null) {
            TextView cityNameTextView = (TextView) convertView.findViewById(R.id.city_name);
            cityNameTextView.setText(cityModel.cityName);
        }
        return convertView;
    }

    @Override
    public void add(CityModel cityModel) {
        super.add(cityModel);
        notifyDataSetChanged();
    }


}
