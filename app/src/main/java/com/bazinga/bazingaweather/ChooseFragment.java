package com.bazinga.bazingaweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.bazingaweather.base.MVpBaseFragment;
import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.presenter.HandleQueryDataPresenter;

import com.bazinga.bazingaweather.view.IShowChooseVIew;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bazinga on 2017/4/1.
 */

public class ChooseFragment extends MVpBaseFragment<IShowChooseVIew,HandleQueryDataPresenter> implements
        IShowChooseVIew {

    @BindView(R.id.back_button)
    Button backButton;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.title_text)
    TextView titleText;

    private ArrayAdapter<String> adapter;


    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    // 用于显示查询到的省市县的信息
    private List<String> dataList = new ArrayList<>();

    // 用于查询数据库的数据
    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;

    private City selectedCity;

    private int currentLevel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area, container, false);

        ButterKnife.bind(this, view);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);

        mPresenter.attach(this);

        mPresenter.setActivity(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.getProvinceData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentLevel == LEVEL_PROVINCE) {

                    selectedProvince = provinceList.get(position);

                    mPresenter.queryCitys(selectedProvince.getProvinceCode());

                } else if (currentLevel == LEVEL_CITY) {

                    selectedCity = cityList.get(position);

                    mPresenter.queryCountys(selectedProvince.getProvinceCode(),selectedCity.getCityCode());

                } else if (currentLevel == LEVEL_COUNTY) {

                    String weatherId = countyList.get(position).getWeatherId();

                    if (getActivity() instanceof MainActivity) {

                          Intent intent = new Intent(getActivity(), WeatherActivity.class);

                          intent.putExtra(getString(R.string.weatherId), weatherId);

                          startActivity(intent);

                          getActivity().finish();

                    }else if(getActivity() instanceof  WeatherActivity){

                        WeatherActivity activity = (WeatherActivity) getActivity();

                        activity.frgmentQureyWeather(weatherId);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (currentLevel == LEVEL_COUNTY) {

                    mPresenter.queryCitys(selectedProvince.getProvinceCode());

                } else if (currentLevel == LEVEL_CITY) {

                    mPresenter.getProvinceData();
                }
            }
        });
    }


    @Override
    protected HandleQueryDataPresenter createPresenter() {
        return new HandleQueryDataPresenter();
    }


    @Override
    public void showProvinceData(List<Province> provinceList) {
        titleText.setText(getString(R.string.state));
        backButton.setVisibility(View.GONE);
        this.dataList.clear();
        this.provinceList = provinceList;
        for (Province province : provinceList) {
            dataList.add(province.getProvinceName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_PROVINCE;
    }

    @Override
    public void showCityData(List<City> cityList) {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        this.dataList.clear();
        this.cityList = cityList;
        for (City city : cityList) {
            dataList.add(city.getCityName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_CITY;

    }

    @Override
    public void showCountyData(List<County> countyList) {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        this.dataList.clear();
        this.countyList = countyList;
        for (County county : countyList) {
            dataList.add(county.getCountyName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_COUNTY;
    }

    @Override
    public void showProvinceError(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCityError(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCountyError(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showloadProgress() {

        if (progressDialog == null) {

            progressDialog = new ProgressDialog(getActivity());

            progressDialog.setMessage(getString(R.string.loadText));

            progressDialog.setCanceledOnTouchOutside(false);

        }

        progressDialog.show();
    }

    @Override
    public void closeloadProgress() {

        if (progressDialog != null) {

            progressDialog.dismiss();

        }
    }
}