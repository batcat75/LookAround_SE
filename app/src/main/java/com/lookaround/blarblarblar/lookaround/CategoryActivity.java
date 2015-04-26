package com.lookaround.blarblarblar.lookaround;

import android.content.Intent;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}