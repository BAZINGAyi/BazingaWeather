package com.bazinga.bazingaweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.bazingaweather.base.MVPBaseActivity;
import com.bazinga.bazingaweather.gson.Forecast;
import com.bazinga.bazingaweather.gson.Weather;
import com.bazinga.bazingaweather.presenter.HandleWeatherDataPresenter;
import com.bazinga.bazingaweather.sync.WeatherSyncUtils;
import com.bazinga.bazingaweather.util.NotificationUtils;
import com.bazinga.bazingaweather.util.ResourceHelper;
import com.bazinga.bazingaweather.view.IShowWeatherVIew;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends MVPBaseActivity<IShowWeatherVIew,HandleWeatherDataPresenter>
    implements IShowWeatherVIew {

    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @BindView(R.id.nav_button)
    Button navButton;
    @BindView(R.id.title_city)
    TextView titleCity;
    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;
    @BindView(R.id.degree_text)
    TextView degreeText;
    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;
    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;
    @BindView(R.id.aqi_text)
    TextView aqiText;
    @BindView(R.id.pm25_text)
    TextView pm25Text;
    @BindView(R.id.comfort_text)
    TextView comfortText;
    @BindView(R.id.car_wash_text)
    TextView carWashText;
    @BindView(R.id.sport_text)
    TextView sportText;
    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private  String mWeatherId = "";

    public static Boolean isFirstStartService = true;

    public static String TAG = "WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setInternalSystemState();

        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        mPresenter.setActivity(this);

        mPresenter.attach(this);

        doWeatherlogic();

        if(isFirstStartService == true){
            Log.i(TAG, "onCreate: "  + isFirstStartService);
            isFirstStartService = false;
            WeatherSyncUtils.scheduleJobWeatherSync(this);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);

                final String weatherString = prefs.getString(getString(R.string.weatherId),null);

                if (weatherString != null)
                    mPresenter.queryWeather(weatherString,true);

                mPresenter.queryPic(true);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    protected HandleWeatherDataPresenter createPresenter() {
        return new HandleWeatherDataPresenter();
    }

    private void doWeatherlogic(){

        mWeatherId = getIntent().getStringExtra(getString(R.string.weatherId));

        if (mWeatherId != null)
            mPresenter.queryWeather(mWeatherId,true);
        else
            mPresenter.queryWeather(null,false);

        mPresenter.queryPic(false);
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


    @Override
    public void showWeatherData(Map<String,Object> weather) {
        if(weather !=null){

            String cityName =(String)weather.get(getString(R.string.data_cityName));

            String updateTime = (String)weather.get(getString(R.string.data_updateTime));

            String degree = (String)weather.get(getString(R.string.data_temperature)) + "℃";

            String weatherInfo = (String)weather.get(getString(R.string.data_weatherInfo));

            titleCity.setText(cityName);

            titleUpdateTime.setText(updateTime.split(" ")[1]);

            degreeText.setText(degree);

            weatherInfoText.setText(weatherInfo);

            forecastLayout.removeAllViews();

            int itemLayout = 0;

            for (Forecast forecast : (List<Forecast>)weather.get(getString(R.string.data_ForeastInfo))) {

                View view;

                if(itemLayout != 0){
                    view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                            forecastLayout, false);
                }else{
                    itemLayout++;
                    view = LayoutInflater.from(this).inflate(R.layout.forecast_today_item,
                            forecastLayout, false);
                }

                TextView dateText = (TextView) view.findViewById(R.id.date);
                TextView infoText = (TextView) view.findViewById(R.id.weather_description);
                TextView maxText = (TextView) view.findViewById(R.id.high_temperature);
                TextView minText = (TextView) view.findViewById(R.id.low_temperature);
                ImageView img = (ImageView) view.findViewById(R.id.weather_icon);


                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);
                img.setImageResource(ResourceHelper.getResourceId(forecast.more.code));

                maxText.invalidate(forecast.date);

                forecastLayout.addView(view);
            }

            if(weather.get(getString(R.string.data_aqi)) == null){
                aqiText.setText(getString(R.string.no_value));
                pm25Text.setText(getString(R.string.no_value));
            }else{
                aqiText.setText((String)weather.get(getString(R.string.data_aqi)));
                pm25Text.setText((String)weather.get(getString(R.string.data_pm25)));
            }

            String comfort = getString(R.string.flag_comfortable)+ weather.get(getString(R.string.data_comfortable));

            String carWash = getString(R.string.flag_cleanCar) + weather.get(getString(R.string.data_carWash));

            String sport = getString(R.string.flag_sport)+  weather.get(getString(R.string.data_sport));

            comfortText.setText(comfort);

            carWashText.setText(carWash);

            sportText.setText(sport);

            weatherLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void showWeatherError(String msg) {
        Toast.makeText(WeatherActivity.this, getString(R.string.weather_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPic(String pic) {
        Glide.with(WeatherActivity.this).load(pic).into(bingPicImg);
    }

    @Override
    public void showPicError(String msg) {
        Toast.makeText(WeatherActivity.this, getString(R.string.background_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showloadProgress() {

    }

    @Override
    public void closeloadProgress() {
        swipeRefresh.setRefreshing(false);
    }

    public void frgmentQureyWeather(String weatherId){

        drawerLayout.closeDrawers();

        swipeRefresh.setRefreshing(true);

        mPresenter.queryWeather(weatherId,true);
    }
}
