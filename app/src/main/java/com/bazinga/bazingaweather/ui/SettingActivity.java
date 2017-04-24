package com.bazinga.bazingaweather.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bazinga.bazingaweather.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
