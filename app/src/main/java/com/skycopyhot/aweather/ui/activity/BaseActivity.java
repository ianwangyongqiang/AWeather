package com.skycopyhot.aweather.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.skycopyhot.aweather.WeatherApplication;
import com.skycopyhot.aweather.event.EventReady;
import com.skycopyhot.core.event.EventNetworkChanged;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yongqiang
 * 15/5/16
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        if (WeatherApplication.getInstance().isReady()) {
            onReady();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected abstract void onReady();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") EventReady event) {
        onReady();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventNetworkChanged event) {
        onConnectionChanged(event.isConnected());
    }

    protected void onConnectionChanged(boolean isConnected) {

    }

    protected boolean isConnected() {
        return WeatherApplication.getInstance().isConnected();
    }

    protected void showSnack(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            View view = findViewById(android.R.id.content);
            if (view != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
