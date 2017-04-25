package com.bazinga.bazingaweather.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.bazinga.bazingaweather.R;
import com.bazinga.bazingaweather.sync.WeatherJobService;
import com.bazinga.bazingaweather.sync.WeatherSyncUtils;

/**
 * Created by bazinga on 2017/4/24.
 */

public class SettingFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (null != preference) {

            if (!(preference instanceof CheckBoxPreference)) {

                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
            }
        }

    }

    private void setPreferenceSummary(Preference preference, Object value) {

        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;

            int prefIndex = listPreference.findIndexOfValue(stringValue);

            if (prefIndex >= 0) {

                preference.setSummary(listPreference.getEntries()[prefIndex]);

                String datas = String.valueOf(listPreference.getEntries()[prefIndex]);

                if(getString(R.string.pref_label_time_one).equals(datas)){

                    WeatherSyncUtils.setIsSyncUpdateService(false);

                    WeatherSyncUtils.cancel();

                } else{

                    WeatherSyncUtils.setIsSyncUpdateService(true);

                    WeatherSyncUtils.cancel();

                    WeatherSyncUtils.scheduleJobWeatherSync(getContext());
                }

            }
        }else if(preference instanceof CheckBoxPreference){

            CheckBoxPreference  checkBoxPreference= (CheckBoxPreference)preference;

            Boolean datas = checkBoxPreference.isChecked();

            if(true == datas){
                WeatherJobService.setisSendNotification(true);
            }else
                WeatherJobService.setisSendNotification(false);
        }
    }

    @Override
    public void onStop() {

        super.onStop();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {

        super.onStart();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


}
