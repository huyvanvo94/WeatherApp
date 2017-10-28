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
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.List;

public class WeatherFragment extends Fragment {
    public static final String TAG = "WeatherFragment";

    private View v;
    private WeatherModel weatherModel;
    private List<WeatherModel> mFiveDaysForecastList;
    
    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(List<WeatherModel> fiveDaysForecastList){
        WeatherFragment fragment = new WeatherFragment();
        fragment.mFiveDaysForecastList = fiveDaysForecastList;
        return fragment;
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
        Logger.d(TAG, "onCreateView");
        v = inflater.inflate(R.layout.fragment_weather, container, false);
        setForecastView(mFiveDaysForecastList);
        return v;
    }

    public View getView(){
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

    public List<WeatherModel> getFiveDaysForecastList(){
        return mFiveDaysForecastList;
    }
    public WeatherModel getWeatherModel(){
        return weatherModel;
    }

    public void setForecastView(List<WeatherModel> fiveDaysForecastList){
        Logger.d(TAG, "setForecastView" + String.valueOf(v==null));

        if(fiveDaysForecastList == null || v == null){
            return;
        }

        TextView textView = (TextView) v.findViewById(R.id.textview_city_name);

        WeatherModel weatherModel = fiveDaysForecastList.get(0);
        String city = weatherModel.city;
        if(city != null){
            textView.setText(city);
        }
    }

    public interface WeatherEventListener{
        void update();
    }

}
