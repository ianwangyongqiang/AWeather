package com.skycopyhot.aweather;

import android.app.Application;

import com.skycopyhot.aweather.event.EventReady;
import com.skycopyhot.core.CoreHelper;
import com.skycopyhot.core.CoreResponse;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by yongqiang
 * 15/5/16
 */
public class WeatherApplication extends Application {

    private static WeatherApplication mInstance;

    /**
     * judge if it is ready for Core
     */
    private boolean mReady = false;

    /**
     * judge the network status
     */
    private boolean mIsConnected = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        CoreHelper.register(this)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CoreResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CoreResponse coreResponse) {
                        mInstance.mIsConnected = coreResponse.mExtraData.getBoolean(CoreResponse.NETWORK_CONNECTION);
                        mInstance.mReady = true;
                        EventBus.getDefault().post(new EventReady(true));
                    }
                });
    }


    public static WeatherApplication getInstance() {
        return mInstance;
    }

    public boolean isReady() {
        return mReady;
    }

    public boolean isConnected() {
        return mIsConnected;
    }
}
