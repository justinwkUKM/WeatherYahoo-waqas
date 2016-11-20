package com.vav.waqas.weatheryahoo.data;

import org.json.JSONObject;

/**
 * Created by haslina on 4/22/2016.
 */
public class Units implements JSONPopulator {

   private  String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
    temperature = data.optString("temperature");
    }
}
