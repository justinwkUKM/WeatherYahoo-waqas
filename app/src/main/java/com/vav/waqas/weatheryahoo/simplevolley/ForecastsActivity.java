package com.vav.waqas.weatheryahoo.simplevolley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vav.waqas.weatheryahoo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForecastsActivity extends AppCompatActivity {

    List<ForecastData> forecastDataList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue ;
    ProgressDialog progressDialog;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecasts);
        forecastDataList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y)
            {
                super.onScrolled(recyclerView, x, y);

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        i = getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String city = i.getStringExtra("AKEY");
        Toast.makeText(ForecastsActivity.this, city, Toast.LENGTH_SHORT).show();
        getData(city);
    }

    private void getData(String city) {
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+"c"+"'", city);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        Log.e("JsonNewsEndPoint","--"+endPoint.toString());
        jsonObjectRequest = new JsonObjectRequest(endPoint,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject post = response.getJSONObject("query").optJSONObject("results").optJSONObject("channel");

                            //temperature data
                            String temperatureCode = post.optJSONObject("item").optJSONObject("condition").optString("code");
                            JSONArray forecasts = post.getJSONObject("item").getJSONArray("forecast");

                            JSON_PARSE_DATA_AFTER_WEBCALL(forecasts);


                            Log.e("Forecast",forecasts.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response.ErrorListener",error.toString());
                        progressDialog.dismiss();
                    }
                });



        requestQueue = Volley.newRequestQueue(this);


        int socketTimeout = 6000; // 6 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);
        progressDialog.show();
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            ForecastData forecastData2 = new ForecastData();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                forecastData2.setfDate(json.getString("date"));
                forecastData2.setfDay(json.getString("day"));
                forecastData2.setfHigh(json.getString("high"));
                forecastData2.setfLow(json.getString("low"));
                forecastData2.setfText(json.getString("text"));
                forecastData2.setfCode(json.getString("code"));

                Log.e("DateDay",json.getString("high")+json.getString("low"));

            } catch (JSONException e) {
                Log.e("JSONException",e.toString());
                e.printStackTrace();
            }

            forecastDataList.add(forecastData2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(forecastDataList, this);
        recyclerView.setAdapter(recyclerViewadapter);



    }


}
