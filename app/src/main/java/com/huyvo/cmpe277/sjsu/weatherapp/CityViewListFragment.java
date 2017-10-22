package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.huyvo.cmpe277.sjsu.weatherapp.model.CityModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;

public class CityViewListFragment extends Fragment implements View.OnClickListener, PlaceSelectionListener{
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public final static String TAG = CityViewListFragment.class.getSimpleName();
    private CityViewAdapter mAdapter;
    private View v;

    public CityViewListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_city_view_list, container, false);

        FloatingActionButton mFabAddCity = (FloatingActionButton) v.findViewById(R.id.fab_add_city);
        mFabAddCity.setOnClickListener(this);

        mAdapter = new CityViewAdapter(getContext(), R.layout.item_city_view, new ArrayList<CityModel>());

        ListView listView = (ListView) v.findViewById(R.id.list_city_view);
        // Assign adapter to ListView
        listView.setAdapter(mAdapter);



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



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_city:
                onSearchCity();
                break;

            default:
                break;
        }
    }

    private void onSearchCity(){
        try{
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(getActivity());

            PlaceAutocomplete.getPlace(getContext(), intent);
            // startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){

        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Logger.d(TAG, place.toString());
    }

    @Override
    public void onError(Status status) {

    }
}
