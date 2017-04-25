package com.bazinga.bazingaweather.sync;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.db.WeatherDb;
import com.bazinga.bazingaweather.gson.Weather;
import com.bazinga.bazingaweather.util.Constant;
import com.bazinga.bazingaweather.util.HttpUtil;
import com.bazinga.bazingaweather.util.JSONUtility;
import com.bazinga.bazingaweather.util.NotificationUtils;
import com.bazinga.bazingaweather.util.ResourceHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by bazinga on 2017/4/24.
 */

public class WeatherJobService extends JobService {

    public static Boolean isSendNotification = true;

    @Override
    public boolean onStartJob(JobParameters params) {

        updateWeather();

        updateBingPic();

        if(isSendNotification)

            sendNotification();

        return false;
    }

    public static void setisSendNotification(Boolean isSendNotification){
        WeatherJobService.isSendNotification = isSendNotification;
    }

    private void sendNotification() {

         new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                WeatherDb weather  = WeatherDb.findFirst(WeatherDb.class);

                if(weather != null){

                    String maxValue =  weather.getMax();

                    String minValue =  weather.getMin();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherJobService.this);

                    String info = prefs.getString(getString(R.string.data_weatherInfo), null);

                    // Forecast: Sunny - High: 14°C Low 7°C

                    String cotent = info+" - 最高温：" + maxValue + "°C 最低温 " + minValue + "°C";

                    int code = weather.getCode();

                    int id = ResourceHelper.getResourceId(code);

                    NotificationUtils.notifyUserOfNewWeather(WeatherJobService.this,id,cotent);
                }
                return null;
            }
        }.execute();



    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    /**
     * 更新天气信息。
     */
    private void updateWeather(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final String weatherId =  prefs.getString(getString(R.string.weatherId), null);

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

                    WeatherTask.saveWeatherForeastDbandSharef(weather,getBaseContext());

                    WeatherTask.saveWeatherId(getBaseContext(),weatherId);


                }
            }
        });
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {

        HttpUtil.sendOkhttpRequest(Constant.PIC_URL, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bingPic = response.body().string();

                if(bingPic != null){

                    WeatherTask.savePic(getBaseContext(),bingPic);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


}
