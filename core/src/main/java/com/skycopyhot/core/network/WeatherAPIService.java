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

    @GET("data/2.5/weather?lat={lat}&lon={lon}&apikey={key}")
    Observable<CurrentWeatherResponse> getCurrentWeather(@Path("lat") String lat, @Path("lon") String lon, @Path("key") String key);

    @GET("data/2.5/forecast/daily?lat={lat}&lon={lon}&cnt={cnt}&apikey={key}")
    Observable<ForecastWeatherResponse> getForecastWeather(@Path("lat") String lat, @Path("lon") String lon, @Path("cnt") String cnt, @Path("key") String key);
}
