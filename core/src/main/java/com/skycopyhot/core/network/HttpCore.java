package com.skycopyhot.core.network;

import android.content.Context;

import com.skycopyhot.core.R;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class HttpCore {

    private static WeatherAPIService mService;
    private static OkHttpClient mHttpClient;
    private static String mBaseAPI;

    private Context mContext;

    private HttpCore() {
        mBaseAPI = mContext.getString(R.string.base_api);
    }

    private static class HttpCoreInstance {

        public static HttpCore mInstance = new HttpCore();
    }

    public static HttpCore instance() {
        return HttpCoreInstance.mInstance;
    }


    public static WeatherAPIService getService() {
        if (mService == null) {
            mHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mBaseAPI)
                    .client(mHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mService = retrofit.create(WeatherAPIService.class);
        }
        return mService;
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
