package com.huyvo.cmpe277.sjsu.weatherapp.event;

/**
 * Created by Huy Vo on 10/23/17.
 */

public class Event implements IEvent, Runnable {
    private int eventId;
    private int eventTime;
    private boolean isSynchronous;
    private Thread thread;
    private boolean isComplete = false;
    private ThreadCompleteListener eventListener;

    public Event(final int eventId, final int eventTime, final boolean isSynchronous) {
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.isSynchronous = isSynchronous;
    }

    public boolean isSynchronous() {
        return isSynchronous;
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        if (null == thread) {
            return;
        }
        thread.interrupt();
    }

    @Override
    public void status() {

    }

    @Override
    public void run() {


    }

    public final void addListener(final ThreadCompleteListener listener) {
        this.eventListener = listener;
    }

    public final void removeListener(final ThreadCompleteListener listener) {
        this.eventListener = null;
    }

    private final void completed() {
        if (eventListener != null) {
            eventListener.completedEventHandler(eventId);
        }
    }
}
