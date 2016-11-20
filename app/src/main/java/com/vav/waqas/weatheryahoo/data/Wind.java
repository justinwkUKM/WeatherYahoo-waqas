package com.vav.waqas.weatheryahoo.data;

import org.json.JSONObject;

/**
 * Created by haslina on 11/16/2016.
 */
public class Wind implements  JSONPopulator {
    String direction ;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public void populate(JSONObject data) {
        direction = data.optString("direction");
    }
}
