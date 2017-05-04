package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.List;

import io.realm.Realm;

public interface MarkerInfoRealmStorage {
    void saveMarker(MarkerInfo markerInfo);
    void updateMarker(final MarkerInfo markerInfo);
    MarkerInfo getMarkerById(Realm realmInstance, final String markerId);
    void deleteMarkerById(final String markerId);
    List<MarkerInfo> getAllMarkers(Realm realmInstance);
}
