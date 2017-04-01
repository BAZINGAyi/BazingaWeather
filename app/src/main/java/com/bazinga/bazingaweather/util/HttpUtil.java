package com.bazinga.bazingaweather.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by bazinga on 2017/4/1.
 */

public class HttpUtil {

    public static void sendOkhttpRequest(String address, okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(address).build();

        Log.e("http", "sendOkhttpRequest: "+ address);

        client.newCall(request).enqueue(callback);
    }
}
