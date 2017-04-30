package com.vitaliyhtc.googlemaps1.model;

public class MarkerWrap {
    private Marker mMarker;

    public MarkerWrap() {
    }

    public MarkerWrap(Marker marker) {
        mMarker = marker;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }
}
