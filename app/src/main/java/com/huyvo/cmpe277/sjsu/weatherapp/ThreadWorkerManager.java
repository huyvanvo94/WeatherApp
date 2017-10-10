package com.huyvo.cmpe277.sjsu.weatherapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Huy Vo on 10/9/17.
 */

public class ThreadWorkerManager {
    public final static int POOL_SIZE = 10;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(POOL_SIZE);

    public ThreadWorkerManager(){

    }

    public void executeWorker(Runnable r){
        mExecutorService.execute(r);
    }

    public void executeWorker(Thread t){
        mExecutorService.execute(t);
    }

    public void shutdown(){
        mExecutorService.shutdown();
    }
}
