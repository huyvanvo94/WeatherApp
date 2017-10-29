package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.huyvo.cmpe277.sjsu.weatherapp.MainActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherForecastContainer;
import com.huyvo.cmpe277.sjsu.weatherapp.model.CityModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.GooglePlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.PlaceService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.intent.FetchForecastIntentService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.intent.RemoveWeatherIntentService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.ListViewMessages.ADD_CITY;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.ListViewMessages.REMOVE_CITY;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.ListViewMessages.UPDATE_CITY_TIME;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.ListViewMessages.UPDATE_WEATHER;

public class CityListViewActivity extends BaseActivityWithFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    public final static String TAG = CityListViewActivity.class.getSimpleName();
    private CityViewAdapter mAdapter;
    private final List<CityModel> mCityModels = new ArrayList<>();
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private ActionMode.Callback mCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_view);
        Logger.d(TAG, "onCreate");

        initUI();
        load(WeatherApp.getLatLngList());

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(new FetchLocalTimePeriodically(), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new FetchWeatherPeriodically(), 0, 3, TimeUnit.HOURS);
    }

    private void initUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cities");

        mCallback = new ActionModeCallback();

        FloatingActionButton mFabAddCity = (FloatingActionButton) findViewById(R.id.fab_add_city);
        mAdapter = new CityViewAdapter(getApplicationContext(), R.layout.item_city_view, mCityModels);
        ListView listView = (ListView) findViewById(R.id.list_city_view);
        listView.setAdapter(mAdapter);
        mFabAddCity.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
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
        }catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException ignored){
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Logger.d(TAG, place.toString());

                final double lat = place.getLatLng().latitude;
                final double lng = place.getLatLng().longitude;

                String location = "lat="+lat+"&lon="+lng;
                if(!WeatherApp.getLatLngList().contains(location)){
                    WeatherApp.getLatLngList().add(location);

                    Intent intent = new Intent(this, FetchForecastIntentService.class);
                    intent.putExtra(FetchForecastIntentService.FETCH_WEATHER, location);
                    startService(intent);

                    // update current view inside of waiting
                    // for forecast fetching which will take relatively long
                    new Thread(new CityLoadRunnable(location)).start();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Logger.d(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {}
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
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
        Logger.d(TAG, "onItemClick " + position);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Logger.d(TAG, "onItemLongClick");
        startActionMode(mCallback);
        return false;
    }

    class CitiesLoadRunnable implements Runnable{

        private List<String> mLocations;

        public CitiesLoadRunnable(List<String> locations){
            mLocations = locations;
        }
        @Override
        public void run() {
            for(String location: mLocations){
                WeatherForecastContainer forecastContainer = WeatherForecastContainer.getInstance();
                WeatherModel weatherModel = forecastContainer.getWeatherModels(location).get(0);

                CityModel cityModel = CityModel.makeFrom(weatherModel);

                if(cityModel != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = cityModel;
                    msg.what = ADD_CITY;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    class FetchWeatherPeriodically implements Runnable{
        @Override
        public void run(){
            synchronized (mCityModels){
                for (int i = 0; i < mCityModels.size(); i++){
                    fetchWeather(mCityModels.get(i));
                }
            }

        }

        private void fetchWeather(final CityModel cityModel){
            DataService dataService = new OpenWeatherDataService();
            String location = cityModel.location;

            dataService.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
                @Override
                public void onCompletion(WeatherModel result) {
                    try {
                        cityModel.currentTemp = String.valueOf(result.temp);
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPDATE_WEATHER;
                        mHandler.sendMessage(msg);

                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onProgress(float progress) {

                }
            });
        }
    }


    class FetchLocalTimePeriodically implements Runnable{
        @Override
        public void run() {
            fetch();
        }

        private void fetch(){
            Message msg = mHandler.obtainMessage();
            msg.what = UPDATE_CITY_TIME;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * load current local time of that city, city name, and current temperature.
     */
    private class CityLoadRunnable implements Runnable{

        private String mLocation;
        public CityLoadRunnable(String location){
            mLocation = location;
        }
        @Override
        public void run() {
            Logger.d(TAG, "run");
            load(mLocation);
        }

        private void load(String location){
            fetchWeather(location);
        }

        private void fetchLocalTime(final CityModel model){
            PlaceService placeService = new GooglePlaceService();
            placeService.getLocalTime(model.latlng, new FutureTaskListener<LocalTimeModel>() {
                @Override
                public void onCompletion(LocalTimeModel result) {
                    Logger.d(TAG, result.timeZoneId);
                    model.timeZoneId = result.timeZoneId;
                    post(model);
                }

                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onProgress(float progress) {

                }
            });
        }

        private void fetchWeather(final String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
                @Override
                public void onCompletion(WeatherModel result) {
                    CityModel model = CityModel.makeFrom(result);
                    fetchLocalTime( model);
                }

                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onProgress(float progress) {

                }
            });
        }

        private void post(CityModel cityModel){
            Message msg = mHandler.obtainMessage();
            msg.what = ADD_CITY;
            msg.obj = cityModel;

            mHandler.sendMessage(msg);
        }
    }


    Handler mHandler = new Handler(Looper.getMainLooper()){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case ADD_CITY:
                    CityModel model = (CityModel) msg.obj;
                    mCityModels.add(model);
                    mAdapter.notifyDataSetChanged();
                    break;

                case REMOVE_CITY:
                    int index = (int) msg.obj;
                    removeFromSystem(index);
                    break;
                case UPDATE_CITY_TIME: case UPDATE_WEATHER:
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

        }
    };

    private void removeFromSystem(int index){
        CityModel cityModel = mCityModels.get(index);
        if(cityModel != null){

            Intent i = new Intent(this, RemoveWeatherIntentService.class);
            i.putExtra(RemoveWeatherIntentService.REMOVE_LOCATION, cityModel.location);
            startService(i);

            mCityModels.remove(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    class ActionModeCallback implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.content_menu_city_view, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}