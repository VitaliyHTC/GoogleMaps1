package com.vitaliyhtc.googlemaps1.model;

public class MarkerWrap {
    private MarkerInfo mMarkerInfo;

    public MarkerWrap() {
    }

    public MarkerWrap(MarkerInfo markerInfo) {
        mMarkerInfo = markerInfo;
    }

    public MarkerInfo getMarkerInfo() {
        return mMarkerInfo;
    }

    public void setMarkerInfo(MarkerInfo markerInfo) {
        mMarkerInfo = markerInfo;
    }
}
