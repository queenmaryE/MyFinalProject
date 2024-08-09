package com.example.myfinalproject;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageStorage {
    private static final String PREFS_NAME = "NasaImages";
    private static final String IMAGES_KEY = "SavedImages";

    public static void saveImage(Context context, NasaImage nasaImage) {
        List<NasaImage> images = getSavedImages(context);
        images.add(nasaImage);
        saveImagesList(context, images);
    }

    public static List<NasaImage> getSavedImages(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(IMAGES_KEY, null);
        Type type = new TypeToken<ArrayList<NasaImage>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    private static void saveImagesList(Context context, List<NasaImage> images) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(images);
        editor.putString(IMAGES_KEY, json);
        editor.apply();
    }

    public static void deleteImage(Context context, NasaImage image) {
        List<NasaImage> images = getSavedImages(context);
        images.remove(image);
        saveImagesList(context, images);
    }
}
