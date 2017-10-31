package com.huyvo.cmpe277.sjsu.weatherapp.activities;


import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.huyvo.cmpe277.sjsu.weatherapp.R;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Configurations;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

public class SettingsActivity extends BaseActivityWithFragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "onCreate");
        setContentView(R.layout.activity_settings);
        init_UI();

    }

    private void init_UI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        Switch prefSwitch = (Switch) findViewById(R.id.switch_temp_pref);
        prefSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            //metric
            Configurations.isImperial = false;
        }else{
            //imperial
            Configurations.isImperial = true;
        }
    }

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

    @Override
    public void onPause(){
        Logger.d(TAG, "onPause");
        super.onPause();
       // bundle.putBoolean("SwitchState", prefSwitch. );
    }

    @Override
    public void onResume(){
        Logger.d(TAG, "onResume");
        super.onResume();
    }



}
