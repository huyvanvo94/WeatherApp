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
    private int index = 0;

    public WeatherPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void add(WeatherFragment weatherFragment){
        if(mWFragments.contains(weatherFragment)){
            replace(weatherFragment);
        }else {
            mWFragments.add(weatherFragment);
            index++;
        }
        notifyDataSetChanged();
    }

    public void replace(WeatherFragment weatherFragment){
        mWFragments.set(index, weatherFragment);
    }

    public void replace(int index, WeatherFragment fragment){
        mWFragments.set(index, fragment);
    }

    public void remove(WeatherFragment weatherFragment){
        mWFragments.remove(weatherFragment);
        index --;
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
