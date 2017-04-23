package com.bazinga.bazingaweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by bazinga on 2017/4/23.
 */

public class WeatherDb extends DataSupport{
    private String date;
    private String info;
    private String max;
    private String min;
    private int code;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
