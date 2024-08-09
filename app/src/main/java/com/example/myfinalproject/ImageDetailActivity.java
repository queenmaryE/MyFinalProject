package com.example.myfinalproject;

import android.os.Bundle;
import android.widget.TextView;

public class ImageDetailActivity extends BaseActivity {

    private TextView dateTextView, urlTextView, hdUrlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Set up the toolbar with title and version number
        setupToolbar("Image Details", "Version 1.0.0");

        // Initialize UI components
        dateTextView = findViewById(R.id.dateTextView);
        urlTextView = findViewById(R.id.urlTextView);
        hdUrlTextView = findViewById(R.id.hdUrlTextView);

        // Get the NasaImage object passed from the previous activity
        NasaImage image = (NasaImage) getIntent().getSerializableExtra("image");

        // Display the image details
        if (image != null) {
            dateTextView.setText(image.getDate());
            urlTextView.setText(image.getUrl());
            hdUrlTextView.setText(image.getHdUrl());
        }
    }

    @Override
    protected String getDefaultHelpMessage() {
        return "Instructions for Image Detail Activity:\n1. View detailed information about the selected image.\n2. The URL and HD URL are provided for further reference.";
    }
}
