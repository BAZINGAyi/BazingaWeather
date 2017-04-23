package com.bazinga.bazingaweather.model;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;

import java.util.List;

/**
 * Created by bazinga on 2017/4/23.
 */

public interface IHandleQueryCountyDataModel {

    void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener);

    // 回调的接口

    interface HandleQueryDataLoadListener{
        void onCompleted(List<County> countyList);
    }
}
