package com.example.myfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SavedImageAdapter extends ArrayAdapter<NasaImage> {

    public SavedImageAdapter(Context context, List<NasaImage> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NasaImage image = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_saved_image, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView noteTextView = convertView.findViewById(R.id.noteTextView);
        TextView urlTextView = convertView.findViewById(R.id.urlTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        dateTextView.setText("Date: " + image.getDate());
        noteTextView.setText("Note: " + image.getNote());
        urlTextView.setText("URL: " + image.getUrl());

        // Load the image using Glide
        Glide.with(getContext())
                .load(image.getUrl())
                .into(imageView);

        return convertView;
    }
}
