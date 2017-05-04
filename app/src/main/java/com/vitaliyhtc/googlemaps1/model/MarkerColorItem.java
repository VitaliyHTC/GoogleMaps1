package com.vitaliyhtc.googlemaps1.model;

public class MarkerColorItem {

    private int imgResId;
    private String title;
    private float markerHue;

    public MarkerColorItem() {
    }

    public MarkerColorItem(int imgResId, String title, float markerHue) {
        this.imgResId = imgResId;
        this.title = title;
        this.markerHue = markerHue;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getMarkerHue() {
        return markerHue;
    }

    public void setMarkerHue(float markerHue) {
        this.markerHue = markerHue;
    }
}
