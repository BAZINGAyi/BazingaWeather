package com.bazinga.bazingaweather.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bazinga.bazingaweather.db.County;
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

public class HandleQueryProvinceDataModel implements IHandleQueryProvinceDataModel{

    public static String TAG = "HQProvinceDataModel";

    List<Province> provinceList;

    private Activity activity;

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener) {

        queryProvince(handleQueryDataLoadListener);

    }


    private void queryProvince(final HandleQueryDataLoadListener handleQueryDataLoadListener) {


        provinceList = DataSupport.findAll(Province.class);

        if (provinceList.size() > 0) {

            handleQueryDataLoadListener.onCompleted(provinceList);

        }else{

            queryFromServer(handleQueryDataLoadListener);
        }


    }

    private void queryFromServer(final HandleQueryDataLoadListener handleQueryDataLoadListener) {


        HttpUtil.sendOkhttpRequest(Constant.PROVINCE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"queryFromServer" + "查询省失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();

                boolean result = false;

                result = JSONUtility.handleProvinceResponse(responseText);

                if (result == true && activity != null)

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryProvince(handleQueryDataLoadListener);
                        }
                    });
            }
        });

    }

}
