package com.bazinga.bazingaweather.model;

import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.gson.Weather;

import java.util.List;

/**
 * Created by bazinga on 2017/4/23.
 */

public interface IHandleQueryWeatherDataModel {
    void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener);

    // 回调的接口

    interface HandleQueryDataLoadListener{
        void onCompleted(Weather weather);
    }

    void loadPicProgress(HandleQueryPicLoadListener handleQueryPicLoadListener);

    // 回调的接口

    interface HandleQueryPicLoadListener{
        void onCompleted(String string);
    }

}
