package com.huyvo.cmpe277.sjsu.weatherapp.service;

/**
 * Created by Huy Vo on 10/5/17.
 */

public interface FutureTaskListener<V> {
    void onCompletion(V result);
    void onError(String errorMessage);
    void onProgress(float progress);
}