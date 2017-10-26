package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.huyvo.cmpe277.sjsu.weatherapp.activities.BaseActivityWithFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.CityListViewActivity;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherPageAdapter;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.service.DataService;
import com.huyvo.cmpe277.sjsu.weatherapp.service.FutureTaskListener;
import com.huyvo.cmpe277.sjsu.weatherapp.service.OpenWeatherDataService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends BaseActivityWithFragment implements ViewPager.OnPageChangeListener{

    public final static String TAG = "MainActivity";

    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d(TAG, "onCreate");

        initUI();

        Intent i = getIntent();
        mPosition = i.getIntExtra("position", -1);
        Logger.d(TAG, "mPosition= "+mPosition);



        load(WeatherApp.getLatLngList());
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
            new Thread(new LoadManyWeatherForecastRunnable(locations)).start();
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

    public static final int ADD         = 1;
    public static final int ADD_EMPTY   = 2;
    public static final int ADD_FORCAST = 3;
    public static final int ADD_MANY = 4;
    class LoadEmptyWeatherRunnable implements Runnable{

        private int mLength;

        public LoadEmptyWeatherRunnable(int length){
            mLength = length;
        }
        @Override
        public void run() {

            for(int j = 0; j < mLength; j++){
                Message message = mHandler.obtainMessage();
                message.what = ADD_EMPTY;
                mHandler.sendMessage(message);
            }


            if(mPosition != -1){
                new Thread(new LoadWeatherForecastRunnable(mPosition)).start();
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

    class Test_LoadMany implements Runnable, Postable{

        private List<String> mLocations;

        public Test_LoadMany(){
            mLocations = WeatherApp.getLatLngList();
        }
        @Override
        public void postMessage() {
            Message msg = mHandler.obtainMessage();

            msg.what = ADD_FORCAST;
            mHandler.sendMessage(msg);
        }

        @Override
        public void run() {

        }
        private void fetchForecast(String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {
                    if(result != null){

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
    /**
     * Loads only one weather at a time
     */
    class LoadWeatherForecastRunnable implements Runnable, Postable{
        private String mLocation;
        private ArrayList<WeatherModel> mResults;

        public LoadWeatherForecastRunnable(int position){
            mLocation = WeatherApp.getLatLngList().get(position);
        }

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
                    if(result != null){
                        mResults = result;
                        postMessage();


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

        @Override
        public void postMessage() {
            Message message = mHandler.obtainMessage();
            message.what = ADD_FORCAST;
            message.obj = mResults;
            mHandler.sendMessage(message);
        }
    }

    /**
     * A class to load weather in order. If it does matter.
     */
    class LoadManyWeatherForecastRunnable implements Runnable{

        private Queue<String> mLocationsQueue;
        private List<WeatherFragment> mFragments;

        public LoadManyWeatherForecastRunnable(List<String> locations){
            Logger.d(TAG, "size"+locations.size());

            mFragments = new ArrayList<>();
            mLocationsQueue = new LinkedList<>(locations);

        }

        @Override
        public void run() {
            fetchForecasts();
        }

        private void postMessage(){
            Message msg = mHandler.obtainMessage();
            msg.obj = mFragments;
            msg.what = ADD_MANY;
            mHandler.sendMessage(msg);
        }

        private void fetchForecasts(){
            fetchForecast(mLocationsQueue.remove());
        }
        private void fetchForecast(String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {
                    if(result != null){
                        WeatherFragment fragment = WeatherFragment.newInstance(result);
                        mFragments.add(fragment);

                        if(mLocationsQueue.size() == 0){
                            postMessage();
                        }else{
                            fetchForecast(mLocationsQueue.remove());
                        }

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
        public void handleMessage(Message msg) {

            switch (msg.what){
                case ADD:
                    WeatherFragment fragment = (WeatherFragment) msg.obj;
                    ViewPager aPager = (ViewPager) findViewById(R.id.city_viewpager);
                    WeatherPageAdapter anAdapter = (WeatherPageAdapter) aPager.getAdapter();
                    anAdapter.add(fragment);
                    int size = anAdapter.getCount();
                    Logger.d(TAG, "adapter size"+size);
                    if(mPosition < size){
                        setCurrentItem(mPosition);
                    }

                    break;
                case ADD_EMPTY:
                    /**
                    ViewPager aPager = (ViewPager) findViewById(R.id.city_viewpager);
                    WeatherPageAdapter anAdapter = (WeatherPageAdapter) aPager.getAdapter();
                    anAdapter.add(new WeatherFragment());*/
                    break;

                case ADD_FORCAST:
                    List<WeatherModel> weatherModels = (List) msg.obj;
                    int aLen = addToAdapter(WeatherFragment.newInstance(weatherModels));
                    if(mPosition < aLen){
                        setCurrentItem(mPosition);
                    }

                    break;
                case ADD_MANY:
                    List<WeatherFragment> fragments = (List) msg.obj;

                    WeatherApp.getLatLngList();

                    for(WeatherFragment f: fragments){
                        addToAdapter(f);
                    }

                    setCurrentItem(mPosition);

                default:
                    break;
            }

        }
    };

    public int addToAdapter(WeatherFragment fragment){
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();
        wpa.add(fragment);
        return wpa.getCount();
    }
}