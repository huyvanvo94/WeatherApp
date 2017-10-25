package com.huyvo.cmpe277.sjsu.weatherapp.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Huy Vo on 10/23/17.
 */

public class EventManager implements ThreadCompleteListener {

    public static final int MAX_RUNNING_EVENTS = 1000; // Just don't wanna have too many running events. :)
    public static final int MIN_ID = 1;
    public static final int MAX_ID = MAX_RUNNING_EVENTS;
    public static final int MAX_EVENT_TIME = 1800; // in seconds / 30 minutes.
    private int currentlyRunningSyncEvent = -1;
    private Random rand;
    private Map<Integer, Event> eventPool;

    public EventManager() {
        rand = new Random(1);
        eventPool = new ConcurrentHashMap<>(MAX_RUNNING_EVENTS);
    }


    public int create(int eventTime) {

        int eventId = createEvent(eventTime, true);
        currentlyRunningSyncEvent = eventId;

        return eventId;
    }


    public int createAsync(int eventTime){
        return createEvent(eventTime, false);
    }

    public void addEvent(Event event){
        
    }
    private int createEvent(int eventTime, boolean isSynchronous) {
        int newEventId = generateId();

        Event newEvent = new Event(newEventId, eventTime, isSynchronous);
        newEvent.addListener(this);
        eventPool.put(newEventId, newEvent);

        return newEventId;
    }

    public void start(int eventId)  {
        eventPool.get(eventId).start();
    }

    public void cancel(int eventId) {
        if (!eventPool.containsKey(eventId)) {
            return;
        }

        if (eventId == currentlyRunningSyncEvent) {
            currentlyRunningSyncEvent = -1;
        }

        eventPool.get(eventId).stop();
        eventPool.remove(eventId);
    }

    public void status(int eventId) {
        if (!eventPool.containsKey(eventId)) {
            return;
        }

        eventPool.get(eventId).status();
    }

    /**
     * Gets status of all running events.
     */
    @SuppressWarnings("rawtypes")
    public void statusOfAllEvents() {
        Iterator it = eventPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ((Event) pair.getValue()).status();
        }
    }

    /**
     * Stop all running events.
     */
    @SuppressWarnings("rawtypes")
    public void shutdown() {
        Iterator it = eventPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ((Event) pair.getValue()).stop();
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     */
    private int generateId() {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((MAX_ID - MIN_ID) + 1) + MIN_ID;
        while (eventPool.containsKey(randomNum)) {
            randomNum = rand.nextInt((MAX_ID - MIN_ID) + 1) + MIN_ID;
        }

        return randomNum;
    }


    @Override
    public void completedEventHandler(int eventId) {
        eventPool.get(eventId).status();
        if (eventPool.get(eventId).isSynchronous()) {
            currentlyRunningSyncEvent = -1;
        }
        eventPool.remove(eventId);
    }

    public Map<Integer, Event> getEventPool() {
        return eventPool;
    }

    public int numOfCurrentlyRunningSyncEvent() {
        return currentlyRunningSyncEvent;
    }
}