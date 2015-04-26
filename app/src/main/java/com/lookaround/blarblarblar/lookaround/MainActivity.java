package com.lookaround.blarblarblar.lookaround;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // find location + measure distance
    GoogleApiClient mLocationClient;
    double latitude = 999.0;
    double longitude = 999.0;
    int target = 0;
    ProgressDialog dialog;
    //TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        //startActivity(intent);

        ImageView menu_button1 = (ImageView)findViewById(R.id.menu1);
        menu_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!= 999) {
                    Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
                    c.putExtra("latitude_value", latitude);
                    c.putExtra("longitude_value", longitude);
                    startActivity(c);
                }else{
                    target = 1;
                    showWait();
                }
            }
        });

        ImageView menu_button2 = (ImageView)findViewById(R.id.menu2);
        menu_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!= 999) {
                    Intent m = new Intent(getApplicationContext(), CategoryActivity.class);
                    m.putExtra("latitude_value", latitude);
                    m.putExtra("longitude_value", longitude);
                    startActivity(m);
                }else{
                    target = 2;
                    showWait();
                }
            }
        });

        ImageView menu_button3 = (ImageView)findViewById(R.id.menu3);
        menu_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!= 999) {
                    Intent s = new Intent(getApplicationContext(), CategoryActivity.class);
                    s.putExtra("latitude_value", latitude);
                    s.putExtra("longitude_value", longitude);
                    startActivity(s);
                }else{
                    target = 3;
                    showWait();
                }
            }
        });

        // Find location --------------------
        //textView1 = (TextView) findViewById(R.id.textView1);
        boolean result = isServicesAvailable();
        result = isServicesAvailable();
        // Check Google api in phone
        if (result) {
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        } else {
            finish();
        }
    }

// Location Provider ----------------------------------------------------

    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    protected void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    public boolean isServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return (resultCode == ConnectionResult.SUCCESS);
    }


    // Find Location Address

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            /*textView1.setText("Provider : " + location.getProvider() + "\n"
                    + "Latitude : " + location.getLatitude() + "\n"
                    + "Longitude : " + location.getLongitude() + "\n");*/
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(target!=0){
                dialog.dismiss();
                goTo();
            }
            //textView1.setText("la"+latitude+ "\nlong:"+longitude);
        }
    };
    public void onConnected(Bundle bundle) {
        //Toast.makeText(ItemListActivity.this, "Services connected", Toast.LENGTH_SHORT).show();

        LocationRequest mRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000).setFastestInterval(10);

        LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,mRequest,locationListener);
    }

    public void onDisconnected() {
        Toast.makeText(MainActivity.this, "Services disconnected", Toast.LENGTH_SHORT).show();
    }

    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Services connection failed", Toast.LENGTH_SHORT).show();
    }
    // End Location Provider ----------------------------------------------------

    public void showWait(){
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
    }

    public void goTo(){
        if(target==1){
            Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
            c.putExtra("latitude_value", latitude);
            c.putExtra("longitude_value", longitude);
            startActivity(c);
            target=0;
        }else if(target==2){
            Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
            c.putExtra("latitude_value", latitude);
            c.putExtra("longitude_value", longitude);
            startActivity(c);
            target=0;
        }else if(target==3){
            Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
            c.putExtra("latitude_value", latitude);
            c.putExtra("longitude_value", longitude);
            startActivity(c);
            target=0;
        }
    }


}
