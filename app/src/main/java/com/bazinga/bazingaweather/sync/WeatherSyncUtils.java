package com.bazinga.bazingaweather.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.util.Constant;
import com.bazinga.bazingaweather.util.TimeHelpter;

/**
 * Created by bazinga on 2017/4/24.
 */

public class WeatherSyncUtils {

    private static int DefalultJoBId = 123;

    private static JobScheduler scheduler;

    public static String TAG = "WeatherSyncUtils";

    public static Boolean isSyncUpdateService = true;

    public static void scheduleJobWeatherSync(@NonNull final Context context) {

        if (isSyncUpdateService == true){

            ComponentName jobService = new ComponentName(context, WeatherJobService.class);;

            scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            int jobId;

            jobId = DefalultJoBId;
            JobInfo jobInfo = new JobInfo.Builder(jobId, jobService)
                    .setPeriodic(getUpdateTime(context))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)// 设置网络条件
                    .setRequiresCharging(true)// 设置是否充电的条件
                    .setRequiresDeviceIdle(false)// 设置手机是否空闲的条件
                    .build();

            scheduler.schedule(jobInfo);

        }

    }

    public static void setIsSyncUpdateService(Boolean isSyncUpdateService) {
        WeatherSyncUtils.isSyncUpdateService = isSyncUpdateService;
    }

    private static int getUpdateTime(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String updateTime =  prefs.getString(context.getString
                (R.string.pref_serviceUpdateTime_key),null);
        Log.e(TAG, "getUpdateTime: ");
        //  只有进入设置后才会有值
        if(updateTime != null){
            scheduler.cancel(DefalultJoBId);
            Log.e(TAG, "getUpdateTime: 2");
            return TimeHelpter.getSeconds(Integer.valueOf(updateTime));
        } else{
            Log.e(TAG, "getUpdateTime: 1");
            return TimeHelpter.getSeconds(Constant.DEFAULT_UPDATETIME);
        }
    }

    public static void cancel(){
        if (scheduler != null)
            scheduler.cancel(DefalultJoBId);
    }
}
