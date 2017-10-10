package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

public class WeatherFragment extends Fragment {
    private View v;

    private static int count = 0;
    private WeatherModel weatherModel;

    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(WeatherModel model){
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.weatherModel = model;
        return weatherFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        TextView textView = (TextView) v.findViewById(R.id.text);
        textView.setText("OK");
        count++;

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface WeatherFragmentEventListener{
        void onItemClicked(WeatherModel weatherModel);
    }

}
