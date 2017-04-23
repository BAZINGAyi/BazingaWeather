package com.bazinga.bazingaweather.sync;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.db.WeatherDb;
import com.bazinga.bazingaweather.gson.AQI;
import com.bazinga.bazingaweather.gson.Basic;
import com.bazinga.bazingaweather.gson.Forecast;
import com.bazinga.bazingaweather.gson.Now;
import com.bazinga.bazingaweather.gson.Suggestion;
import com.bazinga.bazingaweather.gson.Weather;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by bazinga on 2017/4/23.
 */

public class WeatherTask {

    public static void saveWeather(final Weather weather, Context context , String responseText){

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

    public static void saveWeatherForeastDbandSharef(Weather weather,Context context){

        if (weather == null)
            return;

        WeatherDb.deleteAll("WeatherDb");

        for (Forecast forecast : weather.forecastList) {

            WeatherDb w = new WeatherDb();
            w.setInfo(forecast.more.info);
            w.setMax(forecast.temperature.max);
            w.setMin(forecast.temperature.min);
            w.setCode(forecast.more.code);
            w.setDate(forecast.date);
            w.save();
        }

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(context.getString(R.string.data_cityName),weather.basic.cityName);
        editor.putString(context.getString(R.string.data_updateTime),weather.basic.update.updateTime);
        editor.putString(context.getString(R.string.data_temperature),weather.now.temperature);
        editor.putString(context.getString(R.string.data_weatherInfo),weather.now.more.info);


        if(weather.aqi != null){
            editor.putString(context.getString(R.string.data_aqi),weather.aqi.city.aqi);
            editor.putString(context.getString(R.string.data_pm25),weather.aqi.city.pm25);
        }

        editor.putString(context.getString(R.string.data_comfortable),weather.suggestion.comfort.info);
        editor.putString(context.getString(R.string.data_carWash),weather.suggestion.carWash.info);
        editor.putString(context.getString(R.string.data_sport),weather.suggestion.sport.info);

        editor.apply();
    }

    public static Weather getWeaherDbAndSharef(Context context) {

        Weather weather = new Weather();

        Basic.Update update = new Basic.Update();

        Basic basic = new Basic();

        Now now = new Now();

        Suggestion suggestion = new Suggestion();

        AQI aqi = new AQI();

        AQI.AQICity aqiCity = new AQI.AQICity();

        Basic.Update update1 = new Basic.Update();

        Now.More more = new Now.More();

        Suggestion.Comfort comfort = new Suggestion.Comfort();

        Suggestion.Sport sport = new Suggestion.Sport();

        Suggestion.CarWash carWash = new Suggestion.CarWash();

        weather.basic = basic;

        weather.basic.update = update;

        weather.now = now;

        weather.suggestion = suggestion;

        weather.aqi = aqi;

        weather.aqi.city = aqiCity;

        weather.basic.update = update1;

        weather.now.more = more;

        weather.suggestion.comfort = comfort;

        weather.suggestion.sport = sport;

        weather.suggestion.carWash = carWash;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        weather.basic.setCityName(prefs.getString(context.getString(R.string.data_cityName),null));
        weather.basic.update.setUpdateTime(prefs.getString(context.getString(R.string.data_updateTime),null));
        weather.now.setTemperature(prefs.getString(context.getString(R.string.data_temperature),null));
        weather.now.more.setInfo(prefs.getString(context.getString(R.string.data_weatherInfo),null));

        String api = prefs.getString(context.getString(R.string.data_aqi),null);
        String pm25 = prefs.getString(context.getString(R.string.data_pm25),null);

        if(api != null && pm25 != null){
            weather.aqi.city.setAqi(prefs.getString(context.getString(R.string.data_aqi),null));
            weather.aqi.city.setPm25(prefs.getString(context.getString(R.string.data_pm25),null));
        }

        weather.suggestion.comfort.setInfo(prefs.getString(context.getString(R.string.data_comfortable),null));
        weather.suggestion.carWash.setInfo(prefs.getString(context.getString(R.string.data_carWash),null));
        weather.suggestion.sport.setInfo(prefs.getString(context.getString(R.string.data_sport),null));

        List<WeatherDb> lists = WeatherDb.findAll(WeatherDb.class);

        List<Forecast> forecasrList= new ArrayList<>();

        for (WeatherDb forecast : lists) {

            Forecast f = new Forecast();
            f.setDate(forecast.getDate());

            Forecast.Temperature t = new Forecast.Temperature();
            t.setMax(forecast.getMax());
            t.setMin(forecast.getMin());

            Forecast.More m = new Forecast.More();
            m.setInfo(forecast.getInfo());
            m.setCode(forecast.getCode());

            f.setTemperature(t);

            f.setMore(m);

            forecasrList.add(f);

        }

        weather.setForecastList(forecasrList);

        return weather;
    }
}
