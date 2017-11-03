package com.huyvo.cmpe277.sjsu.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Huy Vo on 10/27/17.
 */

public class LatLngModel implements Parcelable{

    public double lat;
    public double lng;

    public String getWeatherLocation(){
        return "lat="+lat+"&lon="+lng;
    }

    public String getTimeLocation(){
        return lat+","+lng;
    }

    protected LatLngModel(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LatLngModel> CREATOR = new Creator<LatLngModel>() {
        @Override
        public LatLngModel createFromParcel(Parcel in) {
            return new LatLngModel(in);
        }

        @Override
        public LatLngModel[] newArray(int size) {
            return new LatLngModel[size];
        }
    };
}
