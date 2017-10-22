package com.huyvo.cmpe277.sjsu.weatherapp.model;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class LatLngModel implements BaseModel{
    public double lat;
    public double lng;

    public LatLngModel(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public String toString(){
        return "lat="+lat+"&lon="+lng;
    }
}
