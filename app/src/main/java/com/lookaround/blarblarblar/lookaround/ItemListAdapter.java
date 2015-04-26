package com.lookaround.blarblarblar.lookaround;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemListAdapter extends ArrayAdapter<String> {

    private final String TAG = "ItemListAdapter";
    private String URL_LINK = "http://vps.bongtrop.com/blarblarblar/getImage.php?type=item";

    private final Activity context;

    private String[] item_id;
    private String[] title;

    ItemListAdapter(Activity context, String[] titles, String[] item_ids)  {
        super(context, R.layout.item_list_layout, titles);

        this.context = context;
        this.title = titles;
        this.item_id = item_ids;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_list_layout, null, true);

        TextView tvItemName = (TextView)rowView.findViewById(R.id.txtItemName);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);

        tvItemName.setText(title[position]);

        String imageUri = URL_LINK + "&id=" + item_id[position];
        Picasso.with(context).load(imageUri).into(imageView);

        return rowView;
    }

}
