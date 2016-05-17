package com.skycopyhot.aweather.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.skycopyhot.aweather.WeatherApplication;
import com.skycopyhot.aweather.event.EventReady;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yongqiang
 * 15/5/16
 */
public abstract class BaseFragment extends RxFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
        if (WeatherApplication.getInstance().isReady()) {
            onReady();
        }
    }

    protected void onReady() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") EventReady ready) {
        onReady();
    }
}
