package com.lookaround.blarblarblar.lookaround;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class ShopActivity extends ActionBarActivity {

    private ImageView imgView;
    private TextView txtTitle, txtDesc;
    private RatingBar ratingScore;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        intent = getIntent();
        String shop_name = intent.getStringExtra("shop_name");
        String shop_desc_brief = intent.getStringExtra("shop_desc_brief");
        String shop_desc = intent.getStringExtra("shop_desc");
        int imgID = intent.getIntExtra("imgID",0);
        float score = intent.getFloatExtra("score", 0.0f);

        imgView = (ImageView)findViewById(R.id.imageView);
        imgView.setImageResource(imgID);

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtTitle.setText(shop_name);

        txtDesc = (TextView)findViewById(R.id.txtDesc);
        txtDesc.setText(shop_desc_brief);

        ratingScore = (RatingBar)findViewById(R.id.ratingBar);
        ratingScore.setRating(score);
        ratingScore.setIsIndicator(true);

        // Google static map -----------------------------------------------------------
        GoogleStaticMaps gsm = new GoogleStaticMaps();
        gsm.setSize(1000, 900);
        ImageView imageView1 = (ImageView)findViewById(R.id.imageViewStaticMap);
        gsm.addMarker(16.002812, 100.221808);
        gsm.setLanguage("th");
        gsm.setMapType(GoogleStaticMaps.TYPE_DEFAULT);
        gsm.setImageFormat(GoogleStaticMaps.FORMAT_JPEG);
        imageView1.setImageBitmap(gsm.getMapByCenter(16.002812, 100.221808, 15));
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m =  new Intent(getApplicationContext(), MapActivity.class);
                startActivity(m);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop, menu);
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
