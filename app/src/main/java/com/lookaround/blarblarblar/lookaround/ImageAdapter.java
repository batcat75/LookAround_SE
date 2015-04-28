package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageAdapter extends ArrayAdapter<String> {

    private final String TAG = "ShopListAdapter";
    private String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getImage.php?type=shop";

    private final Activity context;
    private final String[] shop_id;



    ImageAdapter(Activity context, String[] shop_ids, String[] shop_names)  {
        super(context, R.layout.image_gallery_layout, shop_names);

        this.context = context;
        this.shop_id = shop_ids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.image_gallery_layout, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView_image);

        String imageUri = URL_LINK + "&id=" + shop_id[position];
        Picasso.with(context).load(imageUri).into(imageView);

        return imageView;
    }

}
