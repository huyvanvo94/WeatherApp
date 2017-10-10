package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.MainActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Huy Vo on 10/5/17.
 */

public final class Logger {
    private Logger() {}

    private static final List<String> CLASS_SIMPLE_NAMES = Arrays.asList(
            MainActivity.TAG
    );

    public static void d(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.d(tag, msg);
        }
    }

    public static synchronized void e(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.e(tag, msg);
        }
    }
}