package com.lookaround.blarblarblar.lookaround;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CategoryActivity extends ActionBarActivity {

    private Button btn1, btn2;
    double latitude = 999;
    double longitude = 999;
    //TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("เลือกหมวดหมู่");

        //textView1 = (TextView) findViewById(R.id.textView1);
        latitude = getIntent().getDoubleExtra("latitude_value",999);
        longitude = getIntent().getDoubleExtra("longitude_value",999);

        btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "foodandgift");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                startActivity(intent);
            }
        });

        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "common_use");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                startActivity(intent);
            }
        });

       //textView1.setText("Latitude: "+latitude+"\nlongitude: "+longitude);
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