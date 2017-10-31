package com.huyvo.cmpe277.sjsu.weatherapp;

import com.huyvo.cmpe277.sjsu.weatherapp.model.BaseModel;

/**
 * Created by Huy Vo on 10/31/17.
 */

public interface Request {
    void onComplete(BaseModel model);
    void onError(BaseModel model);
}
