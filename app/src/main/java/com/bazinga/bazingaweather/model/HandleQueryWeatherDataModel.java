package com.bazinga.bazingaweather.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.WeatherActivity;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.gson.Weather;
import com.bazinga.bazingaweather.sync.WeatherTask;
import com.bazinga.bazingaweather.util.Constant;
import com.bazinga.bazingaweather.util.HttpUtil;
import com.bazinga.bazingaweather.util.JSONUtility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by bazinga on 2017/4/23.
 */

public class HandleQueryWeatherDataModel implements IHandleQueryWeatherDataModel{

    public static String TAG = "HQWDataModel";

    private Boolean isSyncWeather = false;

    private String weatherId;

    private Boolean isSyncPic = false;

    private Activity activity;

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setSyncWeather(Boolean syncWeather) {
        isSyncWeather = syncWeather;
    }


    public void setSyncPic(Boolean syncPic) {
        isSyncPic = syncPic;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public void loadProgress(HandleQueryDataLoadListener handleQueryDataLoadListener) {

        queryWeather(handleQueryDataLoadListener);

    }

    @Override
    public void loadPicProgress(HandleQueryPicLoadListener handleQueryPicLoadListener) {
        queryPic(handleQueryPicLoadListener);
    }

    private void queryPic(final HandleQueryPicLoadListener handleQueryPicLoadListener) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        final String pic = prefs.getString(activity.getString(R.string.picData),null);

        if (pic != null && isSyncPic == false ){

            handleQueryPicLoadListener.onCompleted(pic);

        }else{

            HttpUtil.sendOkhttpRequest(Constant.PIC_URL, new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String bingPic = response.body().string();

                    if(bingPic != null){

                        WeatherTask.savePic(activity,bingPic);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleQueryPicLoadListener.onCompleted(bingPic);
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void queryWeather(final HandleQueryDataLoadListener handleQueryDataLoadListener) {

       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

       // final String weatherString = prefs.getString(activity.getString(R.string.weatherData),null);

      //  final Weather weather = WeatherTask.getWeaherDbAndSharef(activity);

       final Map<String ,Object> weather = WeatherTask.getDataTest(activity);

        if (weather.size() != 0 && isSyncWeather == false && weatherId == null){

            new Thread(new Runnable() {
                @Override
                public void run() {

                   // Weather weather = JSONUtility.handleWeatherResponse(weatherString);


                    handleQueryDataLoadListener.onCompleted(weather);

                }
            }).start();

        }else{

            String weatherUrl = Constant.WEATHER_URL +
                    weatherId + Constant.KEY;

            HttpUtil.sendOkhttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseText = response.body().string();

                    final Weather weather = JSONUtility.handleWeatherResponse(responseText);

                    if (weather != null && "ok".equals(weather.status)) {

                        WeatherTask.saveWeatherForeastDbandSharef(weather,activity);

                        //WeatherTask.saveWeather(weather,activity,responseText);

                        WeatherTask.saveWeatherId(activity,weatherId);

                        final Map<String ,Object> weatherinfo = WeatherTask.getDataTest(activity);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleQueryDataLoadListener.onCompleted(weatherinfo);
                            }
                        });

                    }
                }
            });
        }

    }


}
