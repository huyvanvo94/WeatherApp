package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class CityListViewActivity extends BaseActivityWithFragment {
    public final static String TAG = CityListViewActivity.class.getSimpleName();

    private CityViewAdapter mListAdapter;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_city_list_view);
        
        ArrayList<CityModel> cityModels = new ArrayList<>();
        mListAdapter = new CityViewAdapter(getApplicationContext(), R.layout.item_city_view, cityModels);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mListAdapter);

        Button b = (Button) findViewById(R.id.tester);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCitySearch();
            }
        });

    }


    private void startCitySearch(){

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

                mListAdapter.add(new CityModel(place.getAddress().toString(), "A", "B", "C"));

                // test

                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;

                /**
                Intent intent = new Intent();
                intent.putExtra("latlng", "lat="+lat+"&lon="+lng);
                setResult(1, intent);
                finish(); // finish activity */

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Logger.d(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
