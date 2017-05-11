package com.vitaliyhtc.googlemaps1.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerInfoItem implements ClusterItem {

    private MarkerInfo mMarkerInfo;

    public MarkerInfoItem(MarkerInfo markerInfo) {
        mMarkerInfo = MarkerInfo.clone(markerInfo);
    }

    public MarkerInfo getMarkerInfo() {
        return mMarkerInfo;
    }

    public void setMarkerInfo(MarkerInfo markerInfo) {
        mMarkerInfo = markerInfo;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mMarkerInfo.getLatitude(), mMarkerInfo.getLongitude());
    }

    @Override
    public String getTitle() {
        return mMarkerInfo.getTitle();
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
