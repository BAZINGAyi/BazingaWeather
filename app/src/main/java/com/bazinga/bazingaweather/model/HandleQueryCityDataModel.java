package com.bazinga.bazingaweather.model;

import android.app.Activity;
import android.util.Log;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.Province;
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

public class HandleQueryCityDataModel implements IHandleQueryCityDataModel{

    public static String TAG = "HQCityDataModel";

    List<City> cityList;

    private Activity activity;

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    private int provinceCode;

    public void setProvinceCode(int provinceCode){
        this.provinceCode = provinceCode;
    }

    @Override
    public void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener) {

        queryCity(handleQueryDataLoadListener);

    }

    private void queryCity(HandleQueryDataLoadListener handleQueryDataLoadListener) {


        cityList = DataSupport.where("provinceid = ?", String.valueOf(provinceCode)).find(City.class);

        if (cityList.size() > 0) {

            handleQueryDataLoadListener.onCompleted(cityList);

        } else {

            queryFromServer(handleQueryDataLoadListener);
        }
    }

    private void queryFromServer(final HandleQueryDataLoadListener handleQueryDataLoadListener) {


        HttpUtil.sendOkhttpRequest(Constant.CITY_URL + provinceCode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"queryFromServer" + "查询市失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();

                boolean result = false;

                result = JSONUtility.handleCityResponse(responseText,provinceCode);

                if (result == true)

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryCity(handleQueryDataLoadListener);
                        }
                    });
            }
        });

    }

}
