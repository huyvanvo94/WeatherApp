package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Huy Vo on 9/16/17.
 */

public class BaseActivityWithFragment extends AppCompatActivity{

    public void showFragment(int containerResId, Fragment fragment){
        showFragment(containerResId, fragment, false, false);
    }

    public void showFragment(int containerResId, Fragment fragment, boolean addToBackStack){
        showFragment(containerResId, fragment, addToBackStack, false);
    }

    public void showFragment(int containerResId, Fragment fragment, boolean addToBackStack, boolean isAnimated){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerResId, fragment, fragment.getClass().getSimpleName());
        if(addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }
}