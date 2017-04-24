package com.example.bazinga.bazingaweather;

import com.bazinga.bazingaweather.db.County;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bazinga on 2017/4/23.
 */

public class Test {
    public static void main(String[] args){

        County county = new County();
        county.setWeatherId("sd");
        county.setCityId(12);
        county.setCountyName("zhangyuwei");
        List<County> keys = new ArrayList<>();
        keys.add(county);


        Map<String,Object> maps = new HashMap<>();
        maps.put("wo",keys);

        List<County> li = maps.get("wo");

        System.out.println(li.get(0).getCountyName());

    }
}
