package com.bazinga.bazingaweather.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.bazinga.bazingaweather.R;

/**
 * Created by bazinga on 2017/4/23.
 */

public class ResourceHelper {

    public static int getResourceId1(Context context,int code){
        String imgs = "w" + String.valueOf(code);
        //得到application对象
        ApplicationInfo appInfo = context.getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"drawable" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        int resID = context.getResources().getIdentifier(imgs, "drawable", appInfo.packageName);

        if(resID == 0)
            return R.drawable.w100;

        return resID;
    }

    public static int getResourceId(int weatherId){
        if (weatherId >= 100 && weatherId < 200) {
            if (weatherId == 100)
                return R.drawable.w100;
            else
                return R.drawable.w101;
        } else if (weatherId >= 200 && weatherId < 300) {
            return R.drawable.w200;
        } else if (weatherId >= 300 && weatherId < 400) {

            if(weatherId == 300|| weatherId == 305 || weatherId ==309 )
                return R.drawable.w300;
            else if(weatherId >300|| weatherId <305)
                return R.drawable.w302;
            else return R.drawable.w301;

        } else if (weatherId >= 400 && weatherId <= 500) {
            return R.drawable.w400;
        } else if (weatherId >= 500 && weatherId <= 600) {
            return R.drawable.w500;
        }else return R.drawable.w900;
    }
}
