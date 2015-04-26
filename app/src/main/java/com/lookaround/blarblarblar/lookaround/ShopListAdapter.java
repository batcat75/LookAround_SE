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

public class ShopListAdapter extends ArrayAdapter<String> {

    private final String TAG = "ShopListAdapter";
    private String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getImage.php?type=shop";

    private final Activity context;
    private final String[] shop_id;
    private final String[] shop_name;
    private final float[] score;


    ShopListAdapter(Activity context, String[] shop_ids, String[] shop_names, float[] score)  {
        super(context, R.layout.shop_list_layout, shop_names);

        this.context = context;
        this.shop_id = shop_ids;
        this.shop_name = shop_names;
        this.score = score;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.shop_list_layout, null, true);

        TextView tvTitle = (TextView)rowView.findViewById(R.id.txtTitle);
        TextView tvScore = (TextView)rowView.findViewById(R.id.txtScore);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);

        tvTitle.setText(shop_name[position]);
        tvScore.setText(String.valueOf(score[position]));

        String imageUri = URL_LINK + "&id=" + shop_id[position];
        Picasso.with(context).load(imageUri).into(imageView);

        return rowView;
    }

}
