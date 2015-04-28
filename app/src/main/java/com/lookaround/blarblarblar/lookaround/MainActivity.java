package com.lookaround.blarblarblar.lookaround;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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


public class MainActivity extends ActionBarActivity {
    // find location + measure distance
    GoogleApiClient mLocationClient;
    double latitude = 999.0;
    double longitude = 999.0;
    int target = 0;
    ProgressDialog dialog;
    Location currentLocation;
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
                    Intent c = new Intent(getApplicationContext(), ShopListActivity.class);
                    c.putExtra("current_latitude", latitude);
                    c.putExtra("current_longitude", longitude);
                    c.putExtra("near", "yes");
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
                    m.putExtra("near", "no");
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
                    Intent s = new Intent(getApplicationContext(), ShopListActivity.class);
                    s.putExtra("current_latitude", latitude);
                    s.putExtra("current_longitude", longitude);
                    s.putExtra("near", "suggest");
                    startActivity(s);
                }else{
                    target = 3;
                    showWait();
                }
            }
        });

        android.support.v7.widget.CardView card1 = (CardView)findViewById(R.id.card_view);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!= 999) {
                    Intent c = new Intent(getApplicationContext(), ShopDetail.class);
                    c.putExtra("current_latitude", latitude);
                    c.putExtra("current_longitude", longitude);
                    c.putExtra("shop_name", "ศูนย์หัตกรรมบ่อสร้าง - ร่มกะดาษสา");
                    c.putExtra("shop_desc_brief","ศูนย์ผลิตและจำหน่าย สินค้าหัตถกรรม ร่มบ่อสร้าง ร่มกระดาษสา");
                    c.putExtra("shop_desc","ศูนย์ผลิตและจำหน่าย สินค้าหัตถกรรม ร่มบ่อสร้าง ร่มกระดาษสา");
                    c.putExtra("shop_id","14");
                    c.putExtra("item_name","ร่มบ่อสร้าง");
                    c.putExtra("shop_latitude",18.7683055);
                    c.putExtra("shop_longitude",99.0750274);
                    startActivity(c);
                }else{
                    target = 4;
                    showWait();
                }
            }
        });

        android.support.v7.widget.CardView card2 = (CardView)findViewById(R.id.card_view2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude!= 999) {
                    Intent c = new Intent(getApplicationContext(), ShopDetail.class);
                    c.putExtra("current_latitude", latitude);
                    c.putExtra("current_longitude", longitude);
                    c.putExtra("shop_name", "แคบหมูน้องแอ้ ตลาดต้นพยอม");
                    c.putExtra("shop_desc_brief","จำหน่าย ไส้อั่ว เเคบหมู น้ำพริกหนุ่ม เเหนมป้าย่น หมูยอ");
                    c.putExtra("shop_desc","จำหน่าย ไส้อั่ว เเคบหมู น้ำพริกหนุ่ม เเหนมป้าย่น หมูยอ");
                    c.putExtra("shop_id","8");
                    c.putExtra("item_name","ร่มบ่อสร้าง");
                    c.putExtra("shop_latitude",18.7896208);
                    c.putExtra("shop_longitude",98.9613601);
                    startActivity(c);
                }else{
                    target = 5;
                    showWait();
                }
            }
        });

        // Find location --------------------
        FindLocation();
    }

// Location Provider ----------------------------------------------------
public void FindLocation() {
    LocationManager locationManager = (LocationManager) this
            .getSystemService(Context.LOCATION_SERVICE);

    android.location.LocationListener locationListener = new android.location.LocationListener() {
        public void onLocationChanged(Location location) {
            updateLocation(location);
/*
                Toast.makeText(
                        testLocation.this,
                        String.valueOf(currentLatitude) + "\n"
                                + String.valueOf(currentLongitude), Toast.LENGTH_SHORT)
                        .show();*/

        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

}


    void updateLocation(Location location) {
        currentLocation = location;
        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();

        if(target!=0){
            dialog.dismiss();
            goTo();
        }
        //textView1.setText(String.valueOf(currentLatitude) + "\n"+ String.valueOf(currentLongitude));

    }

    // End Location Provider ----------------------------------------------------

    public void showWait(){
        dialog = ProgressDialog.show(this, "Find your location", "Please wait ...\nMake sure you open GPS", true);
    }

    public void goTo(){
        if(target==1){
            Intent c = new Intent(getApplicationContext(), ShopListActivity.class);
            c.putExtra("current_latitude", latitude);
            c.putExtra("current_longitude", longitude);
            c.putExtra("near", "yes");
            startActivity(c);
            target=0;
        }else if(target==2){
            Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
            c.putExtra("latitude_value", latitude);
            c.putExtra("longitude_value", longitude);
            c.putExtra("near", "no");
            startActivity(c);
            target=0;
        }else if(target==3){
            Intent c = new Intent(getApplicationContext(), CategoryActivity.class);
            c.putExtra("current_latitude", latitude);
            c.putExtra("current_longitude", longitude);
            c.putExtra("near", "suggest");
            startActivity(c);
            target=0;
        }else if(target==4){
            Intent c = new Intent(getApplicationContext(), ShopDetail.class);
            c.putExtra("current_latitude", latitude);
            c.putExtra("current_longitude", longitude);
            c.putExtra("shop_name", "ศูนย์หัตกรรมบ่อสร้าง - ร่มกะดาษสา");
            c.putExtra("shop_desc_brief","ศูนย์ผลิตและจำหน่าย สินค้าหัตถกรรม ร่มบ่อสร้าง ร่มกระดาษสา");
            c.putExtra("shop_desc","ศูนย์ผลิตและจำหน่าย สินค้าหัตถกรรม ร่มบ่อสร้าง ร่มกระดาษสา");
            c.putExtra("shop_id","14");
            c.putExtra("item_name","ร่มบ่อสร้าง");
            c.putExtra("shop_latitude",18.7683055);
            c.putExtra("shop_longitude",99.0750274);
            startActivity(c);
            target=0;
        }else if(target==5){
            Intent c = new Intent(getApplicationContext(), ShopDetail.class);
            c.putExtra("current_latitude", latitude);
            c.putExtra("current_longitude", longitude);
            c.putExtra("shop_name", "แคบหมูน้องแอ้ ตลาดต้นพยอม");
            c.putExtra("shop_desc_brief","จำหน่าย ไส้อั่ว เเคบหมู น้ำพริกหนุ่ม เเหนมป้าย่น หมูยอ");
            c.putExtra("shop_desc","จำหน่าย ไส้อั่ว เเคบหมู น้ำพริกหนุ่ม เเหนมป้าย่น หมูยอ");
            c.putExtra("shop_id","8");
            c.putExtra("item_name","ร่มบ่อสร้าง");
            c.putExtra("shop_latitude",18.7896208);
            c.putExtra("shop_longitude",98.9613601);
            startActivity(c);
            target=0;
        }
    }


}
