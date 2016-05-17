package com.skycopyhot.core.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skycopyhot.core.R;
import com.skycopyhot.core.event.EventNetworkChanged;
import com.skycopyhot.core.network.HttpCore;
import com.skycopyhot.core.network.WeatherAPIService;

import org.greenrobot.eventbus.EventBus;

import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by yongqiang
 * 14/5/16
 * network manager, manager the http and network status
 */
class NetworkManager {

    private Context mContext;
    private boolean mIsConnected;
    private HttpCore mHttpCore;

    public NetworkManager(Context context) {
        mContext = context;
        mHttpCore = new HttpCore(mContext.getString(R.string.base_api));
    }

    public Context getContext() {
        return mContext;
    }

    public WeatherAPIService getService() {
        return mHttpCore.getService();
    }

    public Observable<Boolean> init() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    refreshNetworkState();
                    mContext.registerReceiver(mNetworkChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    subscriber.onNext(mIsConnected);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private BroadcastReceiver mNetworkChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                final boolean isConnected = mIsConnected;
                refreshNetworkState();
                if (mIsConnected != isConnected) {
                    EventBus.getDefault().post(new EventNetworkChanged(mIsConnected));
                }
            }
        }
    };

    private void refreshNetworkState() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isConnection = false;
        if (info != null) {
            isConnection = info.isConnected();
        }
        mIsConnected = isConnection;
    }

    public static<T> Observable<T> translate(final Observable<T> observable) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                observable.observeOn(Schedulers.io())
                        .subscribe(new Subscriber<T>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof UnknownHostException) {
                                    subscriber.onError(new Throwable("No Internet Connection."));
                                } else {
                                    subscriber.onError(e);
                                }
                            }

                            @Override
                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }
                        });
            }
        }) ;
    }
}
