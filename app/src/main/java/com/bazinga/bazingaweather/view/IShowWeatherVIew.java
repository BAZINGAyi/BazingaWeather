package com.bazinga.bazingaweather.view;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.gson.Weather;

import java.util.List;
import java.util.Map;

/**
 * Created by bazinga on 2017/4/17.
 */

public interface IShowWeatherVIew {

 //   void showWeatherData(Weather weather);

    void showWeatherData(Map<String,Object> weather);

    void showWeatherError(String msg);

    void showPic(String pic);

    void showPicError(String msg);

    void showloadProgress();

    void closeloadProgress();
}
