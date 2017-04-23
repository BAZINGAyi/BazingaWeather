package com.bazinga.bazingaweather.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.WeatherActivity;
import com.bazinga.bazingaweather.gson.Weather;
import com.bazinga.bazingaweather.util.Constant;
import com.bazinga.bazingaweather.util.HttpUtil;
import com.bazinga.bazingaweather.util.JSONUtility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bazinga on 2017/4/23.
 */

public class WeatherTask {

    public static void saveWeather(final Weather weather, Context context ,String responseText){

        if (weather != null && "ok".equals(weather.status)) {

            SharedPreferences.Editor editor = PreferenceManager.
                            getDefaultSharedPreferences(context).edit();

            editor.putString(context.getString(R.string.weatherData), responseText);

            editor.apply();

        }
    }

    public static void savePic( Context context ,String bingPic){

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(context.getString(R.string.picData), bingPic);

        editor.apply();
    }

    public static void saveWeatherId(Context context ,String weatherId) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(context.getString(R.string.weatherId), weatherId);

        editor.apply();
    }
}
