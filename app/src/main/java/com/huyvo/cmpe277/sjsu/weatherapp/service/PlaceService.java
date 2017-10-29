package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;

/**
 * Created by Huy Vo on 10/29/17.
 */

public interface PlaceService {
    void getLocalTime(String location, FutureTaskListener<LocalTimeModel> listener);
}
