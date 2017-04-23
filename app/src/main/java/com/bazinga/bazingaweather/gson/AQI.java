package com.bazinga.bazingaweather.gson;

/**
 * Created by bazinga on 2017/4/1.
 */

public class AQI  {
    public AQICity getCity() {
        return city;
    }

    public void setCity(AQICity city) {
        this.city = city;
    }

    public AQICity city;

    public static class AQICity{

        public String aqi;

        public String pm25;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }
    }
}
