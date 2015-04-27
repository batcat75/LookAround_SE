package com.lookaround.blarblarblar.lookaround;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends ActionBarActivity {

    GoogleMap map;
    double shop_latitude;
    double shop_longitude;
    double current_latitude;
    double current_longitude;
    String shop_name;
    Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        shop_latitude = getIntent().getDoubleExtra("shop_latitude",0);
        shop_longitude = getIntent().getDoubleExtra("shop_longitude", 0);
        shop_name = getIntent().getStringExtra("shop_name");
        current_latitude = getIntent().getDoubleExtra("current_latitude", 0);
        current_longitude = getIntent().getDoubleExtra("current_longitude", 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(shop_name);

        // Google map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(shop_latitude, shop_longitude), 15));
        LatLng latLng = new LatLng(shop_latitude, shop_longitude);
        map.addMarker(new MarkerOptions().position(latLng).title(shop_name));

        latLng = new LatLng(current_latitude, current_longitude);
        currentMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.manhere)).title("คุณอยู่ที่นี่"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
