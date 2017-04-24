package com.bazinga.bazingaweather.presenter;

import android.app.Activity;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.gson.Weather;
import com.bazinga.bazingaweather.model.HandleQueryCityDataModel;
import com.bazinga.bazingaweather.model.HandleQueryCountyDataModel;
import com.bazinga.bazingaweather.model.HandleQueryProvinceDataModel;
import com.bazinga.bazingaweather.model.HandleQueryWeatherDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryCityDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryCountyDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryProvinceDataModel;
import com.bazinga.bazingaweather.model.IHandleQueryWeatherDataModel;
import com.bazinga.bazingaweather.view.IShowChooseVIew;
import com.bazinga.bazingaweather.view.IShowWeatherVIew;

import java.util.List;
import java.util.Map;

/**
 * Created by bazinga on 2017/4/23.
 */

public class HandleWeatherDataPresenter extends BasePresenter<IShowWeatherVIew> {

    private Activity activity;

    HandleQueryWeatherDataModel handleQueryWeatherDataModel= new HandleQueryWeatherDataModel();

    IShowWeatherVIew iShowWeatherVIew = null;


    public void setActivity(Activity activity){
       this.activity = activity;
    }


    public void attach(IShowWeatherVIew iShowWeatherVIew){
        this.iShowWeatherVIew = iShowWeatherVIew;
    }



    public void queryWeather(String weatherId,Boolean syncWeather){

        if (handleQueryWeatherDataModel != null){
            handleQueryWeatherDataModel.setActivity(activity);
            handleQueryWeatherDataModel.setWeatherId(weatherId);
            handleQueryWeatherDataModel.setSyncWeather(syncWeather);
            iShowWeatherVIew.showloadProgress();
            handleQueryWeatherDataModel.loadProgress(new IHandleQueryWeatherDataModel.
                    HandleQueryDataLoadListener(){
                @Override
                public void onCompleted(Map<String,Object> weather) {
                    if((weather)!=null){
                        iShowWeatherVIew.showWeatherData(weather);
                        iShowWeatherVIew.closeloadProgress();
                    }else{
                        iShowWeatherVIew.showWeatherError(activity.getString(R.string.nework_error));
                    }
                }
            });
        }
    }

    public void queryPic(Boolean sycPic){

        if (handleQueryWeatherDataModel != null){
            handleQueryWeatherDataModel.setActivity(activity);
            handleQueryWeatherDataModel.setSyncPic(sycPic);
            handleQueryWeatherDataModel.loadPicProgress(new IHandleQueryWeatherDataModel.HandleQueryPicLoadListener() {
                @Override
                public void onCompleted(String pic) {
                    if((pic)!=null){
                        iShowWeatherVIew.showPic(pic);
                        iShowWeatherVIew.closeloadProgress();
                    }else{
                        iShowWeatherVIew.showPicError(activity.getString(R.string.nework_error));
                    }
                }
            });
        }

    }

}
