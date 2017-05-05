package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.Realm;

public interface MarkerInfoRealmStorage {
    void onStop();
    void saveMarker(Realm realmInstance, MarkerInfo markerInfo);
    void updateMarker(Realm realmInstance, MarkerInfo markerInfo);
    void getMarkerById(Realm realmInstance, String markerId, MarkerInfoRealmStorageImpl.MarkerRetrievedListener listener);
    void deleteMarkerById(Realm realm, String markerId);
    void getAllMarkersAsync(Realm realm, MarkerInfoRealmStorageImpl.AllMarkersResultListener listener);
}
