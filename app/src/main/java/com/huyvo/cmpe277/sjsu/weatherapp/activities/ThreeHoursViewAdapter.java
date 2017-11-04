package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
 * Created by Huy Vo on 11/2/17.
 */

public class ThreeHoursViewAdapter extends RecyclerView.Adapter<ThreeHoursViewAdapter.ViewHolder> {

    private List<WeatherModel> mWeatherModels;
    private Context mContext;
    private String mTimeZoneId;

     public class ViewHolder extends RecyclerView.ViewHolder{
      public TextView timeTextView;
      public TextView tempTextView;

      public ViewHolder(View itemView){
          super(itemView);

          timeTextView = (TextView) itemView.findViewById(R.id.text_view_time_three_hours);
          tempTextView = (TextView) itemView.findViewById(R.id.text_view_temp_three_hours);
      }
  }

  public ThreeHoursViewAdapter(Context context, List<WeatherModel> objects, String timeZoneId){
      mWeatherModels = objects;
      mContext = context;
      mTimeZoneId = timeZoneId;
  }

  private Context getContext(){
      return mContext;
  }

  @Override
    public ThreeHoursViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);

      View threeHoursView = inflater.inflate(R.layout.item_three_hours_view, parent, false);

      ViewHolder viewHolder = new ViewHolder(threeHoursView);
      return viewHolder;
  }

  @Override
    public void onBindViewHolder(ThreeHoursViewAdapter.ViewHolder viewHolder, int position){
        WeatherModel weatherModel = mWeatherModels.get(position);
        weatherModel.timeZoneId = mTimeZoneId;
      Formatter formatter = new Formatter();

        TextView timeTextView = viewHolder.timeTextView;
        timeTextView.setText(weatherModel.getFutureTime().replaceFirst("^0*", ""));
        TextView tempTextView = viewHolder.tempTextView;
        tempTextView.setText(formatter.formatTemperature(weatherModel.temp));
        setIcon(weatherModel, tempTextView);
  }

  @Override
    public int getItemCount(){
        return mWeatherModels.size();
  }

    private void setIcon(WeatherModel model, TextView dayTempTextView){

        switch(model.icon) {
            case ("01d"): case "01": //sunny cond
                Drawable icon_sunny = getContext().getDrawable(R.drawable.icon_sunny);
                icon_sunny.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_sunny, null, null);
                break;
            case ("01n"):
                Drawable icon_moon = getContext().getDrawable(R.drawable.icon_moon);
                icon_moon.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_moon, null, null);
            case ("02d"): case "02":case "02n": //cloudy cond
                Drawable icon_partly_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_partly_cloudy.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_partly_cloudy, null, null);
                break;
            case ("03d"): case "03":case "03n": //scattered clouds cond
                Drawable icon_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_cloudy.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_cloudy, null, null);
                break;
            case ("04d"): case "04": case "04n": //cloudy cond
                Drawable icon_broken_cloudy = getContext().getDrawable(R.drawable.icon_partly_cloudy);
                icon_broken_cloudy .setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null , icon_broken_cloudy, null, null);
                break;
            case ("09d"): case "09":case "09n"://shower cond
                Drawable icon_shower = getContext().getDrawable(R.drawable.icon_rain);
                icon_shower.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_shower, null, null);
                break;
            case ("10d"): case "10":case "10n": //rain cond
                Drawable icon_rain = getContext().getDrawable(R.drawable.icon_rain);
                icon_rain.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_rain, null, null);
                break;
            case ("11d"): case "11":case "11n": //lightning cond
                Drawable icon_thunder = getContext().getDrawable(R.drawable.icon_thunder);
                icon_thunder.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_thunder, null, null);
            case ("13d"): case "13":case "13n": //snow cond
                Drawable icon_snowy = getContext().getDrawable(R.drawable.icon_snowy);
                icon_snowy.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_snowy, null, null);
                break;
            case ("50d"): case "50":case "50n": //mist cond
                Drawable icon_mist = getContext().getDrawable(R.drawable.icon_mist);
                icon_mist.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_mist, null, null);
                break;
            default:
                Drawable icon_unknown = getContext().getDrawable(R.drawable.icon_unknown);
                icon_unknown.setBounds(0, 0, 135, 135);
                dayTempTextView.setCompoundDrawables(null, icon_unknown, null, null);
                break;
        }
    }


}
