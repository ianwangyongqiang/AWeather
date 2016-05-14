package com.skycopyhot.core.event;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class EventNetworkChanged {

    private boolean mIsConnected;

    public EventNetworkChanged(boolean isConnected) {
        mIsConnected = isConnected;
    }

}
