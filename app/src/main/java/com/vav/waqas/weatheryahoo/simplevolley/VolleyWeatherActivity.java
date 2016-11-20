package com.vav.waqas.weatheryahoo.simplevolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vav.waqas.weatheryahoo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VolleyWeatherActivity extends AppCompatActivity {
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue ;

    LocationListener locationListener;
    LocationManager locationManager;

    Geocoder geocoder;

    double longtitude;
    double latitude;
    List<Address> addresses;
    String city = "Putrajaya";

    private ImageView weatherIconImageView;
    private TextView temperatureTextView, conditionTextView, locationTextView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_weather);
        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.tempratureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        progressDialog = new ProgressDialog(VolleyWeatherActivity.this);
        progressDialog.setMessage("Loading...");


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                longtitude = location.getLongitude();
                latitude = location.getLatitude();

                geocoder = new Geocoder(VolleyWeatherActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longtitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                    //getData(city,"C");
                }

                try {
                    city = addresses.get(0).getLocality();
                    //Toast.makeText(VolleyWeatherActivity.this, city+ " found", Toast.LENGTH_SHORT).show();
                    getData(city,"C");
                }catch (Exception c){
                    //Toast.makeText(VolleyWeatherActivity.this, c.toString(), Toast.LENGTH_SHORT).show();
                    //getData(city,"C");
                }

                getData(city,"C");
                Toast.makeText(VolleyWeatherActivity.this, city, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(VolleyWeatherActivity.this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
                getData(city,"C");
            }

            @Override
            public void onProviderDisabled(String provider) {
                displayPromptForEnablingGPS(VolleyWeatherActivity.this);


                Toast.makeText(VolleyWeatherActivity.this, "Please Turn ON the GPS", Toast.LENGTH_LONG).show();
            }

        };


        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);



    }





    public void forecastFunct(View view){
        Intent i = new Intent(this, ForecastsActivity.class);
        i.putExtra("AKEY",city);
        startActivity(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(city,"C");

    }




    private void getData(String location, String type) {
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+type+"'", location);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        Log.e("JsonNewsEndPoint","--"+endPoint.toString());
        jsonObjectRequest = new JsonObjectRequest(endPoint,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Dismissing progress dialog


                        try {
                            JSONObject post = response.getJSONObject("query").optJSONObject("results").optJSONObject("channel");
                            Log.e("JsonNewsResponse","--"+post.toString());

                            //temperature data
                            String temperatureType = post.optJSONObject("units").optString("temperature");
                            String temperature = post.optJSONObject("item").optJSONObject("condition").optString("temp");
                            String temperatureText = post.optJSONObject("item").optJSONObject("condition").optString("text");
                            String temperatureCode = post.optJSONObject("item").optJSONObject("condition").optString("code");
                            String temperatureDate = post.optJSONObject("item").optJSONObject("condition").optString("date");

                            //location data
                            String locationCity = post.optJSONObject("location").optString("city");
                            String locationCountry = post.optJSONObject("location").optString("country");

                            int resourceID = getResources().
                                    getIdentifier("drawable/icon_" + temperatureCode, null, getPackageName());
                            //noinspection deprecation
                            Drawable weatherIcon = getResources().getDrawable(resourceID);
                            weatherIconImageView.setImageDrawable(weatherIcon);

                            temperatureTextView.setText(temperature+"\u00b0"+" "+ temperatureType);
                            conditionTextView.setText(temperatureText+"\n"+temperatureDate);
                            locationTextView.setText(locationCity+", "+locationCountry);

                            progressDialog.dismiss();

                            Log.e("DATA",temperature+"-"+temperatureCode+"-"+temperatureDate+"-"+temperatureText+"-"+temperatureType+"-"+locationCity+"-"+locationCountry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //calling method to parse json array

                        //          dismissDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response.ErrorListener",error.toString());
                        progressDialog.dismiss();
                    }
                });

        // dismissDialog();

        requestQueue = Volley.newRequestQueue(this);


               int socketTimeout = 6000; // 6 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        //jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);
        progressDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.temp_menu:
                AlertDialog.Builder alert = new AlertDialog.Builder(VolleyWeatherActivity.this);
                LayoutInflater inflater=VolleyWeatherActivity.this.getLayoutInflater();
                //this is what I did to added the layout to the alert dialog
                View layout=inflater.inflate(R.layout.dialog, null);
                alert.setView(layout);
                final EditText locationStringInput=(EditText)layout.findViewById(R.id.etPlace);
                final EditText typeStringInput=(EditText)layout.findViewById(R.id.etType);
                alert.setTitle("Selection Area");
                alert.setMessage("Enter Place and Type");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String locationString = locationStringInput.getText().toString();
                        String typeString = typeStringInput.getText().toString();
                        if (locationString.isEmpty() || typeString.isEmpty()) {
//change
                        } else {
                            city =locationString;
                        getData(locationString, typeString);
                        }
                    }
                });
                alert.show();
                return true;
            case R.id.maps_menu:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


}
