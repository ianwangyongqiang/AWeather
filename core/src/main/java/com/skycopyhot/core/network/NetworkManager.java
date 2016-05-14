package com.skycopyhot.core.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skycopyhot.core.event.EventNetworkChanged;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class NetworkManager {

    private Context mContext;

    private BroadcastReceiver mNetworkChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                refreshNetworkState();
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

        EventBus.getDefault().post(new EventNetworkChanged(isConnection));
    }
}
