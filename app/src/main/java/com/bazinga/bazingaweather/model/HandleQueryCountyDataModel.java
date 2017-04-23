package com.bazinga.bazingaweather.model;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.util.Constant;
import com.bazinga.bazingaweather.util.HttpUtil;
import com.bazinga.bazingaweather.util.JSONUtility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by bazinga on 2017/4/23.
 */

public class HandleQueryCountyDataModel implements IHandleQueryCountyDataModel{

    public static String TAG = "HQCityDataModel";

    List<County> countyList;

    private Activity activity;

    private int cityCode;

    private int provinceCode;

    public void setProvinceCode(int provinceCode){
        this.provinceCode = provinceCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public void loadProgress(IHandleQueryCountyDataModel.HandleQueryDataLoadListener handleQueryDataLoadListener) {

        queryCounty(handleQueryDataLoadListener);

    }

    private void queryCounty(IHandleQueryCountyDataModel.HandleQueryDataLoadListener handleQueryDataLoadListener) {


        countyList = DataSupport.where("cityid = ?", String.valueOf(cityCode)).find(County.class);

        if (countyList.size() > 0) {

            handleQueryDataLoadListener.onCompleted(countyList);

        } else {

            queryFromServer(handleQueryDataLoadListener);
        }
    }

    private void queryFromServer(final IHandleQueryCountyDataModel.HandleQueryDataLoadListener handleQueryDataLoadListener) {


        HttpUtil.sendOkhttpRequest(Constant.COUNTY_URL + provinceCode + "/" + cityCode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"queryFromServer" + "查询市失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();

                boolean result = false;

                result = JSONUtility.handleCountyResponse(responseText,cityCode);

                if (result == true)

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryCounty(handleQueryDataLoadListener);
                        }
                    });
            }
        });

    }

}
