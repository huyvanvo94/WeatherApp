package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A page adapter to hold cities
 */
public class WeatherPageAdapter extends FragmentPagerAdapter {
    private List<WeatherFragment> mWFragments = new ArrayList<>();



    public WeatherPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(WeatherFragment weatherFragment){
        mWFragments.add(weatherFragment);
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
