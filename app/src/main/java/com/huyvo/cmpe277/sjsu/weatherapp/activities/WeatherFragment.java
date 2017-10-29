package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
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


//    to do:
// fit min and max temp in layout
//
//
//
//

    public void setForecastView(List<WeatherModel> fiveDaysForecastList){
        Logger.d(TAG, "setForecastView" + String.valueOf(v==null));

        if(fiveDaysForecastList == null || v == null){
            return;
        }
        LinearLayout weatherLayout = (LinearLayout) v.findViewById(R.id.weather_layout);
        ListView forecastView = (ListView) v.findViewById(R.id.five_day_forecast_list);
        TextView cityNameTextView = (TextView) v.findViewById(R.id.textview_city_name);
        TextView currentDateTextView = (TextView) v.findViewById(R.id.textview_date);
        TextView currentTempTextView = (TextView) v.findViewById(R.id.textview_temp);
        TextView humidityTextView = (TextView) v.findViewById(R.id.textview_humidity);
        TextView pressureTextView = (TextView) v.findViewById(R.id.textview_pressure);
        TextView windSpeedTextView = (TextView) v.findViewById(R.id.textview_windspeed);

        WeatherModel weatherModel = fiveDaysForecastList.get(0);

        int[] backgroundColors = getContext().getResources().getIntArray(R.array.backgroundcolors);
        int itemColor = backgroundColors[weatherModel.colorIndex];
        weatherLayout.setBackgroundColor(itemColor);

        if(weatherModel.city != null){
            cityNameTextView.setText(weatherModel.city);
            currentDateTextView.setText(weatherModel.getDayOfTheWeek());

            humidityTextView.setText(String.valueOf(weatherModel.humidity) + " %");
            Drawable humidityIcon = getContext().getDrawable(R.drawable.icon_humidity);
            humidityIcon.setBounds(0,0,100,100);
            humidityTextView.setCompoundDrawables(null, humidityIcon, null, null);

            pressureTextView.setText(String.valueOf(weatherModel.pressure) + " hPa");
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


        }


    }

    public interface WeatherEventListener{
        void update();
    }

}
