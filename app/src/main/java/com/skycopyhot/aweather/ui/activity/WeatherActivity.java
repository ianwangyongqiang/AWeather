package com.skycopyhot.aweather.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.skycopyhot.aweather.R;
import com.skycopyhot.aweather.model.Weather;
import com.skycopyhot.aweather.ui.fragment.WeatherFragment;
import com.skycopyhot.aweather.ui.view.CirclePageIndicator;
import com.skycopyhot.core.CoreHelper;
import com.skycopyhot.core.CoreResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.vp_weather)
    ViewPager vpWeather;
    @BindView(R.id.cpi_weather)
    CirclePageIndicator cpiWeather;
    @BindView(R.id.pb_weather)
    ProgressBar pbWeather;

    private List<Weather> mDataList = new ArrayList<>();

    private GoogleApiClient mGoogleApiClient;

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    private final GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle connectionHint) {
            if (PackageManager.PERMISSION_GRANTED == getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    PackageManager.PERMISSION_GRANTED == getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location != null) {
                    WeatherActivity.this.getData(location);
                }
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            mGoogleApiClient.connect();
        }
    };

    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            if (mResolvingError) {
                return;
            }
            if (connectionResult.hasResolution()) {
                try {
                    mResolvingError = true;
                    connectionResult.startResolutionForResult(WeatherActivity.this, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    mGoogleApiClient.connect();
                }
            } else {
                mResolvingError = true;
                WeatherActivity.this.showSnack("Get location error, code = " + connectionResult.getErrorCode());
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onReady() {
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private void getData(Location location) {

        CoreHelper.getForecastWeather(location.getLatitude(), location.getLongitude(), "7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<CoreResponse>bindToLifecycle())
                .compose(new Observable.Transformer<CoreResponse, CoreResponse>() {
                    @Override
                    public Observable<CoreResponse> call(Observable<CoreResponse> coreResponseObservable) {
                        return coreResponseObservable.doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                pbWeather.setVisibility(View.VISIBLE);
                            }
                        }).doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                pbWeather.setVisibility(View.GONE);
                            }
                        }).doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                pbWeather.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .subscribe(new Subscriber<CoreResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showSnack("Failed to get weather, " + e.getMessage());
                    }

                    @Override
                    public void onNext(CoreResponse coreResponse) {
                        parseResponse(coreResponse);
                        refreshUI();
                    }
                });
    }

    private void refreshUI() {
        WeatherFragmentAdapter adapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        vpWeather.setAdapter(adapter);
        cpiWeather.setViewPager(vpWeather);
    }

    private void parseResponse(CoreResponse response) {
        String city = response.mExtraData.getString(CoreResponse.CITY_NAME);
        ArrayList<String> dateList = response.mExtraData.getStringArrayList(CoreResponse.DATE);
        ArrayList<String> desList = response.mExtraData.getStringArrayList(CoreResponse.WEATHER_DESCRIPTION);
        ArrayList<String> iconList = response.mExtraData.getStringArrayList(CoreResponse.WEATHER_ICON);
        ArrayList<String> tempAveList = response.mExtraData.getStringArrayList(CoreResponse.WEATHER_TEMP_AVERAGE);
        ArrayList<String> tempMinList = response.mExtraData.getStringArrayList(CoreResponse.WEATHER_TEMP_MIN);
        ArrayList<String> tempMaxList = response.mExtraData.getStringArrayList(CoreResponse.WEATHER_TEMP_MAX);
        mDataList.clear();
        if (dateList != null && desList != null && iconList != null && tempAveList != null && tempMaxList != null && tempMinList != null) {
            for (int i = 0; i < dateList.size(); i++) {
                mDataList.add(new Weather(city, desList.get(i), iconList.get(i), dateList.get(i), tempAveList.get(i), tempMaxList.get(i), tempMinList.get(i)));
            }
        }
    }


    private class WeatherFragmentAdapter extends FragmentPagerAdapter {

        public WeatherFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position the position
         */
        @Override
        public Fragment getItem(int position) {
            return WeatherFragment.newInstance(mDataList.get(position));
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }
    }

}
