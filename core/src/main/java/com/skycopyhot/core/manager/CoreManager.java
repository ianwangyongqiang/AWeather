package com.skycopyhot.core.manager;

import android.content.Context;

import com.skycopyhot.core.CoreResponse;

import rx.Observable;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class CoreManager {

    public static Observable<CoreResponse> register(Context context) {
        return CoreManagerImpl.register(context);
    }

    public static Observable<CoreResponse> getCurrentWeather(Double lat, Double lon) {
        return CoreManagerImpl.getCurrentWeather(lat, lon);
    }

    public static Observable<CoreResponse> getForecastWeather(Double lat, Double lon, String cnt) {
        return CoreManagerImpl.getForecastWeather(lat, lon, cnt);
    }
}
