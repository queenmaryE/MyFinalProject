package com.example.myfinalproject;
import java.io.Serializable;

public class NasaImage implements Serializable {
    private String date;
    private String url;
    private String hdUrl;
    private String note;

    public NasaImage(String date, String url, String hdUrl, String note) {
        this.date = date;
        this.url = url;
        this.hdUrl = hdUrl;
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Date: " + date + "\nNote: " + note;
    }
}
