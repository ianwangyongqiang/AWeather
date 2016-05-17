package com.skycopyhot.aweather.ui.activity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yongqiang
 * 15/5/16
 */
public final class Navigator {

    public static void toWeatherActivity(Context context) {
        context.startActivity(new Intent(context, WeatherActivity.class));
    }
}
