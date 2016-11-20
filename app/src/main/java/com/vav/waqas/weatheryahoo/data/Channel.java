package com.vav.waqas.weatheryahoo.data;



import org.json.JSONObject;

/**
 * Created by haslina on 4/22/2016.
 */
public class Channel implements JSONPopulator {
    private  Item item;
    private Units units;
    private Location location;
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public Units getUnits() {
        return units;
    }

    public Item getItem() {

        return item;
    }

    public Location getLocation() {
        return location;
    }


    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        location = new Location();
        location.populate(data.optJSONObject("location"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));
    }
}
