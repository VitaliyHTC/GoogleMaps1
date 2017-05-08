package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.RealmResults;

public interface MarkersListStorage extends DataStorage {
    RealmResults<MarkerInfo> getRealmResultWithMarkerInfo();
}
