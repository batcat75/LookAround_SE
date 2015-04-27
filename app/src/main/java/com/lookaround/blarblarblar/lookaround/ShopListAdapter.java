package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.app.Notification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ShopListAdapter extends ArrayAdapter<String> {

    private final String TAG = "ShopListAdapter";
    private String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getImage.php?type=shop";

    private final Activity context;
    private final String[] shop_id;
    private final String[] shop_name;
    private final float[] score;
    private final double[] shop_latitude;
    private final double[] shop_longitude;
    private final double current_latitude;
    private final double current_longitude;



    ShopListAdapter(Activity context, String[] shop_ids, String[] shop_names, float[] score,double[] shop_latitude,double[] shop_longitude,double current_latitude,double current_longitude)  {
        super(context, R.layout.shop_list_layout, shop_names);

        this.context = context;
        this.shop_id = shop_ids;
        this.shop_name = shop_names;
        this.score = score;
        this.shop_latitude = shop_latitude;
        this.shop_longitude = shop_longitude;
        this.current_latitude = current_latitude;
        this.current_longitude =current_longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.shop_list_layout, null, true);

        TextView tvTitle = (TextView)rowView.findViewById(R.id.txtTitle);
        TextView tvScore = (TextView)rowView.findViewById(R.id.txtScore);
        TextView tvDistance = (TextView)rowView.findViewById(R.id.txtDistance);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);

        tvTitle.setText(shop_name[position]);
        //tvScore.setText(String.valueOf(score[position]));

        String imageUri = URL_LINK + "&id=" + shop_id[position];
        Picasso.with(context).load(imageUri).into(imageView);

        double distance = getDistance(current_latitude,current_longitude,shop_latitude[position],shop_longitude[position]);
        tvDistance.setText(new DecimalFormat("##.#").format((distance))+"กม.");

        return rowView;
    }

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
