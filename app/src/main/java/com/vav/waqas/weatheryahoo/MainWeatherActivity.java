package com.vav.waqas.weatheryahoo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.waqas.weatheryahoo.data.Channel;
import com.vav.waqas.weatheryahoo.service.WeatherServiceCallback;
import com.vav.waqas.weatheryahoo.service.YahooWeatherService;

public class MainWeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView tempratureTextView, conditionTextView, locationTextView;
    private YahooWeatherService yahooWeatherService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        tempratureTextView = (TextView) findViewById(R.id.tempratureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);




        yahooWeatherService = new YahooWeatherService(this);

        yahooWeatherService.refreshWeatherForecast("Bangi, My", "C", MainWeatherActivity.this);




       /* yahooWeatherService = new YahooWeatherService(MainWeatherActivity.this);
        yahooWeatherService.refreshWeatherForecast("Klang, Malaysia", "c", this);
*/
        progressDialog = new ProgressDialog(MainWeatherActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();



    }

    @Override
    public void serviceSuccess(Channel channel) {
        progressDialog.hide();

        int resourceID = getResources().
                getIdentifier("drawable/icon_" + channel.getItem().getCondition().getCode(), null, getPackageName());
        //noinspection deprecation
        Drawable weatherIcon = getResources().getDrawable(resourceID);
        weatherIconImageView.setImageDrawable(weatherIcon);
        Log.e("wind direction",channel.getWind().getDirection());
        tempratureTextView.setText(channel.getItem().getCondition().getTemperature() + "\u00B0 " + channel.getUnits().getTemperature());
        String city = channel.getLocation().getCity();
        String country = channel.getLocation().getCountry();
        locationTextView.setText(city+", "+ country);
        conditionTextView.setText(channel.getItem().getCondition().getDescription());


    }

    @Override
    public void serviceFailure(Exception exception) {
        progressDialog.hide();
        Log.e("serviceFailureEXC",exception.toString());
        Toast.makeText(MainWeatherActivity.this, "serviceFailureEXC", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder alert = new AlertDialog.Builder(MainWeatherActivity.this);
                LayoutInflater inflater=MainWeatherActivity.this.getLayoutInflater();
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
                            Toast.makeText(getApplicationContext(), "Enter Place and Type", Toast.LENGTH_SHORT).show();
                        } else {
                            yahooWeatherService = new YahooWeatherService(MainWeatherActivity.this);
                            yahooWeatherService.refreshWeatherForecast(locationString, typeString, MainWeatherActivity.this);

                            progressDialog = new ProgressDialog(MainWeatherActivity.this);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                        }
                    }
                });
                alert.show();
                return true;
            case R.id.maps_menu:
                Intent i = new Intent (MainWeatherActivity.this, MapsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
