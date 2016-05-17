package com.skycopyhot.core.network;

import com.skycopyhot.core.network.response.CurrentWeatherResponse;
import com.skycopyhot.core.network.response.ForecastWeatherResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yongqiang
 * 14/5/16
 */
public interface WeatherAPIService {

    @GET("data/2.5/weather")
    Observable<CurrentWeatherResponse> getCurrentWeather(@QueryMap Map<String, String> options);

    @GET("data/2.5/forecast/daily")
    Observable<ForecastWeatherResponse> getForecastWeather(@QueryMap Map<String, String> options);
}
