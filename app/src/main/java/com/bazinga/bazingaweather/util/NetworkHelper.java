package com.bazinga.bazingaweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bazinga on 2017/4/25.
 */

public class NetworkHelper {

    /**
     * 判断当前是否有网络连接
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable();
        }
        return false;
    }
}