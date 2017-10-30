package com.huyvo.cmpe277.sjsu.weatherapp.activities;


import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsActivity extends BaseActivityWithFragment {

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
