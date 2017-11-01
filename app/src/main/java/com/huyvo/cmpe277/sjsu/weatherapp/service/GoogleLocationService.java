package com.huyvo.cmpe277.sjsu.weatherapp.service;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;

/**
 * Created by Huy Vo on 10/30/17.
 */

public class GoogleLocationService implements ConnectionCallbacks,
        OnConnectionFailedListener, OnRequestPermissionsResultCallback, LocationListener{

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    public GoogleLocationService() {




    }

    private boolean checkPermission(){
        return  (ActivityCompat.checkSelfPermission(WeatherApp.getInstance().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(WeatherApp.getInstance().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    public Location getLastLocation(){
        return mLastLocation;
    }

    public void connect(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        mGoogleApiClient.disconnect();
    }

    public void fetchLocation(){
        if(checkPermission()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("OK", String.valueOf(checkPermission()));


        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }
    LocationRequest locationRequest;

    FusedLocationProviderApi usedLocationProviderApi;
    private void getLocation(){

    }

    public void fetch_test() {
        if (checkPermission()){
            mGoogleApiClient = new GoogleApiClient.Builder(WeatherApp.getInstance().getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1);
            locationRequest.setFastestInterval(1);

            Location location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);



            Log.d("OK", String.valueOf(location==null));

        }
    }

    public boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(WeatherApp.getInstance().getApplicationContext());

        return (resultCode != ConnectionResult.SUCCESS);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("OK", location.toString());


    }
}
