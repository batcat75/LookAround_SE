package com.lookaround.blarblarblar.lookaround;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class CategoryActivity extends ActionBarActivity {

    private TextView txtmenu1,txtmenu2;
    private ImageView menu1,menu2;
    double latitude = 999;
    double longitude = 999;
    String near;
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
        near = getIntent().getStringExtra("near");

        menu1 = (ImageView)findViewById(R.id.imageView_menu1);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "1");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                intent.putExtra("near", near);
                startActivity(intent);
            }
        });

        txtmenu1 = (TextView)findViewById(R.id.txtMenu1);
        txtmenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "1");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                intent.putExtra("near", near);
                startActivity(intent);
            }
        });

        menu2 = (ImageView)findViewById(R.id.imageView_menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "3");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                intent.putExtra("near", near);
                startActivity(intent);
            }
        });

        txtmenu2 = (TextView)findViewById(R.id.txtMenu2);
        txtmenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("item_type", "3");
                intent.putExtra("latitude_value", latitude);
                intent.putExtra("longitude_value", longitude);
                intent.putExtra("near", near);
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