package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Formatter;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.List;

public class WeatherFragment extends Fragment {
    public static final String TAG = "WeatherFragment";

    private View v;
    private WeatherModel today;
    private List<WeatherModel> mFiveDaysForecastList;
    private ForecastViewAdapter mForecastViewAdapter;
    
    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(List<WeatherModel> fiveDaysForecastList, WeatherModel today){
        WeatherFragment fragment = new WeatherFragment();
        fragment.mFiveDaysForecastList = fiveDaysForecastList;
        fragment.today = today;
        return fragment;
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
        setBackgroundColor(today);
        setTodayView(today);
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

    private void setBackgroundColor(WeatherModel weatherModel){
        if(weatherModel == null || v == null){
            return;
        }
        RelativeLayout weatherLayout = (RelativeLayout) v.findViewById(R.id.weather_layout);
        int[] backgroundColors = getContext().getResources().getIntArray(R.array.backgroundcolors);
        int index = WeatherApp.getLatLngList().indexOf(weatherModel.getKey());
        int itemColor = backgroundColors[index % 9 ];
        weatherLayout.setBackgroundColor(itemColor);

    }
 
    public void setTodayView(WeatherModel weatherModel){
        if(weatherModel == null || v == null){
            return;
        }

        Address address = WeatherApp.getAddressHere();
        if(weatherModel.isMyLocation(address)){
            Logger.d(TAG, "true");
            TextView geoLocation = (TextView) v.findViewById(R.id.text_view_geo_location);
            geoLocation.setVisibility(v.VISIBLE);
            geoLocation.setText("You are here");
        }

        Formatter formatter = new Formatter();

        TextView cityNameTextView = (TextView) v.findViewById(R.id.textview_city_name);
        cityNameTextView.setText(weatherModel.city);

        TextView dateTextView = (TextView) v.findViewById(R.id.text_view_date);
        dateTextView.setText(formatter.formatDate(weatherModel));


        TextView todayTemp = (TextView) v.findViewById(R.id.text_view_today_temp);
        todayTemp.setText(formatter.formatTemperature(weatherModel.temp));

        TextView todayCond = (TextView) v.findViewById(R.id.text_view_cond);
        todayCond.setText(weatherModel.main);

        TextView todayMinTemp = (TextView) v.findViewById(R.id.text_view_min_temp);
        todayMinTemp.setText(formatter.formatMinTemperature(weatherModel.temp_min));

        TextView todayMaxTemp = (TextView) v.findViewById(R.id.text_view_max_temp);
        todayMaxTemp.setText(formatter.formatMaxTemperature(weatherModel.temp_max));

        Drawable humidIcon = getContext().getDrawable(R.drawable.icon_humidity);
        humidIcon.setBounds(0,0, 75, 75);
        TextView todayHumidity = (TextView) v.findViewById(R.id.text_view_humidity);
        todayHumidity.setText(formatter.formatHumidity(weatherModel.humidity));
        todayHumidity.setCompoundDrawables(null, humidIcon, null, null);

        Drawable pressureIcon = getContext().getDrawable(R.drawable.icon_pressure);
        pressureIcon.setBounds(0,0, 75, 75);
        TextView todayPressure = (TextView) v.findViewById(R.id.text_view_pressure);
        todayPressure.setText(formatter.formatPressure(weatherModel.pressure));
        todayPressure.setCompoundDrawables(null, pressureIcon, null, null);

        Drawable windSpeedIcon = getContext().getDrawable(R.drawable.icon_wind);
        windSpeedIcon.setBounds(0,0, 75, 75);
        TextView todayWindSpeed = (TextView) v.findViewById(R.id.text_view_windspeed);
        todayWindSpeed.setText(formatter.formatWindSpeed(weatherModel.windSpeed));
        todayWindSpeed.setCompoundDrawables(null, windSpeedIcon, null, null);


    }

    public void setForecastView(List<WeatherModel> fiveDaysForecastList){
        Logger.d(TAG, "setForecastView" + String.valueOf(v==null));

        if(fiveDaysForecastList == null || v == null){
            return;
        }

        mForecastViewAdapter = new ForecastViewAdapter(getContext(), fiveDaysForecastList);
        ListView forecastListView = (ListView) v.findViewById(R.id.forecast_list);
        forecastListView.setAdapter(mForecastViewAdapter);



    }

}
