package com.huyvo.cmpe277.sjsu.weatherapp.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Configurations;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

public class SettingsActivity extends BaseActivityWithFragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "onCreate");
        setContentView(R.layout.activity_settings);
        onLoadUI();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Configurations.setUnit(isChecked);
        ((WeatherApp) WeatherApp.getInstance()).saveUnitChange(isChecked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onLoadUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        Switch prefSwitch = (Switch) findViewById(R.id.a_switch);
        prefSwitch.setChecked(Configurations.isImperial());

        prefSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onFetchPeriodically() {

    }

    @Override
    protected void onLoadData() {

    }
}


