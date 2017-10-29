package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        //setForecastView(mFiveDaysForecastList);
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

        Logger.d(TAG, weatherModel.city);

        Formatter formatter = new Formatter();
        TextView cityNameTextView = (TextView) v.findViewById(R.id.textview_city_name);
        cityNameTextView.setText(weatherModel.city);

        TextView dateTextView = (TextView) v.findViewById(R.id.text_view_date);
        dateTextView.setText(formatter.formatDate(weatherModel));


        TextView todayTemp = (TextView) v.findViewById(R.id.text_view_today_temp);
        todayTemp.setText(formatter.formatTemperature(weatherModel.temp));
    }

    public void setForecastView(List<WeatherModel> fiveDaysForecastList){
        Logger.d(TAG, "setForecastView" + String.valueOf(v==null));

        if(fiveDaysForecastList == null || v == null){
            return;
        }
        /*
        LinearLayout weatherLayout = (LinearLayout) v.findViewById(R.id.weather_layout);
        ListView forecastView = (ListView) v.findViewById(R.id.five_day_forecast_list);
        TextView cityNameTextView = (TextView) v.findViewById(R.id.textview_city_name);
        TextView currentDateTextView = (TextView) v.findViewById(R.id.textview_date);
        TextView currentTempTextView = (TextView) v.findViewById(R.id.textview_temp);
        TextView humidityTextView = (TextView) v.findViewById(R.id.textview_humidity);
        TextView pressureTextView = (TextView) v.findViewById(R.id.textview_pressure);
        TextView windSpeedTextView = (TextView) v.findViewById(R.id.textview_windspeed);*/

        /**
        if(weatherModel.city != null){
            cityNameTextView.setText(weatherModel.city);
            currentDateTextView.setText(weatherModel.getDayOfTheWeek());

            humidityTextView.setText(weatherModel.humidity + " %");
            Drawable humidityIcon = getContext().getDrawable(R.drawable.icon_humidity);
            humidityIcon.setBounds(0,0,100,100);
            humidityTextView.setCompoundDrawables(null, humidityIcon, null, null);

            pressureTextView.setText(weatherModel.pressure + " hPa");
            Drawable pressureIcon = getContext().getDrawable(R.drawable.icon_pressure);
            pressureIcon.setBounds(0,0, 100, 100);
            pressureTextView.setCompoundDrawables(null, pressureIcon, null, null);

            windSpeedTextView.setText(String.valueOf(weatherModel.windSpeed) + " mph");
            Drawable windSpeedIcon = getContext().getDrawable(R.drawable.icon_wind);
            windSpeedIcon.setBounds(0,0,100,100);
            windSpeedTextView.setCompoundDrawables(null, windSpeedIcon, null, null);

            if(weatherModel.temp_day != 0.0){
                currentTempTextView.setText(String.valueOf((int)weatherModel.temp_day) + '\u00B0' + " F");
            }else{
                currentTempTextView.setText(String.valueOf((int)weatherModel.temp) + '\u00B0' + " F");
            }


        }*/


    }

}
