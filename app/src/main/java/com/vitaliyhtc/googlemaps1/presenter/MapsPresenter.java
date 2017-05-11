package com.vitaliyhtc.googlemaps1.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerInfoItem;

public interface MapsPresenter extends BasePresenter {
    void onMapTypeSwitchClick();
    void onMapTypeSelected(int mapType);
    void getAllMarkers();
    void actionNewMarker(LatLng latLng);
    void onNewMarkerDialogSuccess(MarkerInfo markerInfo);
    void actionMarkerOptions(MarkerInfoItem markerInfoItem);
    void actionEditMarker(MarkerInfoItem markerInfoItem);
    void actionDeleteMarker(MarkerInfoItem markerInfoItem);
    void onEditMarkerDialogSuccess(MarkerInfo markerInfo);
}
