package com.example.myfinalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends BaseActivity {

    private DatePicker datePicker;
    private Button fetchButton, hdImageButton, saveButton, viewSavedButton;
    private EditText editText;
    private TextView dateTextView, urlTextView;
    private ProgressBar progressBar;

    private String selectedDate;
    private String imageUrl;
    private String hdImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);  // Set activity_base.xml as the layout

        // Set up the toolbar with title and version number
        setupToolbar("NASA Image Search", "Version 1.0.0");

        // Inflate the specific content layout for MainActivity into the content_frame
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.content_frame));

        // Initialize UI components
        datePicker = findViewById(R.id.datePicker);
        fetchButton = findViewById(R.id.fetchButton);
        hdImageButton = findViewById(R.id.hdImageButton);
        saveButton = findViewById(R.id.saveButton);
        viewSavedButton = findViewById(R.id.viewSavedButton);
        editText = findViewById(R.id.editText);
        dateTextView = findViewById(R.id.dateTextView);
        urlTextView = findViewById(R.id.urlTextView);
        progressBar = findViewById(R.id.progressBar);

        // Fetch Image button click listener
        fetchButton.setOnClickListener(v -> {
            selectedDate = getDateFromDatePicker();
            dateTextView.setText(selectedDate);
            new FetchNASAImageTask().execute(selectedDate);
        });

        // View HD Image button click listener
        hdImageButton.setOnClickListener(v -> {
            if (hdImageUrl != null && !hdImageUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hdImageUrl));
                startActivity(browserIntent);
            } else {
                Toast.makeText(MainActivity.this, "HD Image URL is not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Save Image button click listener
        saveButton.setOnClickListener(v -> {
            String note = editText.getText().toString();
            if (selectedDate != null && imageUrl != null && hdImageUrl != null) {
                NasaImage nasaImage = new NasaImage(selectedDate, imageUrl, hdImageUrl, note);
                ImageStorage.saveImage(MainActivity.this, nasaImage);
                Toast.makeText(MainActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Nothing to save", Toast.LENGTH_SHORT).show();
            }
        });

        // View Saved Images button click listener
        viewSavedButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedImagesActivity.class);
            startActivity(intent);
        });
    }

    // Method to get the selected date from the DatePicker
    private String getDateFromDatePicker() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is 0-indexed
        int year = datePicker.getYear();

        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

    // Inflate the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();  // Use the utility method from BaseActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // AsyncTask to fetch the NASA image based on the selected date
    private class FetchNASAImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            urlTextView.setText("");
            Log.d("FetchNASAImageTask", "Fetching image started");
        }

        @Override
        protected String doInBackground(String... params) {
            String date = params[0];
            String urlString = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + date;
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                Log.d("FetchNASAImageTask", "Successfully fetched data");
                return result.toString();
            } catch (Exception e) {
                Log.e("FetchNASAImageTask", "Error fetching image", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    imageUrl = jsonObject.getString("url");
                    hdImageUrl = jsonObject.getString("hdurl");

                    urlTextView.setText(imageUrl);

                    Snackbar.make(findViewById(android.R.id.content), "Image fetched successfully", Snackbar.LENGTH_LONG).show();
                    Log.d("FetchNASAImageTask", "Image URL: " + imageUrl);

                } catch (Exception e) {
                    Log.e("FetchNASAImageTask", "Error parsing JSON", e);
                    Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                Log.d("FetchNASAImageTask", "Result is null");
            }
        }
    }

    @Override
    protected String getDefaultHelpMessage() {
        return "Instructions for Main Activity:\n1. Select a date to view NASA's image of the day.\n2. Fetch and save images.";
    }
}
