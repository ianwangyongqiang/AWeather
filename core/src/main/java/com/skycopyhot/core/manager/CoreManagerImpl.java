package com.skycopyhot.core.manager;

import android.content.Context;
import android.text.TextUtils;

import com.skycopyhot.core.CoreResponse;
import com.skycopyhot.core.R;
import com.skycopyhot.core.model.City;
import com.skycopyhot.core.model.Forecast;
import com.skycopyhot.core.model.Main;
import com.skycopyhot.core.model.Temp;
import com.skycopyhot.core.model.Weather;
import com.skycopyhot.core.network.response.CurrentWeatherResponse;
import com.skycopyhot.core.network.response.ForecastWeatherResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by yongqiang
 * 14/5/16
 * implement of the core manager
 */
class CoreManagerImpl {

    private static NetworkManager mNetworkManager;

    public static Observable<CoreResponse> register(Context context) {
        mNetworkManager = new NetworkManager(context);
        return Observable.create(new Observable.OnSubscribe<CoreResponse>() {
            @Override
            public void call(final Subscriber<? super CoreResponse> subscriber) {
                mNetworkManager.init()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                CoreResponse response = new CoreResponse();
                                response.mExtraData.putBoolean(CoreResponse.NETWORK_CONNECTION, aBoolean);
                                subscriber.onNext(response);
                            }
                        });
            }
        });
    }

    public static Observable<CoreResponse> getCurrentWeather(final Double lat, final Double lon) {
        return Observable.create(new Observable.OnSubscribe<CoreResponse>() {
            @Override
            public void call(final Subscriber<? super CoreResponse> subscriber) {
                Map<String, String> options = new HashMap<>();
                options.put("lat", String.valueOf(lat));
                options.put("lon", String.valueOf(lon));
                options.put("apikey", mNetworkManager.getContext().getString(R.string.api_key));
                NetworkManager.translate(mNetworkManager.getService().getCurrentWeather(options))
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CurrentWeatherResponse>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(CurrentWeatherResponse currentWeatherResponse) {
                                CoreResponse response = new CoreResponse();
                                if (!TextUtils.isEmpty(currentWeatherResponse.getName())) {
                                    response.mExtraData.putString(CoreResponse.CITY_NAME, currentWeatherResponse.getName());
                                }
                                if (currentWeatherResponse.getWeather() != null && currentWeatherResponse.getWeather().size() > 0) {
                                    Weather weather = currentWeatherResponse.getWeather().get(0);
                                    if (!TextUtils.isEmpty(weather.getDescription())) {
                                        response.mExtraData.putString(CoreResponse.WEATHER_DESCRIPTION, weather.getDescription());
                                    }
                                    if (!TextUtils.isEmpty(weather.getIcon())) {
                                        response.mExtraData.putString(CoreResponse.WEATHER_ICON, CoreManagerImpl.getIconUrl(weather.getIcon()));
                                    }
                                }
                                if (currentWeatherResponse.getMain() != null) {
                                    Main main = currentWeatherResponse.getMain();
                                    if (main.getTemp() != null) {
                                        response.mExtraData.putString(CoreResponse.WEATHER_TEMP_AVERAGE, CoreManagerImpl.getTempString(main.getTemp()));
                                    }
                                    if (main.getTempMin() != null) {
                                        response.mExtraData.putString(CoreResponse.WEATHER_TEMP_MIN, CoreManagerImpl.getTempString(main.getTempMin()));
                                    }
                                    if (main.getTempMax() != null) {
                                        response.mExtraData.putString(CoreResponse.WEATHER_TEMP_MAX, CoreManagerImpl.getTempString(main.getTempMax()));
                                    }
                                }
                                subscriber.onNext(response);
                            }
                        });
            }
        });
    }

    public static Observable<CoreResponse> getForecastWeather(final Double lat, final Double lon, final String cnt) {
        return Observable.create(new Observable.OnSubscribe<CoreResponse>() {
            @Override
            public void call(final Subscriber<? super CoreResponse> subscriber) {
                Map<String, String> options = new HashMap<>();
                options.put("lat", String.valueOf(lat));
                options.put("lon", String.valueOf(lon));
                options.put("cnt", cnt);
                options.put("apikey", mNetworkManager.getContext().getString(R.string.api_key));
                NetworkManager.translate(mNetworkManager.getService().getForecastWeather(options))
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ForecastWeatherResponse>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(ForecastWeatherResponse forecastWeatherResponse) {
                                CoreResponse response = new CoreResponse();
                                if (forecastWeatherResponse.getCity() != null) {
                                    City city = forecastWeatherResponse.getCity();
                                    if (!TextUtils.isEmpty(city.getName())) {
                                        response.mExtraData.putString(CoreResponse.CITY_NAME, city.getName());
                                    }
                                }
                                if (forecastWeatherResponse.getList() != null) {
                                    ArrayList<String> descriptionList = new ArrayList<>();
                                    ArrayList<String> iconList = new ArrayList<>();
                                    ArrayList<String> tempAveList = new ArrayList<>();
                                    ArrayList<String> tempMinList = new ArrayList<>();
                                    ArrayList<String> tempMaxList = new ArrayList<>();
                                    ArrayList<String> dateList = new ArrayList<>();
                                    for (Forecast forecast : forecastWeatherResponse.getList()) {
                                        dateList.add(CoreManagerImpl.getDate(forecast.getDt()));
                                        Weather weather = forecast.getWeather().get(0);
                                        if (!TextUtils.isEmpty(weather.getDescription())) {
                                            descriptionList.add(weather.getDescription());
                                        }
                                        if (!TextUtils.isEmpty(weather.getIcon())) {
                                            iconList.add(CoreManagerImpl.getIconUrl(weather.getIcon()));
                                        }
                                        Temp temp = forecast.getTemp();
                                        if (temp.getDay() != null) {
                                            tempAveList.add(CoreManagerImpl.getTempString(temp.getDay()));
                                        }
                                        if (temp.getMin() != null) {
                                            tempMinList.add(CoreManagerImpl.getTempString(temp.getMin()));
                                        }
                                        if (temp.getMax() != null) {
                                            tempMaxList.add(CoreManagerImpl.getTempString(temp.getMax()));
                                        }
                                    }
                                    response.mExtraData.putStringArrayList(CoreResponse.WEATHER_DESCRIPTION, descriptionList);
                                    response.mExtraData.putStringArrayList(CoreResponse.WEATHER_ICON, iconList);
                                    response.mExtraData.putStringArrayList(CoreResponse.WEATHER_TEMP_AVERAGE, tempAveList);
                                    response.mExtraData.putStringArrayList(CoreResponse.WEATHER_TEMP_MIN, tempMinList);
                                    response.mExtraData.putStringArrayList(CoreResponse.WEATHER_TEMP_MAX, tempMaxList);
                                    response.mExtraData.putStringArrayList(CoreResponse.DATE, dateList);
                                    subscriber.onNext(response);
                                }
                            }
                        });
            }
        });
    }

    private static String getIconUrl(String icon) {
        return mNetworkManager.getContext().getString(R.string.base_api) + "/img/w/" + icon + ".png";
    }

    private static String getTempString(Double temp) {
        return String.valueOf(temp) +  "℉";
    }

    private static String getDate(Integer date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date((long) date * 1000));
    }
}
