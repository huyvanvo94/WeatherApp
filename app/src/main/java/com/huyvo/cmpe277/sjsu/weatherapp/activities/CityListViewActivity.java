package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.huyvo.cmpe277.sjsu.weatherapp.FetchWeatherIntentService;
import com.huyvo.cmpe277.sjsu.weatherapp.MainActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.model.CityModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.DateHelper;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonHelper;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityListViewActivity extends BaseActivityWithFragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    public final static String TAG = CityListViewActivity.class.getSimpleName();

    private CityViewAdapter mAdapter;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_view);

        Logger.d(TAG, "onCreate");

        FloatingActionButton mFabAddCity = (FloatingActionButton) findViewById(R.id.fab_add_city);
        ArrayList<CityModel> cityModels = new ArrayList<>();
        mAdapter = new CityViewAdapter(getApplicationContext(), R.layout.item_city_view, cityModels);
        ListView listView = (ListView) findViewById(R.id.list_city_view);
        listView.setAdapter(mAdapter);
        mFabAddCity.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        load(((WeatherApp) WeatherApp.getInstance()).getLatLngList());
    }

    private void load(List<String> locations){
        Logger.d(TAG, String.valueOf(locations.size()));
        if(!locations.isEmpty()) {
            new Thread(new CitiesLoadRunnable(locations)).start();

        }
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
        Logger.d(TAG, "onActivityResult");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                final double lat = place.getLatLng().latitude;
                final double lng = place.getLatLng().longitude;

                String location = "lat="+lat+"&lon="+lng;
                if(!((WeatherApp) WeatherApp.getInstance()).getLatLngList().contains(location)){

                    Intent intent = new Intent(this, FetchWeatherIntentService.class);
                    intent.putExtra(FetchWeatherIntentService.FETCH_WEATHER, location);
                    startService(intent);
                    ((WeatherApp) WeatherApp.getInstance()).getLatLngList().add(location);
                    new Thread(new CityLoadRunnable(location)).start();
                }
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

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
        Logger.d(TAG, "onItemClick " + position);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }

    private void appendCityModel(CityModel model){
        mAdapter.add(model);
    }


    class CityRemoveRunnable implements Runnable{

        @Override
        public void run() {

        }
    }

    class CitiesLoadRunnable implements Runnable{

        private List<String> mLocations;

        public CitiesLoadRunnable(List<String> locations){
            mLocations = locations;
        }
        @Override
        public void run() {
            for(String location: mLocations){
                fetchWeather(location);
            }
        }


        private void fetchWeather(final String location){
            String url = "http://api.openweathermap.org/data/2.5/weather?"+location+"&units=imperial&appid=b54f500d4a53fdfc96813a4ba9210417";
            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Logger.d(TAG, response.toString());

                                WeatherModel weatherModel = JsonParser.parseWeather(response);

                                fetchLocalTime(weatherModel.lat+","+weatherModel.lon);

                                CityModel model = new CityModel();
                                model.cityName = weatherModel.city;
                                model.latlng = location;

                                Message msg = mHandler.obtainMessage();
                                msg.what = ADD_CITY;
                                msg.obj = model;

                                mHandler.sendMessage(msg);

                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }));
        }

        private void fetchLocalTime(String location){

            String url = "https://maps.googleapis.com/maps/api/timezone/json?location="+location+"&timestamp="+DateHelper.getTimeStamp()+"&key=AIzaSyCLsOiLE8N82juTF3fljOupSenGI7Nb9Ro";
            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Logger.d(TAG, response.toString());

                                String timeZoneId = JsonHelper.getString(response, "timeZoneId");

                                //Logger.d(TAG, timeZoneId);

                                long unixTime = System.currentTimeMillis() / 1000L;
                                Logger.d(TAG, DateHelper.getLocalTime(unixTime , timeZoneId));
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }));
        }
    }

    /**
     * load current local time of that city, city name, and current temperature.
     */
    private class CityLoadRunnable implements Runnable{

        private String location;
        public CityLoadRunnable(String location){
            this.location = location;
        }
        @Override
        public void run() {
            Logger.d(TAG, "run");
            load(location);
        }

        private void load(String location){
            fetchWeather(location);
        }

        private void fetchLocalTime(String location){

            String url = "https://maps.googleapis.com/maps/api/timezone/json?location="+location+"&timestamp="+DateHelper.getTimeStamp()+"&key=AIzaSyCLsOiLE8N82juTF3fljOupSenGI7Nb9Ro";
            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Logger.d(TAG, response.toString());

                                String timeZoneId = JsonHelper.getString(response, "timeZoneId");

                                //Logger.d(TAG, timeZoneId);

                                long unixTime = System.currentTimeMillis() / 1000L;
                                Logger.d(TAG, DateHelper.getLocalTime(unixTime , timeZoneId));
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }));
        }

        private void fetchWeather(final String location){
            String url = "http://api.openweathermap.org/data/2.5/weather?"+location+"&units=imperial&appid=b54f500d4a53fdfc96813a4ba9210417";
            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                WeatherModel weatherModel = JsonParser.parseWeather(response);

                                fetchLocalTime(weatherModel.lat+","+weatherModel.lon);

                                CityModel model = new CityModel();
                                model.cityName = weatherModel.city;
                                model.latlng = location;

                                Message msg = mHandler.obtainMessage();
                                msg.what = ADD_CITY;
                                msg.obj = model;

                                mHandler.sendMessage(msg);

                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }));
        }
    }

    public final static int ADD_CITY    = 1;
    public final static int REMOVE_CITY = 2;

    Handler mHandler = new Handler(Looper.getMainLooper()){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case ADD_CITY:
                    CityModel model = (CityModel) msg.obj;
                    appendCityModel(model);
                    break;
                default:
                    break;
            }

        }
    };
}
