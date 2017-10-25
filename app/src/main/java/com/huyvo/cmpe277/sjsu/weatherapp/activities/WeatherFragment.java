package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {
    public static final String TAG = "WeatherFragment";

    private View v;
    private WeatherModel weatherModel;
    private List<WeatherModel> mFiveDaysForecastList;
    
    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(WeatherModel weatherModel, ArrayList<WeatherModel> fiveDaysForecastList){
        return null;
    }
    public static WeatherFragment newInstance(WeatherModel model){
        Logger.d(TAG, model.city);
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
        v = inflater.inflate(R.layout.fragment_weather, container, false);
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

    public WeatherModel getWeatherModel(){
        return weatherModel;
    }

    public void setFiveDaysForecastView(List<WeatherModel> fiveDaysForecastList){

    }

    public void update(WeatherModel model){

    }

    public void setTodayView(WeatherModel model){

    }


    public interface WeatherEventListener{
        void update();
    }

}
