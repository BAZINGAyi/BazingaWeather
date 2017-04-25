package com.bazinga.bazingaweather.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.WeatherActivity;


public class NotificationUtils {

    private static final int WEATHER_NOTIFICATION_ID = 3004;

    // Forecast: Sunny - High: 14°C Low 7°C
    public static void notifyUserOfNewWeather(Context context,int todayIcno,String notificationContent) {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(todayIcno)
                    .setContentTitle(context.getString(R.string.notificationTitle))
                    .setContentText(notificationContent)
                    .setAutoCancel(true);


            Intent detailIntentForToday = new Intent(context, WeatherActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

    }

}
