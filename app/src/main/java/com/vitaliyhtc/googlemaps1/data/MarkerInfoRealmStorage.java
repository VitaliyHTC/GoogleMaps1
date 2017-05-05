package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.Realm;

public interface MarkerInfoRealmStorage {
    void onStop();
    void saveMarker(MarkerInfo markerInfo);
    void updateMarker(MarkerInfo markerInfo);
    MarkerInfo getMarkerById(Realm realmInstance, String markerId);
    void deleteMarkerById(Realm realm, String markerId);
    void getAllMarkersAsync(Realm realm, MarkerInfoRealmStorageImpl.AllMarkersResultListener listener);
}
