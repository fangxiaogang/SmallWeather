package com.example.xiaogang.smallweather.net;

import com.example.xiaogang.smallweather.model.WeatherBean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaogang on 16/11/13.
 */

public class GetData {
    private static final String TAG = "GetData";

    private GetData(){

    }

    public static GetData getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder{
        private static final GetData INSTANCE = new GetData();
    }
    public void getmeizidata(Subscriber<WeatherBean> subscriber, String city  ){

        Network.getWeatherApi()
                .getWeather(city,"28d595d33c704826861c12d4b9f6bdc6")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
}
