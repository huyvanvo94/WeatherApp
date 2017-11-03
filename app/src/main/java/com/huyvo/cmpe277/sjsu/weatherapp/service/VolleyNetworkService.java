package com.huyvo.cmpe277.sjsu.weatherapp.service;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.huyvo.cmpe277.sjsu.weatherapp.Singleton;
import com.huyvo.cmpe277.sjsu.weatherapp.WeatherApp;
import com.huyvo.cmpe277.sjsu.weatherapp.util.Logger;

/**
 * Created by Huy Vo on 10/22/17.
 */

public class VolleyNetworkService extends Singleton implements NetworkService {

    public final static String TAG = VolleyNetworkService.class.getSimpleName();

    private static final int VOLLEY_TIME_OUT = 3000;
    private static final int NUMBER_OF_RETRY = 0;

    private final RequestQueue mfRequestQueue;

    private static VolleyNetworkService instance = new VolleyNetworkService();



    private VolleyNetworkService(){
        Logger.d(TAG, "constructor");

        mfRequestQueue = Volley.newRequestQueue(WeatherApp.getInstance());
    }

    public synchronized static VolleyNetworkService getInstance(){
        Logger.d(TAG, "getInstance");
        return instance;
    }

    @Override
    public void cancel(String tag) {

        mfRequestQueue.cancelAll(tag);
    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void getString(String url, String tag, FutureTaskListener<String> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new VolleyResponseListener<>(listener), new VolleyErrorListener<>(listener));
        startRequest(stringRequest, tag);
    }

    @Override
    protected void shutdown() {

    }

    private void startRequest(Request request, String tag){
        request.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_TIME_OUT, NUMBER_OF_RETRY, 0));
        request.setTag(tag);
        mfRequestQueue.add(request);
    }

    private class VolleyResponseListener<T> implements Response.Listener<T> {
        private final FutureTaskListener<T> mListener;

        public VolleyResponseListener(FutureTaskListener<T> listener) {
            mListener = listener;
        }

        @Override
        public void onResponse(T response) {
            mListener.onCompletion(response);
        }
    }

    private class VolleyErrorListener<T> implements Response.ErrorListener {
        private final FutureTaskListener<T> mListener;

        public VolleyErrorListener(FutureTaskListener<T> listener) {
            mListener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mListener.onError(error.getMessage());
        }
    }
}
