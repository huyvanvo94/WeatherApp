package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.BaseActivityWithFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.model.CityModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;

public class CityListViewActivity extends BaseActivityWithFragment implements View.OnClickListener{
    public final static String TAG = CityListViewActivity.class.getSimpleName();

    private CityViewAdapter mAdapter;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_view);

        FloatingActionButton mFabAddCity = (FloatingActionButton) findViewById(R.id.fab_add_city);
        mFabAddCity.setOnClickListener(this);

        ArrayList<CityModel> cityModels = new ArrayList<>();
        mAdapter = new CityViewAdapter(getApplicationContext(), R.layout.item_city_view, cityModels);
        ListView listView = (ListView) findViewById(R.id.list_city_view);

        listView.setAdapter(mAdapter);
    }


    private void onCitySearch(){

        try{
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (GooglePlayServicesRepairableException e){

        }catch (GooglePlayServicesNotAvailableException e){

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Logger.d(TAG, "Place: " + place.getLatLng());

                final double lat = place.getLatLng().latitude;
                final double lng = place.getLatLng().longitude;

                CityModel cityModel = new CityModel();
                cityModel.cityName = place.getAddress().toString();
                mAdapter.add(cityModel);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Logger.d(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_city:
                onCitySearch();
                break;

            default:
                break;
        }
    }
}
