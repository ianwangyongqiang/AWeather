package com.skycopyhot.core.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yongqiang
 * 14/5/16
 * the okHttp
 */
public class HttpCore {

    private WeatherAPIService mService;
    private String mBaseAPI;

    public HttpCore(String url) {
        mBaseAPI = url;
    }

    public WeatherAPIService getService() {
        if (mService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mBaseAPI)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mService = retrofit.create(WeatherAPIService.class);
        }
        return mService;
    }
}
