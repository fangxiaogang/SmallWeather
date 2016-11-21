package com.example.xiaogang.smallweather.net;

import com.example.xiaogang.smallweather.model.WeatherBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xiaogang on 16/11/13.
 */

public interface WeatherApi {
    String WEATHER_API = "https://free-api.heweather.com/x3/";
    @GET("weather")
    Observable<WeatherBean> getWeather(@Query("city") String city, @Query("key") String key);
}
