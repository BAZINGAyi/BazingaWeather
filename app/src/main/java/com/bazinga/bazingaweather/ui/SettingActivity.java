package com.bazinga.bazingaweather.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bazinga.bazingaweather.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setInternalSystemState();
        setContentView(R.layout.activity_setting);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setInternalSystemState() {

        if (Build.VERSION.SDK_INT >= 21) {

            View decorView = getWindow().getDecorView();

            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        }
    }
}
