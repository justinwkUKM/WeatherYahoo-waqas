package com.vav.waqas.weatheryahoo.service;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vav.waqas.weatheryahoo.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by haslina on 4/22/2016.
 */
public class YahooWeatherService {
    private WeatherServiceCallback weatherServiceCallback;
    private String location;
    private Exception error;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue ;
    Context mContext;

    public YahooWeatherService(WeatherServiceCallback weatherServiceCallback) {
        this.weatherServiceCallback = weatherServiceCallback;
    }

    public String getLocation() {
        return location;
    }

    public void refreshWeatherForecast(String l, final String type, Context context) {
        this.location = l;
        mContext =context;
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+type+"'", l);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        jsonObjectRequest = new JsonObjectRequest(endPoint,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                         JSONObject queryResults = response.optJSONObject("query");
                        int count = queryResults.optInt("count");
                        if (count == 0) {
                            Log.e("Response.ErrorListener1",error.toString());
                            weatherServiceCallback.serviceFailure(new LocationWeatherException("No Weather data found for " + location));
                        }
                        Channel channel = new Channel();
                        channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                        weatherServiceCallback.serviceSuccess(channel);


                        Log.e("DATA",queryResults.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Response.ErrorListener2", Toast.LENGTH_SHORT).show();
                        Log.e("Response.ErrorListener2",error.toString());
                        weatherServiceCallback.serviceFailure(error);


                    }
                });


        requestQueue = Volley.newRequestQueue(mContext);


        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);

        /*new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+type+"'", params[0]);

                String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                try {
                    URL url = new URL(endPoint);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } catch (MalformedURLException e) {
                    error = e;
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null) {
                    weatherServiceCallback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject queryResults = jsonObject.optJSONObject("query");
                    int count = queryResults.optInt("count");
                    if (count == 0) {
                        weatherServiceCallback.serviceFailure(new LocationWeatherException("No Weather data found for " + location));
                    }
                    Channel channel = new Channel();
                    channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                    weatherServiceCallback.serviceSuccess(channel);
                } catch (JSONException e) {
                    weatherServiceCallback.serviceFailure(error);
                }
            }
        }.execute(location);*/
    }
    public void refreshWeatherSunset(String l, final String type, Context context) {
        this.location = l;
        mContext =context;
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+type+"'", l);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        jsonObjectRequest = new JsonObjectRequest(endPoint,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject queryResults = response.optJSONObject("query");
                        int count = queryResults.optInt("count");
                        if (count == 0) {
                            Log.e("Response.ErrorListener1",error.toString());
                            weatherServiceCallback.serviceFailure(new LocationWeatherException("No Weather data found for " + location));
                        }
                        Channel channel = new Channel();
                        channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                        weatherServiceCallback.serviceSuccess(channel);


                        Log.e("DATA",queryResults.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Response.ErrorListener2", Toast.LENGTH_SHORT).show();
                        Log.e("Response.ErrorListener2",error.toString());
                        weatherServiceCallback.serviceFailure(error);


                    }
                });


        requestQueue = Volley.newRequestQueue(mContext);


        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);


    }

    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}
