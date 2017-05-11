package com.vitaliyhtc.googlemaps1.view;

import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerInfoItem;

import java.util.List;

public interface MapsView extends BaseView {

    void displayMapTypePickDialog();
    void applyMapType(int mapType);
    void onAllMarkersRetrieved(List<MarkerInfo> markers);
    void displayNewMarkerDialog(LatLng latLng);
    void placeNewMarkerOnMap(MarkerInfo markerInfo);
    void displayMarkerInfoAndOptionsDialog(MarkerInfoItem markerInfoItem);
    void displayEditMarkerDialog(MarkerInfo markerInfo);
    void updateMarkerUi(MarkerInfo markerInfo);
    void deleteMarkerUi(MarkerInfoItem markerInfoItem);
}
