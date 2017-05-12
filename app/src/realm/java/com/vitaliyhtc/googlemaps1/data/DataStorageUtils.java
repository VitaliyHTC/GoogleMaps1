package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.data.realm.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.data.realm.MarkersListRealmStorageImpl;

public abstract class DataStorageUtils {

    private DataStorageUtils() {
        throw new AssertionError();
    }

    public static MarkerInfoStorage getMarkerInfoStorageInstance() {
        return new MarkerInfoRealmStorageImpl();
    }

    public static MarkersListStorage getMarkersListStorageInstance() {
        return new MarkersListRealmStorageImpl();
    }
}
