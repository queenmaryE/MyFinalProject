package com.example.myfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.List;

public class SavedImagesActivity extends BaseActivity {

    private ListView listView;
    private List<NasaImage> savedImages;
    private SavedImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);  // Set activity_base.xml as the layout

        // Set up the toolbar with title and version number
        setupToolbar("Saved Images", "Version 1.0.0");

        // Inflate the specific content layout for SavedImagesActivity into the content_frame
        View contentView = getLayoutInflater().inflate(R.layout.activity_saved_images, findViewById(R.id.content_frame), true);

        listView = contentView.findViewById(R.id.listView);

        savedImages = ImageStorage.getSavedImages(this);
        adapter = new SavedImageAdapter(this, savedImages);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            NasaImage selectedImage = savedImages.get(position);
            showImageDetails(selectedImage);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            NasaImage imageToDelete = savedImages.get(position);
            showDeleteConfirmationDialog(imageToDelete);
            return true;
        });
    }


    private void showImageDetails(NasaImage image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image Details");
        builder.setMessage("Date: " + image.getDate() + "\n\nURL: " + image.getUrl() + "\n\nHD URL: " + image.getHdUrl());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(NasaImage image) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    ImageStorage.deleteImage(SavedImagesActivity.this, image);
                    savedImages.remove(image);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SavedImagesActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected String getDefaultHelpMessage() {
        return "Instructions for Saved Images Activity:\n1. View and manage your saved images.\n2. Tap an image to view details.";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            showHelpDialog();  // Show help dialog defined in BaseActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_saved_images) {
            startActivity(new Intent(this, SavedImagesActivity.class));
        } else if (id == R.id.nav_help) {
            showHelpDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
