package com.bazinga.bazingaweather.model;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.Province;

import java.util.List;
import java.util.Map;

/**
 * Created by bazinga on 2017/4/23.
 */

public interface IHandleQueryProvinceDataModel {
    void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener);

    // 回调的接口

    interface HandleQueryDataLoadListener{
        void onCompleted(List<Province> provinceList );
    }

}
