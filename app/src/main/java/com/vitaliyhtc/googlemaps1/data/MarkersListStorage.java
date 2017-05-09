package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.RealmResults;

public interface MarkersListStorage extends DataStorage {
    // TODO: 09/05/17 Realm  result leaked
    RealmResults<MarkerInfo> getRealmResultWithMarkerInfo();
}
