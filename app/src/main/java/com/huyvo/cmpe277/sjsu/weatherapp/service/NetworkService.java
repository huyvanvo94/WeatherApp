package com.huyvo.cmpe277.sjsu.weatherapp.service;

/**
 * Created by Huy Vo on 10/5/17.
 */

public interface NetworkService {
    void cancel(String tag);
    void cancelAll();
    void getString(String url, String tag, FutureTaskListener<String> listener);
}

