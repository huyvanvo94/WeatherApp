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

import static com.huyvo.cmpe277.sjsu.weatherapp.MainActivity.Messages.ADD_MANY;
import static com.huyvo.cmpe277.sjsu.weatherapp.MainActivity.Messages.ADD_ONCE_AT_A_TIME;

public class MainActivity extends BaseActivityWithFragment implements ViewPager.OnPageChangeListener{

    public final static String TAG = "MainActivity";

    private PostFinishedListener postFinishedListener;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d(TAG, "onCreate");

        initUI();

        Intent i = getIntent();
        mPosition = i.getIntExtra("position", -1);


        List<String> mLocations = WeatherApp.getLatLngList();
        if(!mLocations.isEmpty()){
            postFinishedListener = new PostForcastRunnable(mLocations);
            new Thread((Runnable) postFinishedListener).start();

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
        Logger.d(TAG, "onPageScrolled="+position);

        WeatherForecastContainer container = WeatherForecastContainer.getInstance();
        boolean shouldRequestFetchWeather = container.shouldRequestFetchWeather(WeatherApp.getLatLngList().get(position));
        if(shouldRequestFetchWeather){
            mPosition = position;
            new Thread(new UpdateWeatherForecastRunnable(WeatherApp.getLatLngList().get(mPosition))).start();
        }
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(TAG, "onPageSelected="+position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public final class Messages{
        public static final int ADD         = 1;
        public static final int ADD_EMPTY   = 2;
        public static final int ADD_ONCE_AT_A_TIME = 3;
        public static final int ADD_MANY = 4;
        public static final int UPDATE = 5;
    }

    class PostForcastRunnable implements Runnable, PostFinishedListener, Postable{
        private Queue<String> mLocations;
        public PostForcastRunnable(List<String> locations){

            mLocations = new LinkedList<>(locations);
        }

        @Override
        public void done() {
            postMessage();
        }

        @Override
        public void run() {
            postMessage();
        }

        @Override
        public void postMessage() {
            if(!mLocations.isEmpty()) {
                String location = mLocations.remove();
                WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                List<WeatherModel> weatherModels = weatherForecastContainer.getWeatherModels(location);

                Message message = mHandler.obtainMessage();
                message.obj = weatherModels;
                message.what = -1;
                mHandler.sendMessage(message);

            }
        }
    }



    class LoadForcastRunnable implements Runnable{

        private String mLocation;
        public LoadForcastRunnable(int position){
            mLocation = WeatherApp.getLatLngList().get(position);
        }
        @Override
        public void run() {

            fetchWeather(mLocation);

        }

        private void fetchWeather(String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {
                    Message message = mHandler.obtainMessage();
                    message.what = Messages.ADD;
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

    /**
     * A class that loads weather once at a time.
     * Handler will update that it has done posting
     */
    class LoadOnceAtATime implements Runnable, Postable, PostFinishedListener{

        private Queue<String> mLocations;

        public LoadOnceAtATime(List<String> aList){
            mLocations = new LinkedList<>(aList);
        }
        @Override
        public void postMessage() {

        }

        @Override
        public void run() {
            fetchForecast(mLocations.remove());
        }
        private void fetchForecast(String location){
            DataService dataService = new OpenWeatherDataService();
            dataService.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(ArrayList<WeatherModel> result) {
                    if(result != null){
                        Message msg = mHandler.obtainMessage();
                        msg.obj = result;
                        msg.what = ADD_ONCE_AT_A_TIME;
                        mHandler.sendMessage(msg);
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
        public void done() {
            if(!mLocations.isEmpty()) {
                fetchForecast(mLocations.remove());
            }
        }
    }

    interface PostFinishedListener{
        void done();
    }

    class UpdateWeatherForecastRunnable implements Runnable{

        private String mLocation;

        public UpdateWeatherForecastRunnable(String location){
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
                        WeatherForecastContainer container = WeatherForecastContainer.getInstance();
                        container.put(mLocation, result);

                        Message msg = mHandler.obtainMessage();
                        msg.obj = result;
                        msg.what = Messages.UPDATE;
                        mHandler.sendMessage(msg);
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
            message.what = ADD_ONCE_AT_A_TIME;
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
                case Messages.ADD:
                    List<WeatherModel> models = (List) msg.obj;
                    replaceFragmentInAdapter(mPosition, models);
                    setCurrentItem(mPosition);
                    break;
                case Messages.ADD_EMPTY:

                    int len = addToAdapter(new WeatherFragment());

                    if(mPosition == len-1){
                        new Thread(new LoadForcastRunnable(mPosition)).start();
                    }

                    break;

                case Messages.ADD_ONCE_AT_A_TIME:
                    List<WeatherModel> weatherModels = (List) msg.obj;
                    int aLen = addToAdapter(WeatherFragment.newInstance(weatherModels));
                    if(aLen-1 == mPosition){
                        setCurrentItem(mPosition);
                    }
                    postFinishedListener.done();
                    break;
                case Messages.ADD_MANY:
                    List<WeatherFragment> fragments = (List) msg.obj;
                    for(WeatherFragment f: fragments){
                        addToAdapter(f);
                    }

                    setCurrentItem(mPosition);
                    break;
                case Messages.UPDATE:

                    replaceFragmentInAdapter(mPosition, (List) msg.obj);

                    break;
                default:
                    int length = addToAdapter((List) msg.obj);
                    postFinishedListener.done();
                    if(length-1==mPosition){
                        setCurrentItem(mPosition);
                    }

                    break;
            }

        }
    };

    public int addToAdapter(List<WeatherModel> weatherModels){
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();
        wpa.add(WeatherFragment.newInstance(weatherModels));
        return wpa.getCount();
    }
    public void replaceFragmentInAdapter(int position, List<WeatherModel> weatherModels){
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();
        WeatherFragment fragment = (WeatherFragment) wpa.getItem(position);
        fragment.setForecastView(weatherModels);
    }

    public int addToAdapter(WeatherFragment fragment){
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();
        wpa.add(fragment);
        return wpa.getCount();
    }
}