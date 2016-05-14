package com.skycopyhot.core.network;

import com.skycopyhot.core.network.response.CurrentWeatherResponse;
import com.skycopyhot.core.network.response.ForecastWeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yongqiang
 * 14/5/16
 */
public interface WeatherAPIService {

    @GET("data/2.5/weather?lat={lat}&lon={lon}")
    Observable<CurrentWeatherResponse> getCurrentWeather(@Path("lat") String lat, @Path("lon") String lon);

    @GET("data/2.5/forecast?lat={lat}&lon={lon}")
    Observable<ForecastWeatherResponse> getForecastWeather(@Path("lat") String lat, @Path("lon") String lon);
}
