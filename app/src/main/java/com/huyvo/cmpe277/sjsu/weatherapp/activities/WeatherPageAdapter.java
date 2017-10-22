package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A page adapter to hold cities
 */
public class WeatherPageAdapter extends FragmentPagerAdapter {
    private List<WeatherFragment> mWFragments = new ArrayList<>();
    private static int index = 0;

    public WeatherPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void add(WeatherFragment weatherFragment){
        Log.d("WeatherPageAdapter", String.valueOf(index) +"|" +mWFragments.size());

        mWFragments.add(weatherFragment);
        notifyDataSetChanged();
    }

    public void remove(WeatherFragment weatherFragment){
        mWFragments.remove(weatherFragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mWFragments.get(position);
    }

    @Override
    public int getCount() {
        return mWFragments.size();
    }
}
