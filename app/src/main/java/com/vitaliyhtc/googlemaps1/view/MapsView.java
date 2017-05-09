package com.vitaliyhtc.googlemaps1.view;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.List;

public interface MapsView extends BaseView {

    void displayMapTypePickDialog();
    void applyMapType(int mapType);
    // TODO: 09/05/17 placeMarkerOnMapAndGet don't used anywhere accept MapsFragment, no need to put it here
    Marker placeMarkerOnMapAndGet(MarkerInfo markerInfo);
    void onAllMarkersRetrieved(List<MarkerInfo> markers);
    void displayNewMarkerDialog(LatLng latLng);
    void placeNewMarkerOnMap(MarkerInfo markerInfo);
    void displayMarkerInfoAndOptionsDialog(Marker marker);
    void displayEditMarkerDialog(MarkerInfo markerInfo);
    void updateMarkerUi(MarkerInfo markerInfo);
    void deleteMarkerUi(Marker marker);
}
