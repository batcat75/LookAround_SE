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
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ItemListActivity extends ActionBarActivity {

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
    private static String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getItem.php";
    private String LOAD_LINK = "";

    // ALL JSON node names
    private static final String TAG_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_IMAGE = "image_id";

    private final String TAG = "MainActivity";

    private String[] item_id;
    private String[] title;
    private int[] imgID;

    ListView lv;
    // find location + measure distance
    //GoogleApiClient mLocationClient;
    TextView textView1;
    double currentlatitude = 999;
    double currentlongitude = 999;
    String near;
    //double[] results_distance = new double[1];
    ProgressDialog dialog;
    Location currentLocation;
    //--------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        currentlatitude = getIntent().getDoubleExtra("latitude_value",999);
        currentlongitude = getIntent().getDoubleExtra("longitude_value",999);
        near = getIntent().getStringExtra("near");

        activity = this;

        cd = new ConnectionDetector(getApplicationContext());

        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(ItemListActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        final String item_type = getIntent().getStringExtra("item_type");

        //set type of items for data query
        LOAD_LINK = URL_LINK + "?type_id="+item_type;
        Log.d(TAG, LOAD_LINK);

        if(item_type.equals("1")) {
            actionBar.setTitle("เลือกชนิดของกิน");
        }else if(item_type.equals("3")){
            actionBar.setTitle("เลือกชนิดของฝาก");
        }else{
            actionBar.setTitle("เลือกสื่งที่สนใจ");
        }

        // Hashmap for ListView
        shopList = new ArrayList<HashMap<String, String>>();

        // Loading Items JSON in Background Thread
        new LoadItems().execute();


        lv = (ListView) findViewById(R.id.listView);
        /**
         * Listview item click listener
         * TrackListActivity will be lauched by passing album id
         * */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position : " + position + " , id : " + id);

                Intent i = new Intent(getApplicationContext(), ShopListActivity.class);
                i.putExtra("item_id", item_id[(int) id]);
                i.putExtra("item_name", title[(int) id]);
                i.putExtra("current_latitude", currentlatitude);
                i.putExtra("current_longitude", currentlongitude);
                i.putExtra("near", near);

                startActivity(i);
            }
        });

        // Find location --------------------
        textView1 = (TextView) findViewById(R.id.textView1);

        //new find location solution
        FindLocation();

        /*
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
*/
/*
        results_distance[0] = getDistance(latitude,longitude,18.789595,98.974265);
        textView1.setText("la"+latitude+ "\nlong:"+longitude+"\nDistance : " + results_distance[0] + "\n");*/
        // ----------------------------------------------

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
    class LoadItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ItemListActivity.this);
            pDialog.setMessage("Listing Items ...");
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
            Log.d("Items JSON: ", "> " + json);

            try {
                shops_json_array = new JSONArray(json);

                if (shops_json_array != null) {

                    /*
                        Allocated String Array
                    */
                    item_id = new String[shops_json_array.length()];
                    title = new String[shops_json_array.length()];
                    imgID = new int[shops_json_array.length()];

                    // looping through All albums
                    for (int i = 0; i < shops_json_array.length(); i++) {
                        JSONObject c = shops_json_array.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_ITEM_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_ITEM_NAME, name);
                        map.put(TAG_IMAGE, String.valueOf(R.drawable.icon));

                        // adding HashList to ArrayList
                        shopList.add(map);

                        Log.d(TAG, "id:name = " + id + " : " + name);
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

                        item_id[i] = map.get(TAG_ID);
                        title[i] = map.get(TAG_ITEM_NAME);
                        imgID[i] = Integer.parseInt(map.get(TAG_IMAGE));

                        Log.d(TAG, "2] id:name = " + item_id[i] + " : " + title[i]);
                    }

                    /**
                     * Updating data into ListView
                     * */
                    ItemListAdapter listAdap = new ItemListAdapter(activity, title, item_id);

                    // updating listview
                    lv.setAdapter(listAdap);

                }
            });

        }

    }
    // Location Provider ----------------------------------------------------
//new find solution
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

/*
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
           /* latitude = location.getLatitude();
            longitude = location.getLongitude();
            dialog.dismiss();
            textView1.setText("la"+latitude+ "\nlong:"+longitude+"\nDistance : " + results_distance[0] + "\n");
        }
    };
    public void onConnected(Bundle bundle) {
       //Toast.makeText(ItemListActivity.this, "Services connected", Toast.LENGTH_SHORT).show();

        LocationRequest mRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000).setFastestInterval(100);

        LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,mRequest,locationListener);
    }

    public void onDisconnected() {
        Toast.makeText(ItemListActivity.this, "Services disconnected", Toast.LENGTH_SHORT).show();
    }

    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(ItemListActivity.this, "Services connection failed", Toast.LENGTH_SHORT).show();
    }*/
    // End Location Provider ----------------------------------------------------

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) { //Haversine_formula
        double Radius = 6372.8; // In kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }
}
