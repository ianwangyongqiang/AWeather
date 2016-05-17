package com.skycopyhot.core;

import android.content.Context;

import com.skycopyhot.core.manager.CoreManager;

import rx.Observable;

/**
 * Created by yongqiang
 * 14/5/16
 * Core helper
 */
public class CoreHelper {

    /**
     * register Core
     * @param context the context
     * @return observable
     */
    public static Observable<CoreResponse> register(Context context) {
        return CoreManager.register(context);
    }

    /**
     * get current weather (not implement for now)
     * @param lat latitude
     * @param lon longitude
     * @return observable
     */
    public static Observable<CoreResponse> getCurrentWeather(Double lat, Double lon) {
        return CoreManager.getCurrentWeather(lat, lon);
    }

    /**
     * get the forecast weather
     * @param lat latitude
     * @param lon longitude
     * @param cnt the day count you want
     * @return observable
     */
    public static Observable<CoreResponse> getForecastWeather(Double lat, Double lon, String cnt) {
        return CoreManager.getForecastWeather(lat, lon, cnt);
    }

}
