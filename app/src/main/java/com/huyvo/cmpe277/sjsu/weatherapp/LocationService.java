package com.huyvo.cmpe277.sjsu.weatherapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Huy Vo on 10/29/17.
 */

public class LocationManager implements LocationListener {

    private static LocationManager mLocationManager;

    private LocationManager(){

    }



    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
