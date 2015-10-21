package com.example.paracite.whoismyrepdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Derek Strasters on 10/3/15,
 * for the use within the
 * MyApplication project under the
 * <p/>
 * TODO: Update copyright status. (Auto Generated)
 */

public class ImageURLAdapter extends ArrayAdapter<String> {

    private Context context;
    private int targetResource;
    private String[] urls;
    private LayoutInflater inflater;

    public ImageURLAdapter(Context context, int targetResource, String[] urls) {
        super(context, targetResource, urls);

        this.context = context;
        this.targetResource = targetResource;
        this.urls = urls;

        inflater = LayoutInflater.from(context);
    }


    // Called when todo finish this statement
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(targetResource, parent, false);
        }

        Picasso
                .with(context)
                .load(urls[position])
                .fit()
                .placeholder(R.drawable.us_seal)
                .noFade()
                .centerInside()
                .into((ImageView) convertView);

        return convertView;
    }
}
