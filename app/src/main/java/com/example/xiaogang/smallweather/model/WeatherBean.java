package com.example.xiaogang.smallweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaogang on 16/11/13.
 */

public class WeatherBean {
    @SerializedName("HeWeather data service 3.0") @Expose
    public List<Weather> mHeWeather
            = new ArrayList<>();
}
