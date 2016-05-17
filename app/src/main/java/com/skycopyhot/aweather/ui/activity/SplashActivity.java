package com.skycopyhot.aweather.ui.activity;

import android.os.Bundle;

import com.skycopyhot.aweather.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onReady() {
        setContentView(R.layout.activity_splash);

        Navigator.toWeatherActivity(this);
        finish();
    }
}
