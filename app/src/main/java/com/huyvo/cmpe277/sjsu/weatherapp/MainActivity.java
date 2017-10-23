package com.huyvo.cmpe277.sjsu.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherFragment;
import com.huyvo.cmpe277.sjsu.weatherapp.activities.WeatherPageAdapter;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;
import com.huyvo.cmpe277.sjsu.weatherapp.util.JsonParser;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends BaseActivityWithFragment implements ViewPager.OnPageChangeListener{

    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Intent i = getIntent();
        String location = i.getStringExtra("latlon");
        if(location != null){
            List<String> locations = ((WeatherApp) WeatherApp.getInstance()).getLatLngList();
            if(locations.contains(location)){
                final int position = locations.indexOf(location);
                ViewPager pager = (ViewPager) findViewById(R.id.city_viewpager);
                pager.setCurrentItem(position, true);

            }else{
                test_LatLngWeather(location);
            }

        }
    }

    private void init(){
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
                startActivityForResult(i, 1);
                break;

            default:
                break;
        }

        return true;
    }

    private void fetchWeather(){
        final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=alameda&mode=json&units=imperial&cnt=7&appid=b54f500d4a53fdfc96813a4ba9210417";

        requestWeather(URL);
    }

    private void test_LatLngWeather(String location){
        requestWeather("http://api.openweathermap.org/data/2.5/weather?"+location+"&appid=b54f500d4a53fdfc96813a4ba9210417");
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
}