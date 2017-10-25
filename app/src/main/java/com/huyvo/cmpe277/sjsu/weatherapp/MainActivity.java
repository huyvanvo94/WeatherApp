package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.BaseActivityWithFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.CityListViewActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherPageAdapter;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivityWithFragment implements ViewPager.OnPageChangeListener{

    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d(TAG, "onCreate");

        //initUI();
        //load(WeatherApp.getLatLngList());

        loadTester();

    }

    public void loadTester(){

        if(true){
            String location = "lat=37.7652065&lon=-122.2416355";

            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {

                    for(WeatherModel weatherModel:result){
                        Logger.d(TAG, weatherModel.day +" ");
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

    public void setCurrentItem(int position){
        ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
        pager.setCurrentItem(position);
    }

    private void load(String location){
        Logger.d(TAG, "location= " + location);
        if (location != null){
            new Thread(new LoadWeatherForecastRunnable(location)).start();
        }
    }
    private void load(List<String> locations){

        if(!locations.isEmpty()){
            load(locations.get(0));
            new Thread(new LoadWeatherRunnable(locations)).start();
        }
    }

    private void initUI(){
        ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
        pager.addOnPageChangeListener(this);
        PagerAdapter adapter = new WeatherPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.weather:
                Intent i = new Intent(this, CityListViewActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }

        return true;
    }

    private void fetchWeather(String location){

        DataService dataService = new OpenWeatherDataService();
        dataService.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
            @Override
            public void onCompletion(WeatherModel result) {
                Logger.d(TAG, result.toString());
                ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
                ((WeatherPageAdapter)pager.getAdapter()).add(WeatherFragment.newInstance(result));
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    private void test_LatLngWeather(String location){
        requestWeather("http://api.openweathermap.org/w"+location+"&appid=b54f500d4a53fdfc96813a4ba9210417");
    }

    private void requestWeather(String url){
        ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response:%n %s", response.toString(4));

                            WeatherModel model = JsonParser.parseWeather(response);

                            ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
                            ((WeatherPageAdapter)pager.getAdapter()).add(new WeatherFragment());
                        }catch(JSONException e) {
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(TAG, "onPageSelected="+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        Logger.d(TAG, "onActivityResult");
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 1) {
            String location = i.getStringExtra("latlon");

            if(location != null){
                test_LatLngWeather(location);
            }

        }
    }


    private class WeatherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            fetchWeather();
            return null;
        }

        private void fetchWeather(){
            final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=alameda&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";

            requestWeather(URL);
        }

        private void requestWeather(String url){
            ((WeatherApp) WeatherApp.getInstance()).addToRequestQueue(new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("Response:%n %s", response.toString(4));

                                WeatherModel model = JsonParser.parseWeather(response);

                            }catch(JSONException e) {
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


    private class ForecastTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


    public static final int ADD = 1;

    class LoadEmptyWeatherRunnable implements Runnable{

        private int mLength;

        public LoadEmptyWeatherRunnable(int length){
            mLength = length;
        }
        @Override
        public void run() {

            for(int j = 0; j < mLength; j++){
                WeatherModel weatherModel = new WeatherModel();
                Message message = mHandler.obtainMessage();
                message.what = ADD;
                message.obj = weatherModel;
                mHandler.sendMessage(message);
            }

        }
    }

    class LoadWeatherRunnable implements Runnable{

        private List<String> mLocations;
        public LoadWeatherRunnable(List<String> locations){
            mLocations = locations;
        }
        @Override
        public void run() {

            for(String location: mLocations){
                fetchWeather(location);
            }

        }

        private void fetchWeather(String location){

            DataService dataService = new OpenWeatherDataService();
            dataService.getWeatherByLatLng(location, new FutureTaskListener<WeatherModel>() {
                @Override
                public void onCompletion(WeatherModel result) {
                    Logger.d(TAG, result.toString());

                    Message message = mHandler.obtainMessage();
                    message.what = ADD;
                    message.obj = result;

                    mHandler.sendMessage(message);
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

    class LoadWeatherForecastRunnable implements Runnable{

        private String mLocation;

        public LoadWeatherForecastRunnable(String location){
            mLocation = location;
        }
        @Override
        public void run() {
            fetchForecast(mLocation);
        }

        private void fetchForecast(String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {
                    for(int i = 0; i< result.size(); i++){
                        Logger.d(TAG, result.get(i).toString());
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

    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case ADD:
                    WeatherModel weatherModel = (WeatherModel) msg.obj;
                    ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
                    WeatherPageAdapter weatherPageAdapter = (WeatherPageAdapter) pager.getAdapter();
                    weatherPageAdapter.add(WeatherFragment.newInstance(weatherModel));
                    Intent i = getIntent();
                    int position = i.getIntExtra("position", -1);
                    if(position !=-1){
                        setCurrentItem(position);
                    }
                    break;
                default:
                    break;
            }

        }
    };
}