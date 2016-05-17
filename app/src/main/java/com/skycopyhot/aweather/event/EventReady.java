package com.skycopyhot.aweather.event;

/**
 * Created by yongqiang
 * 15/5/16
 */
public class EventReady {

    private boolean mReady;

    public EventReady(boolean ready) {
        mReady = ready;
    }

    public boolean isReady() {
        return mReady;
    }
}
