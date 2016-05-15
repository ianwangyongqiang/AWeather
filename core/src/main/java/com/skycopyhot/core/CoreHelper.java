package com.skycopyhot.core;

import android.content.Context;

import com.skycopyhot.core.manager.CoreManager;

import rx.Observable;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class CoreHelper {

    public static Observable<CoreResponse> register(Context context) {
        return CoreManager.register(context);
    }

    public static Observable<CoreResponse> getCurrentWeather(Double lat, Double lon) {
        return CoreManager.getCurrentWeather(lat, lon);
    }

    public static Observable<CoreResponse> getForecastWeather(Double lat, Double lon, String cnt) {
        return CoreManager.getForecastWeather(lat, lon, cnt);
    }

}
