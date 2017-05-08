package com.vitaliyhtc.googlemaps1.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MapsPresenter extends BasePresenter {
    void onMapTypeSwitchClick();
    void onMapTypeSelected(int mapType);
    void getAllMarkers();
    void actionNewMarker(LatLng latLng);
    void onNewMarkerDialogSuccess(MarkerInfo markerInfo);
    void actionMarkerOptions(Marker marker);
    void actionEditMarker(Marker marker);
    void actionDeleteMarker(Marker marker);
    void onEditMarkerDialogSuccess(MarkerInfo markerInfo);
}
