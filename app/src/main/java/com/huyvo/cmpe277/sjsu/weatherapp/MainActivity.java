package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
import com.huyvo.cmpe277.sjsu.weatherapp.service.intent.UpdateForecastIntentService;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Constants;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.ADD;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.ADD_EMPTY;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.ADD_MANY;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.ADD_ONCE_AT_A_TIME;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.UPDATE;
import static com.huyvo.cmpe277.sjsu.weatherapp.util.Constants.MainViewMessages.UPDATE_FORECAST;

public class MainActivity extends BaseActivityWithFragment implements ViewPager.OnPageChangeListener{

    public final static String TAG = "MainActivity";

    private PostFinishedListener postFinishedListener;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Logger.d(TAG, "onCreate");
        initUI();

        Intent i = getIntent();
        mPosition = i.getIntExtra("position", -1);

        List<String> mLocations = WeatherApp.getLatLngList();
        if(!mLocations.isEmpty()){
            postFinishedListener = new PostForecastRunnable(mLocations);
            new Thread((Runnable) postFinishedListener).start();
            // Update Every 3 hours if user is on screen
            ScheduledExecutorService executorService= Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(new UpdateForecastPeriodicallyRunnable(), 0, 3, TimeUnit.HOURS);
        }



    }


    public void setCurrentItem(int position){
        ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
        pager.setCurrentItem(position);
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
       // Logger.d(TAG, "onPageScrolled="+position);

    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(TAG, "onPageSelected="+position);
        WeatherForecastContainer container = WeatherForecastContainer.getInstance();
        boolean shouldRequestFetchWeather = container.shouldRequestFetchWeather(WeatherApp.getLatLngList().get(position));
        if(shouldRequestFetchWeather){
            mPosition = position;
            new Thread(new UpdateWeatherForecastRunnable(WeatherApp.getLatLngList().get(mPosition))).start();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class PostForecastRunnable implements Runnable, PostFinishedListener, Postable {
        private Queue<String> mLocations;
        public PostForecastRunnable(List<String> locations){

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
                try {
                    WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                    List<WeatherModel> weatherModels = weatherForecastContainer.getWeatherModels(location);

                    Message message = mHandler.obtainMessage();
                    message.obj = weatherModels;
                    message.what = -1;
                    mHandler.sendMessage(message);
                }catch (Exception e){}

            }
        }
    }

    class UpdateForecastPeriodicallyRunnable implements Runnable{


        public UpdateForecastPeriodicallyRunnable(){
        }

        @Override
        public void run() {

            List<String> locations = WeatherApp.getLatLngList();

            for(String location: locations) {
                Intent intent = new Intent(MainActivity.this, UpdateForecastIntentService.class);
                intent.putExtra(UpdateForecastIntentService.UPDATE, new Messenger(mHandler));
                intent.putExtra(UpdateForecastIntentService.LOCATION, location);
                startService(intent);
            }
        }


    }


    class LoadForecastRunnable implements Runnable{

        private String mLocation;
        public LoadForecastRunnable(int position){
            mLocation = WeatherApp.getLatLngList().get(position);
        }

        public LoadForecastRunnable(String location){
            mLocation = location;
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
                    message.what = Constants.MainViewMessages.ADD;
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
                        msg.what = Constants.MainViewMessages.UPDATE;
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

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            if(reply != null){
                String location = reply.getString(UpdateForecastIntentService.LOCATION, null);
                if(location != null){
                    WeatherForecastContainer weatherForecastContainer = WeatherForecastContainer.getInstance();
                    List<WeatherModel> models = weatherForecastContainer.getWeatherModels(location);

                    int position = WeatherApp.getLatLngList().indexOf(location);
                    replaceFragmentInAdapter(position, models);

                    return;
                }


            }


            switch (msg.what){
                case ADD:
                    List<WeatherModel> models = (List) msg.obj;

                    replaceFragmentInAdapter(mPosition, models);
                    setCurrentItem(mPosition);
                    break;
                case ADD_EMPTY:

                    int len = addToAdapter(new WeatherFragment());

                    if(mPosition == len-1){
                        new Thread(new LoadForecastRunnable(mPosition)).start();
                    }

                    break;

                case ADD_ONCE_AT_A_TIME:
                    List<WeatherModel> weatherModels = (List) msg.obj;
                    int aLen = addToAdapter(WeatherFragment.newInstance(weatherModels));
                    if(aLen-1 == mPosition){
                        setCurrentItem(mPosition);
                    }
                    postFinishedListener.done();
                    break;
                case ADD_MANY:
                    List<WeatherFragment> fragments = (List) msg.obj;
                    for(WeatherFragment f: fragments){
                        addToAdapter(f);
                    }

                    setCurrentItem(mPosition);
                    break;
                case UPDATE:

                    replaceFragmentInAdapter(mPosition, (List) msg.obj);

                    break;

                case UPDATE_FORECAST:
                    ForecastInfo info = (ForecastInfo) msg.obj;
                    replaceFragmentInAdapter(info.position, info.mList);
                    break;
                default:
                    if( msg.obj != null) {

                        int length = addToAdapter((List) msg.obj);
                        postFinishedListener.done();
                        if (length - 1 == mPosition) {
                            setCurrentItem(mPosition);
                        }
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
    public boolean replaceFragmentInAdapter(int position, List<WeatherModel> weatherModels){
        Logger.d(TAG, "replaceFragmentInAdapter");
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();

        if(position >= wpa.getCount()){
            return false;
        }
        WeatherFragment fragment = (WeatherFragment) wpa.getItem(position);
        if(fragment == null){
            return false;
        }
        fragment.setForecastView(weatherModels);
        return true;
    }

    public int addToAdapter(WeatherFragment fragment){
        ViewPager vp = (ViewPager) findViewById(R.id.city_viewpager);
        WeatherPageAdapter wpa = (WeatherPageAdapter) vp.getAdapter();
        wpa.add(fragment);
        return wpa.getCount();
    }

    class ForecastInfo{
        public int position;
        public List<WeatherModel> mList;
    }

}