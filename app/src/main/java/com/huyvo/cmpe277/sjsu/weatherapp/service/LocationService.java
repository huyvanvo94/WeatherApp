package com.huyvo.cmpe277.sjsu.weatherapp.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Huy Vo on 10/31/17.
 */

public class LocationService implements LocationListener {

    private Location mLastKnownLocation;
    private LocationManager mLocationManager;
    private String mProvider;

    public LocationService() {
        if(checkPermission()){
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);


        mLocationManager =
                (LocationManager)
                        WeatherApp.getInstance().getApplicationContext()
                                .getSystemService(LOCATION_SERVICE);

        mProvider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates( mProvider , 5000, 2.0f, this);

        mLastKnownLocation = mLocationManager.getLastKnownLocation(mProvider);

    }

    public boolean checkPermission(){
        return  (ActivityCompat.checkSelfPermission(WeatherApp.getInstance().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(WeatherApp.getInstance().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;

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

    public Address getAddress(Location location){
        if(location == null){
            return null;
        }

        return getAddress(location.getLatitude(), location.getLongitude());
    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(WeatherApp.getInstance().getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Location getLastKnownLocation() {
        if(checkPermission()){
            return null;
        }
        mLocationManager = (LocationManager) WeatherApp.getInstance().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
