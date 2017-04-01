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

import com.bazinga.bazingaweather.db.City;
import com.bazinga.bazingaweather.db.County;
import com.bazinga.bazingaweather.db.Province;
import com.bazinga.bazingaweather.util.HttpUtil;
import com.bazinga.bazingaweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
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

public class ChooseFragment extends Fragment {

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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentLevel == LEVEL_PROVINCE) {

                    selectedProvince = provinceList.get(position);

                    queryCities();

                } else if (currentLevel == LEVEL_CITY) {

                    selectedCity = cityList.get(position);

                    queryCounties();

                } else if (currentLevel == LEVEL_COUNTY) {

                    String weatherId = countyList.get(position).getWeatherId();

                    if (getActivity() instanceof MainActivity) {

                        // Intent intent = new Intent(getActivity(), WeatherActivity.class);

                        //  intent.putExtra("weather_id", weatherId);

                        //  startActivity(intent);

                        //  getActivity().finish();
                        Intent intent = new Intent();

                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (currentLevel == LEVEL_COUNTY) {

                    queryCities();

                } else if (currentLevel == LEVEL_CITY) {

                    queryProvinces();
                }
            }
        });

        queryProvinces();
    }

    /**
     * 先从数据库查询省的信息，没有查到再去请求服务器
     */
    private void queryProvinces() {

        titleText.setText("中国");

        backButton.setVisibility(View.GONE);

        provinceList = DataSupport.findAll(Province.class);

        if (provinceList.size() > 0) {

            dataList.clear();

            for (Province province : provinceList) {

                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            currentLevel = LEVEL_PROVINCE;

        } else {

            String address = "http://guolin.tech/api/china";

            queryFromServer(address, "province");
        }
    }

    /**
     * 先从数据库查询市的信息，没有查到再去请求服务器
     */

    private void queryCities() {

        titleText.setText(selectedProvince.getProvinceName());

        backButton.setVisibility(View.VISIBLE);

        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList.size() > 0) {

            dataList.clear();

            for (City city : cityList) {

                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            currentLevel = LEVEL_CITY;

        } else {

            int provinceCode = selectedProvince.getProvinceCode();

            String address = "http://guolin.tech/api/china/" + provinceCode;

            queryFromServer(address, "city");
        }
    }

    /**
     * 先从数据库查询县的信息，没有查到再去请求服务器
     */

    private void queryCounties() {

        titleText.setText(selectedCity.getCityName());

        backButton.setVisibility(View.VISIBLE);

        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);

        if (countyList.size() > 0) {

            dataList.clear();

            for (County county : countyList) {

                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            currentLevel = LEVEL_COUNTY;

        } else {

            int provinceCode = selectedProvince.getProvinceCode();

            int cityCode = selectedCity.getCityCode();

            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;

            queryFromServer(address, "county");
        }
    }

    private void queryFromServer(String address, final String type) {

        showProgressDialog();

        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                // 注意这里是开启的新线程，更新 ui 需要在主线程中

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeProgressDialog();

                        Toast.makeText(getContext(), "Honey，加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();

                boolean result = false;

                if ("province".equals(type)) {

                    result = Utility.handleProvinceResponse(responseText);

                } else if ("city".equals(type)) {

                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());

                } else if ("county".equals(type)) {

                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());

                }

                // 如果返回成功，说明数据已经加载到数据里了
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            closeProgressDialog();

                            if ("province".equals(type)) {

                                queryProvinces();

                            } else if ("city".equals(type)) {

                                queryCities();

                            } else if ("county".equals(type)) {

                                queryCounties();

                            }
                        }
                    });
                }

            }
        });

    }


    private void showProgressDialog() {


        if (progressDialog == null) {

            progressDialog = new ProgressDialog(getActivity());

            progressDialog.setMessage("正在加载...");

            progressDialog.setCanceledOnTouchOutside(false);

        }

        progressDialog.show();
    }

    private void closeProgressDialog() {

        if (progressDialog != null) {

            progressDialog.dismiss();

        }
    }
}