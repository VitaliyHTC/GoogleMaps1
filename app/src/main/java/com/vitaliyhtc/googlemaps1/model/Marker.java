package com.vitaliyhtc.googlemaps1.model;

import io.realm.RealmObject;

public class Marker extends RealmObject {

    private double latitude;
    private double longitude;
    private String title;
    private float iconHue;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getIconHue() {
        return iconHue;
    }

    public void setIconHue(float iconHue) {
        this.iconHue = iconHue;
    }
}
