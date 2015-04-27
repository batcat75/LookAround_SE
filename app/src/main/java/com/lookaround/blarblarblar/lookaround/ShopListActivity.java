package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShopListActivity extends ActionBarActivity {

    //Activity
    private Activity activity;

    // Connection detector
    private ConnectionDetector cd;

    // Alert dialog manager
    private AlertDialogManager alert = new AlertDialogManager();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> shopList;

    // items JSONArray
    private JSONArray shops_json_array = null;

    // albums JSON url
    private static String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getShop.php";
    private String LOAD_LINK = "";

    // ALL JSON node names
    private static final String TAG_ID = "shop_id";
    private static final String TAG_SHOP_NAME = "shop_name";
    private static final String TAG_SHOP_DESC_BRIEF = "shop_desc_brief";
    private static final String TAG_SHOP_DESC = "shop_desc";
    private static final String TAG_IMAGE = "image_id";
    private static final String TAG_SCORE = "score";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private final String TAG = "ShopListActivity";

    private String[] shop_id;
    private String[] shop_name;
    private String[] shop_brief;
    private String[] shop_desc;
    private int[] imgID;
    private float[] score;
    private double[] shop_latitude;
    private double[] shop_longitude;

    ListView lv;

    Location currentLocation;
    double currentlatitude = 999;
    double currentlongitude = 999;
    String item_name_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*get Intent item_id */
        String item_id = getIntent().getStringExtra("item_id");
        LOAD_LINK = URL_LINK + "?item_id=" + item_id;

        Log.d(TAG, LOAD_LINK);

        currentlatitude = getIntent().getDoubleExtra("current_latitude",999);
        currentlongitude = getIntent().getDoubleExtra("current_longitude",999);
        item_name_bar = getIntent().getStringExtra("item_name");
        actionBar.setTitle(item_name_bar);

        //set activity
        activity = this;


        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(ShopListActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }


        // Hashmap for ListView
        shopList = new ArrayList<HashMap<String, String>>();

        // Loading Items JSON in Background Thread
        new LoadShops().execute();

        //new find location solution
        FindLocation();

        lv = (ListView) findViewById(R.id.listView2);
        /**
         * Listview item click listener
         * TrackListActivity will be lauched by passing album id
         * */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position : " + position + " , id : " + id);

                Intent i = new Intent(getApplicationContext(), ShopDetail.class);
                i.putExtra("shop_id", shop_id[(int)position]);
                i.putExtra("item_name", item_name_bar);
                i.putExtra("shop_name", shop_name[(int)position]);
                i.putExtra("shop_desc_brief", shop_brief[(int)position]);
                i.putExtra("shop_desc", shop_desc[(int)position]);
                i.putExtra("imgID", imgID[(int)position]);
                i.putExtra("score", score[(int)position]);
                i.putExtra("shop_latitude", shop_latitude[(int)position]);
                i.putExtra("shop_longitude", shop_longitude[(int)position]);
                i.putExtra("current_latitude", currentlatitude);
                i.putExtra("current_longitude", currentlongitude);
                startActivity(i);
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
            pDialog = new ProgressDialog(ShopListActivity.this);
            pDialog.setMessage("Listing Shops ...");
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
                    shop_name = new String[shops_json_array.length()];
                    shop_brief = new String[shops_json_array.length()];
                    shop_desc = new String[shops_json_array.length()];
                    imgID = new int[shops_json_array.length()];
                    score = new float[shops_json_array.length()];
                    shop_latitude = new double[shops_json_array.length()];
                    shop_longitude = new double[shops_json_array.length()];

                    // looping through All albums
                    for (int i = 0; i < shops_json_array.length(); i++) {

                        JSONObject c = shops_json_array.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_SHOP_NAME);
                        String brief = c.getString(TAG_SHOP_DESC_BRIEF);
                        String desc = c.getString(TAG_SHOP_DESC);
                        String shop_lati = c.getString(TAG_LATITUDE);
                        String shop_longi = c.getString(TAG_LONGITUDE);

                        Log.d(TAG, "id:name = " + id + " : " + name);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_SHOP_NAME, name);
                        map.put(TAG_SHOP_DESC_BRIEF, brief);
                        map.put(TAG_SHOP_DESC, desc);
                        map.put(TAG_IMAGE, String.valueOf(R.drawable.icon));
                        map.put(TAG_SCORE, String.valueOf(5.0f));
                        map.put(TAG_LATITUDE, shop_lati);
                        map.put(TAG_LONGITUDE, shop_longi);

                        // adding HashList to ArrayList
                        shopList.add(map);


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
            runOnUiThread(new Runnable() {
                public void run() {

                    for (int i = 0; i < shopList.size(); i++) {
                        HashMap<String, String> map = shopList.get(i);

                        shop_id[i] = map.get(TAG_ID);
                        shop_name[i] = map.get(TAG_SHOP_NAME);
                        shop_brief[i] = map.get(TAG_SHOP_DESC_BRIEF);
                        shop_desc[i] = map.get(TAG_SHOP_DESC);
                        imgID[i] = Integer.parseInt(map.get(TAG_IMAGE));
                        score[i] = Float.parseFloat(map.get(TAG_SCORE));
                        shop_latitude[i] =  Double.parseDouble(map.get(TAG_LATITUDE));
                        shop_longitude[i] =  Double.parseDouble(map.get(TAG_LONGITUDE));

                    }

                    /**
                     * Updating data into ListView
                     * */
                    ShopListAdapter listAdap = new ShopListAdapter(activity, shop_id, shop_name, score,shop_latitude,shop_longitude,currentlatitude,currentlongitude);

                    // updating listview
                    lv.setAdapter(listAdap);

                }
            });

        }

    }

    // Find location -----------------------------------------------------------------------------------
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
        currentlatitude = currentLocation.getLatitude();
        currentlongitude = currentLocation.getLongitude();
        //dialog.dismiss();
        //textView1.setText(String.valueOf(latitude) + "\n" + String.valueOf(longitude));

    }

}
