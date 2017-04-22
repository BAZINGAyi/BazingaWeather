package com.bazinga.bazingaweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;

public class MainActivity extends AppCompatActivity {

    public static int once = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getString("weather", null) != null) {

            Intent intent = new Intent(this, WeatherActivity.class);

            startActivity(intent);

            finish();
        }


    }
}
