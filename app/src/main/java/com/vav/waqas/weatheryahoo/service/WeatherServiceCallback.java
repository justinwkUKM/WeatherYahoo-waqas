package com.vav.waqas.weatheryahoo.service;

import com.vav.waqas.weatheryahoo.data.Channel;

/**
 * Created by haslina on 4/22/2016.
 */
public interface WeatherServiceCallback {

    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
