package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.RealmResults;

public interface MarkersListStorage extends DataStorage {
    // TODO: 09/05/17 Realm  result leaked
    // See: https://github.com/realm/realm-android-adapters/blob/master/adapters/src/main/java/io/realm/RealmRecyclerViewAdapter.java
    RealmResults<MarkerInfo> getRealmResultWithMarkerInfo();
}
