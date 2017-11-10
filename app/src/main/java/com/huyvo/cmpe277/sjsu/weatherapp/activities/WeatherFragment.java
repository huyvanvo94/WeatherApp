package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Formatter;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.List;

public class WeatherFragment extends Fragment {
    public static final String TAG = "WeatherFragment";

    private View v;
    private WeatherModel today;
    private List<WeatherModel> mForecastList;
    private List<WeatherModel> mThreeHours;
    private String mTimeZoneId;
    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(List<WeatherModel> forcastList, WeatherModel today, List<WeatherModel> threehours){
        WeatherFragment fragment = new WeatherFragment();
        fragment.mForecastList = forcastList;
        fragment.today = today;
        fragment.mThreeHours = threehours;
        fragment.mTimeZoneId = today.timeZoneId;
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
        setForecastView(mForecastList);
        setThreeHoursView(mThreeHours);
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

    // set here
    public void setThreeHoursView(final List<WeatherModel> threeHours){
        if(v == null || threeHours == null){
            return;
        }
        RecyclerView rvThreeHours = (RecyclerView) v.findViewById(R.id.threehours_recycler_view);
        ThreeHoursViewAdapter threeHoursViewAdapter = new ThreeHoursViewAdapter(getContext(), mThreeHours, mTimeZoneId);
        rvThreeHours.setAdapter(threeHoursViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvThreeHours.setLayoutManager(layoutManager);
        rvThreeHours.setHasFixedSize(true);
        rvThreeHours.scrollToPosition(0);

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
 
    public void setTodayView(final WeatherModel weatherModel){
        if(weatherModel == null || v == null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Address address = WeatherApp.getAddressHere();
                if(address != null){
                    if(weatherModel.isMyLocation(address)) {
                        final TextView geoLocation = (TextView) v.findViewById(R.id.text_view_geo_location);
                        geoLocation.post(new Runnable() {
                            @Override
                            public void run() {
                                geoLocation.setVisibility(View.VISIBLE);
                                geoLocation.setText(R.string.current_location);
                            }
                        });
                    }
                }
            }
        }).start();

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

    public void setForecastView(List<WeatherModel> forecastList){
        Logger.d(TAG, "setForecastView " + String.valueOf(v==null));

        if(forecastList == null || v == null){
            return;
        }

        if(forecastList.size() != 5) {
            if (DateHelper.numberOfDayFromToday(forecastList.get(0).dt, mTimeZoneId) < 1) {
                forecastList.remove(0);
            } else {
                forecastList.remove(forecastList.size() - 1);
            }
        }
        ForecastViewAdapter mForecastViewAdapter = new ForecastViewAdapter(getContext(), forecastList, mTimeZoneId);
        ListView forecastListView = (ListView) v.findViewById(R.id.forecast_list);
        forecastListView.setAdapter(mForecastViewAdapter);

    }

}
