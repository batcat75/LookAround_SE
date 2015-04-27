package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopDetail extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> shopDetail;

    // items JSONArray
    private JSONArray shops_json_array = null;

    // ALL JSON node names
    private static final String TAG_ID = "shop_id";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_TIME = "open_time";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private final String TAG = "ShopDetail";

    private String[] shop_id;
    private String[] address;
    private String[] open_time;
    private String[] phone;
    private double[] latitude;
    private double[] longitude;

    private String URL_LINK_COVER = "http://vps.bongtrop.com/blarblarblar/getImage_detailPage.php?type=shop";
    private String URL_LINK_DETAIL = "http://vps.bongtrop.com/blarblarblar/getShop_Detail.php?shop_id=";

    private Intent intent;
    private ImageView imgView;
    private TextView txtTitle, txtDesc,txtPhone,txtAddress,txtTime;
    Activity context ;

    String LOAD_LINK;

    String shop_name;
    double shop_latitude;
    double shop_longitude;
    double current_latitude;
    double current_longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        intent = getIntent();
        shop_name = intent.getStringExtra("shop_name");
        String shop_desc_brief = intent.getStringExtra("shop_desc_brief");
        String shop_desc = intent.getStringExtra("shop_desc");
        String shopID = intent.getStringExtra("shop_id");
        String item_name= intent.getStringExtra("item_name");;
        float score = intent.getFloatExtra("score", 0.0f);
        shop_latitude = intent.getDoubleExtra("shop_latitude",0);
        shop_longitude = intent.getDoubleExtra("shop_longitude",0);
        current_latitude = intent.getDoubleExtra("current_latitude",999);
        current_longitude = intent.getDoubleExtra("current_longitude",999);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(item_name);

        LOAD_LINK = URL_LINK_DETAIL + shopID;
        Log.d(TAG, LOAD_LINK);
        // Loading Items JSON in Background Thread
        new LoadShops().execute();

        imgView = (ImageView)findViewById(R.id.imageView_cover);
        //imgView.setImageResource(imgID);
        String imageUri = URL_LINK_COVER + "&id=" + shopID+"&use_for=cover";
        Picasso.with(context).load(imageUri).resize(1080, 500).into(imgView);

        txtTitle = (TextView)findViewById(R.id.textView_name);
        txtTitle.setText(shop_name);

        txtDesc = (TextView)findViewById(R.id.textView_description);
        txtDesc.setText(shop_desc_brief);



        // Google static map -----------------------------------------------------------
        GoogleStaticMaps gsm = new GoogleStaticMaps();
        gsm.setSize(1080, 300);
        ImageView imageView1 = (ImageView)findViewById(R.id.imageViewStaticMap);
        gsm.addMarker(shop_latitude, shop_longitude);
        gsm.setLanguage("th");
        gsm.setMapType(GoogleStaticMaps.TYPE_DEFAULT);
        gsm.setImageFormat(GoogleStaticMaps.FORMAT_JPEG);
        imageView1.setImageBitmap(gsm.getMapByCenter(shop_latitude, shop_longitude, 15));
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), MapActivity.class);
                m.putExtra("shop_latitude", shop_latitude);
                m.putExtra("shop_longitude", shop_longitude);
                m.putExtra("shop_name", shop_name);
                m.putExtra("current_latitude", current_latitude);
                m.putExtra("current_longitude", current_longitude);
                startActivity(m);
            }
        });
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

    //Inner Class

    /**
     * Background Async Task to Load all Albums by making http request
     */
    class LoadShops extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShopDetail.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(LOAD_LINK, "GET",
                    params);

            // Check your log cat for JSON reponse
            Log.d("Shops JSON: ", "> " + json);

            try {
                shops_json_array = new JSONArray(json);

                Log.d(TAG, "json_array_len : " + shops_json_array.length());

                if (shops_json_array != null) {

                    /*
                        Allocated String Array
                    */
                    shop_id = new String[shops_json_array.length()];
                    address = new String[shops_json_array.length()];
                    open_time = new String[shops_json_array.length()];
                    phone = new String[shops_json_array.length()];
                    latitude = new double[shops_json_array.length()];
                    longitude = new double[shops_json_array.length()];

                    // looping through All albums
                    for (int i = 0; i < shops_json_array.length(); i++) {

                        JSONObject c = shops_json_array.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String address = c.getString(TAG_ADDRESS);
                        String time = c.getString(TAG_TIME);
                        String phone = c.getString(TAG_PHONE);
                        String lati = c.getString(TAG_LATITUDE);
                        String longi = c.getString(TAG_LONGITUDE);

                        txtPhone = (TextView)findViewById(R.id.textView_phone);
                        txtPhone.setText(phone);

                        txtAddress = (TextView)findViewById(R.id.textView_address);
                        txtAddress.setText(address);

                        txtTime = (TextView)findViewById(R.id.textView_time);
                        txtTime.setText(time);

                        Log.d(TAG, "id:name = " + id );

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_ADDRESS, address);
                        map.put(TAG_TIME, time);
                        map.put(TAG_PHONE, phone);
                       // map.put(TAG_LATITUDE, lati);
                        //map.put(TAG_LONGITUDE, longi);

                        // adding HashList to ArrayList
                        //shopDetail.add(map);


                    }
                } else {
                    Log.d("Items: ", "null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();

            // updating UI from Background Thread
           /* runOnUiThread(new Runnable() {
                public void run() {

                    for (int i = 0; i < shopDetail.size(); i++) {
                        HashMap<String, String> map = shopDetail.get(i);

                        shop_id[i] = map.get(TAG_ID);
                        address[i] = map.get(TAG_ADDRESS);
                        open_time[i] = map.get(TAG_TIME);
                        phone[i] = map.get(TAG_PHONE);
                        latitude[i] = Double.parseDouble(map.get(TAG_LATITUDE));
                        logitude[i] = Double.parseDouble(map.get(TAG_LONGITUDE));

                    }



                }
            });*/

        }

    }

}
