package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.List;

public interface MarkerInfoStorage extends DataStorage {
    void saveMarker(MarkerInfo markerInfo);
    void saveMarkersListSynchronously(List<MarkerInfo> markers);
    void updateMarker(MarkerInfo markerInfo);
    void getMarkerById(String markerId, MarkerInfoRetrievedListener listener);
    void deleteMarkerById(String markerId);
    void getAllMarkersAsync(MarkerInfoAllMarkersResultListener listener);
    void deleteAllMarkers(MarkerInfoAllMarkersDeletedListener listener);
}
