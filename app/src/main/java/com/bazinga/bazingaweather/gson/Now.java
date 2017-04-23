package com.bazinga.bazingaweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bazinga on 2017/4/1.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public static class More{
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @SerializedName("txt")
        public  String info;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }
}
