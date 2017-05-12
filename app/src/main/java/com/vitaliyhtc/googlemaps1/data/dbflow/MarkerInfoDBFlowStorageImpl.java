package com.vitaliyhtc.googlemaps1.data.dbflow;

import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersDeletedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersResultListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRetrievedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.List;

public class MarkerInfoDBFlowStorageImpl implements MarkerInfoStorage {

    @Override
    public void initResources() {

    }

    @Override
    public void releaseResources() {

    }

    @Override
    public void saveMarker(MarkerInfo markerInfo) {

    }

    @Override
    public void saveMarkersListSynchronously(List<MarkerInfo> markers) {

    }

    @Override
    public void updateMarker(MarkerInfo markerInfo) {

    }

    @Override
    public void getMarkerById(String markerId, MarkerInfoRetrievedListener listener) {

    }

    @Override
    public void deleteMarkerById(String markerId) {

    }

    @Override
    public void getAllMarkersAsync(MarkerInfoAllMarkersResultListener listener) {

    }

    @Override
    public void deleteAllMarkers(MarkerInfoAllMarkersDeletedListener listener) {

    }
}
