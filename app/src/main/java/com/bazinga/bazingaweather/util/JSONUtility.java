package com.bazinga.bazingaweather.util;

import android.text.TextUtils;

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bazinga on 2017/4/1.
 */

public class JSONUtility {

    /** 解析省级数据
     *
     */

    public static boolean handleProvinceResponse(String reponse){

        if(!TextUtils.isEmpty(reponse)){

            try{

                JSONArray allProvinces = new JSONArray(reponse);

                for (int i = 0; i < allProvinces.length(); i++){

                    JSONObject provinceObject = allProvinces.getJSONObject(i);

                    Province province = new Province();

                    province.setProvinceName(provinceObject.getString("name"));

                    province.setProvinceCode(provinceObject.getInt("id"));

                    province.save();
                }

                return true;

            }catch (JSONException e){

                e.printStackTrace();
            }


        }

        return false;

    }

    /** 解析市级数据
     *
     */

    public static boolean handleCityResponse(String reponse, int provinceId){

        if(!TextUtils.isEmpty(reponse)){

            try {

                JSONArray allCities = new JSONArray(reponse);

                for(int i = 0; i < allCities.length(); i++){

                    JSONObject cityObject = allCities.getJSONObject(i);

                    City city = new City();

                    city.setCityName(cityObject.getString("name"));

                    city.setCityCode(cityObject.getInt("id"));

                    city.setProvinceId(provinceId);

                    city.save();
                }

                // api 接口有问题 少这个数据顾手动插入
                if(provinceId == 12 ){

                    City city = new City();
                    city.setCityName("承德");
                    city.setProvinceId(12);
                    city.setCityCode(1000);
                    city.save();

                    handleInsertCountryData();
                }

                return true;

            }catch (JSONException e){

                e.printStackTrace();
            }

        }

        return false;
    }


    /**
     * 解析县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {

            try {

                JSONArray allCounties = new JSONArray(response);

                for (int i = 0; i < allCounties.length(); i++) {

                    JSONObject countyObject = allCounties.getJSONObject(i);

                    County county = new County();

                    county.setCountyName(countyObject.getString("name"));

                    county.setWeatherId(countyObject.getString("weather_id"));

                    county.setCityId(cityId);

                    county.save();
                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");

            String weatherContent = jsonArray.getJSONObject(0).toString();

            return new Gson().fromJson(weatherContent, Weather.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void handleInsertCountryData() {

        County county13 = new County();
        county13.setCityId(1000);
        county13.setCountyName("双桥");
        county13.setWeatherId("CN101090401");
        county13.save();

        County county = new County();
        county.setCityId(1000);
        county.setCountyName("承德");
        county.setWeatherId("CN101090402");
        county.save();

        County county2 = new County();
        county2.setCityId(1000);
        county2.setCountyName("承德县");
        county2.setWeatherId("CN101090403");
        county2.save();

        County county3 = new County();
        county3.setCityId(1000);
        county3.setCountyName("兴隆");
        county3.setWeatherId("CN101090404");
        county3.save();

        County county4 = new County();
        county4.setCityId(1000);
        county4.setCountyName("平泉");
        county4.setWeatherId("CN101090405");
        county4.save();

        County county5 = new County();
        county5.setCityId(1000);
        county5.setCountyName("滦平");
        county5.setWeatherId("CN101090406");
        county5.save();


        County county6 = new County();
        county6.setCityId(1000);
        county6.setCountyName("隆化");
        county6.setWeatherId("CN101090407");
        county6.save();

        County county7 = new County();
        county7.setCityId(1000);
        county7.setCountyName("丰宁");
        county7.setWeatherId("CN101090408");
        county7.save();

        County county8 = new County();
        county8.setCityId(1000);
        county8.setCountyName("宽城");
        county8.setWeatherId("CN101090409");
        county8.save();

        County county9 = new County();
        county9.setCityId(1000);
        county9.setCountyName("围场");
        county9.setWeatherId("CN101090410");
        county9.save();


        County county10 = new County();
        county10.setCityId(1000);
        county10.setCountyName("双滦");
        county10.setWeatherId("CN101090402");
        county10.save();

        County county11 = new County();
        county11.setCityId(1000);
        county11.setCountyName("鹰手营子矿");
        county11.setWeatherId("CN101090402");
        county11.save();

    }
}
