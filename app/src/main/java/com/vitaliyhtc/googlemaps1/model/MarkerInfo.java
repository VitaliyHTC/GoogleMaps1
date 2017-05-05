package com.vitaliyhtc.googlemaps1.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarkerInfo extends RealmObject {

    @PrimaryKey
    private String id;
    private double latitude;
    private double longitude;
    private String title;
    private float iconHue;


    public String getId() {
        return id;
    }

    public static MarkerInfo clone(MarkerInfo markerInfo){
        MarkerInfo markerInfo1 = new MarkerInfo();

        markerInfo1.setId(markerInfo.getId());
        markerInfo1.setTitle(markerInfo.getTitle());
        markerInfo1.setIconHue(markerInfo.getIconHue());
        markerInfo1.setLatitude(markerInfo.getLatitude());
        markerInfo1.setLongitude(markerInfo.getLongitude());

        return markerInfo1;
    }

    public void setId(String id) {
        this.id = id;
    }

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
