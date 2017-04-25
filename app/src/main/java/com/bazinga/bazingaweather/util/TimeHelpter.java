package com.bazinga.bazingaweather.util;

/**
 * Created by bazinga on 2017/4/24.
 */

public class TimeHelpter {

    public static int getSeconds(int hour) {
        return hour * 1000 * 60 * 60;
    }
}
