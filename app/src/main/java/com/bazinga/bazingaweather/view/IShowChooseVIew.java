package com.bazinga.bazingaweather.view;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;

import java.util.List;

/**
 * Created by bazinga on 2017/4/17.
 */

public interface IShowChooseVIew {

    void showProvinceData(List<Province> provinceList);

    void showCityData(List<City> cityList);

    void showCountyData(List<County> countyList);

    void showProvinceError(String msg);

    void showCityError(String msg);

    void showCountyError(String msg);

    void showloadProgress();

    void closeloadProgress();
}
