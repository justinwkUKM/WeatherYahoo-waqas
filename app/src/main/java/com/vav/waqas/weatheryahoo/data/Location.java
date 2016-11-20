package com.vav.waqas.weatheryahoo.data;

import org.json.JSONObject;

/**
 * Created by haslina on 4/22/2016.
 */
public class Location implements JSONPopulator {

   private  String city;
    private  String country;

    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }


    @Override
    public void populate(JSONObject data) {
    city = data.optString("city");
        country = data.optString("country");
    }
}
