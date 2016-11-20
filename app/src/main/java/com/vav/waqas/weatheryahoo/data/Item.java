package com.vav.waqas.weatheryahoo.data;

import org.json.JSONObject;

/**
 * Created by haslina on 4/22/2016.
 */
public class Item implements JSONPopulator {

    public Condition getCondition() {
        return condition;
    }

    private Condition condition;
    @Override
    public void populate(JSONObject data) {

        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
