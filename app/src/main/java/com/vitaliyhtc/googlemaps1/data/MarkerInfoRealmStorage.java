package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.Realm;

// TODO: 06/05/17 separate abstraction from realization, there should be single abstraction that can be implemented in different ways
// for example method void saveMarker(MarkerInfo markerInfo); can be implemented either with Realm, SQLite or Firebase realization
public interface MarkerInfoRealmStorage {
    void onStop();
    void saveMarker(Realm realmInstance, MarkerInfo markerInfo);
    void updateMarker(Realm realmInstance, MarkerInfo markerInfo);
    void getMarkerById(Realm realmInstance, String markerId, MarkerInfoRealmStorageImpl.MarkerRetrievedListener listener);
    void deleteMarkerById(Realm realm, String markerId);
    void getAllMarkersAsync(Realm realm, MarkerInfoRealmStorageImpl.AllMarkersResultListener listener);
}
