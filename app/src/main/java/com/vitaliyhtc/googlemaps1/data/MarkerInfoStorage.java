package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MarkerInfoStorage extends DataStorage {
    void saveMarker(MarkerInfo markerInfo);
    void updateMarker(MarkerInfo markerInfo);
    void getMarkerById(String markerId, MarkerInfoRetrievedListener listener);
    void deleteMarkerById(String markerId);
    void getAllMarkersAsync(MarkerInfoAllMarkersResultListener listener);
}
