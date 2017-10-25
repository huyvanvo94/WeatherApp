package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class PreferenceManager{

    private static final String PREFERENCE_FILE_KEY = "com.huyvo.cmpe277.weatherapp.preference_weather";
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public PreferenceManager(Context context){
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }
    


    public void save(List<String> datas){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putInt("length", datas.size());

        for(int i=0; i<datas.size(); i++){
            editor.putString("latlon."+i, datas.get(i));
        }
        editor.commit();
    }

    public String[] get(){
        int length = mSharedPreferences.getInt("save", -1);
        String datas[] = null;

        if(length != -1){
            datas = new String[length];
            for(int i=0; i<length; i++){
                datas[i] = mSharedPreferences.getString("latlon."+i, null);
            }
        }
        return datas;

    }
}
