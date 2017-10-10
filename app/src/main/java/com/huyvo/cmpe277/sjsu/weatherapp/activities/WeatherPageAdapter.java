package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

/**
 * A page adapter to hold cities
 */
public class WeatherPageAdapter extends FragmentPagerAdapter {
    private final static int SIZE = 2;

    public WeatherPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            default:
                return WeatherFragment.newInstance(new WeatherModel());
        }
    }

    @Override
    public int getCount() {
        return SIZE;
    }
}
