package com.example.bazinga.bazingaweather;

import com.bazinga.bazingaweather.db.County;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bazinga on 2017/4/23.
 */

public class Test {
    public static void main(String[] args){
        County county = new County();
        county.setWeatherId("sd");
        county.setCityId(12);
        List<Object> keys = new ArrayList<>();
        keys.add(county);

    }
}
