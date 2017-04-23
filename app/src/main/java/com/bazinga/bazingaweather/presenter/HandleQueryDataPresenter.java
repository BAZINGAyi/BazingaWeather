package com.bazinga.bazingaweather.presenter;

import android.app.Activity;
import android.content.Context;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.model.HandleQueryCityDataModel;
import com.bazinga.bazingaweather.model.HandleQueryCountyDataModel;
import com.bazinga.bazingaweather.model.HandleQueryProvinceDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryCityDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryCountyDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryProvinceDataModel;
import com.bazinga.bazingaweather.view.IShowChooseVIew;

import java.util.List;
import java.util.Map;

/**
 * Created by bazinga on 2017/4/23.
 */

public class HandleQueryDataPresenter extends BasePresenter<IShowChooseVIew> {

    private Activity activity;

    public void setActivity(Activity activity){
       this.activity = activity;
    }

    HandleQueryCountyDataModel handleQueryCountyDataModel = new HandleQueryCountyDataModel();

    HandleQueryCityDataModel handleQueryCityDataModel = new HandleQueryCityDataModel();

    HandleQueryProvinceDataModel handleQueryProvinceDataModel = new HandleQueryProvinceDataModel();

    IShowChooseVIew iShowChooseVIew = null;

    public void attach(IShowChooseVIew iShowChooseVIew){
        this.iShowChooseVIew = iShowChooseVIew;
    }

    public void getProvinceData(){
        if (handleQueryProvinceDataModel != null){
            handleQueryProvinceDataModel.setActivity(activity);
            iShowChooseVIew.showloadProgress();
            handleQueryProvinceDataModel.loadProgress(new IHandleQueryProvinceDataModel.HandleQueryDataLoadListener() {
                @Override
                public void onCompleted(List<Province> provinceList) {
                    if(((provinceList)!=null)&&(provinceList.size() != 0)){
                        iShowChooseVIew.showProvinceData(provinceList);
                        iShowChooseVIew.closeloadProgress();
                    }else{
                        iShowChooseVIew.showProvinceError(activity.getString(R.string.nework_error));
                    }
                }
            });
        }
    }

    public void queryCitys(int provinceCode){

        if (handleQueryCityDataModel != null){
            handleQueryCityDataModel.setActivity(activity);
            handleQueryCityDataModel.setProvinceCode(provinceCode);
            iShowChooseVIew.showloadProgress();
            handleQueryCityDataModel.loadProgress(new IHandleQueryCityDataModel.HandleQueryDataLoadListener() {
                @Override
                public void onCompleted(List<City> cityList) {
                    if(((cityList)!=null)&&(cityList.size() != 0)){
                        iShowChooseVIew.showCityData(cityList);
                        iShowChooseVIew.closeloadProgress();
                    }else{
                        iShowChooseVIew.showCityError(activity.getString(R.string.nework_error));
                    }
                }
            });
        }

    }

    public void queryCountys(int provinceCode,int cityCode){

        if (handleQueryCountyDataModel != null){
            handleQueryCountyDataModel.setActivity(activity);
            handleQueryCountyDataModel.setProvinceCode(provinceCode);
            handleQueryCountyDataModel.setCityCode(cityCode);
            iShowChooseVIew.showloadProgress();
            handleQueryCountyDataModel.loadProgress(new IHandleQueryCountyDataModel.HandleQueryDataLoadListener() {
                @Override
                public void onCompleted(List<County> countyList) {
                    if(((countyList)!=null)&&(countyList.size() != 0)){
                        iShowChooseVIew.showCountyData(countyList);
                        iShowChooseVIew.closeloadProgress();
                    }else{
                        iShowChooseVIew.showCountyError(activity.getString(R.string.nework_error));
                    }
                }
            });
        }

    }


}
