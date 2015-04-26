package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

    private final String TAG = "ShopListActivity";

    private String[] shop_id;
    private String[] shop_name;
    private String[] shop_brief;
    private String[] shop_desc;
    private int[] imgID;
    private float[] score;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        /*get Intent item_id */
        String item_id = getIntent().getStringExtra("item_id");
        LOAD_LINK = URL_LINK + "?item_id=" + item_id;

        Log.d(TAG, LOAD_LINK);

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


        lv = (ListView) findViewById(R.id.listView2);
        /**
         * Listview item click listener
         * TrackListActivity will be lauched by passing album id
         * */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position : " + position + " , id : " + id);

                Intent i = new Intent(getApplicationContext(), ShopActivity.class);
                i.putExtra("shop_id", shop_id[(int)position]);
                i.putExtra("shop_name", shop_name[(int)position]);
                i.putExtra("shop_desc_brief", shop_brief[(int)position]);
                i.putExtra("shop_desc", shop_desc[(int)position]);
                i.putExtra("imgID", imgID[(int)position]);
                i.putExtra("score", score[(int)position]);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_list, menu);
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

                    // looping through All albums
                    for (int i = 0; i < shops_json_array.length(); i++) {

                        JSONObject c = shops_json_array.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_SHOP_NAME);
                        String brief = c.getString(TAG_SHOP_DESC_BRIEF);
                        String desc = c.getString(TAG_SHOP_DESC);

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

                    }

                    /**
                     * Updating data into ListView
                     * */
                    ShopListAdapter listAdap = new ShopListAdapter(activity, shop_id, shop_name, score);

                    // updating listview
                    lv.setAdapter(listAdap);

                }
            });

        }

    }

}
