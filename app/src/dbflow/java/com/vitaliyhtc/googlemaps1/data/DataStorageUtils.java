package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.data.dbflow.MarkerInfoDBFlowStorageImpl;
import com.vitaliyhtc.googlemaps1.data.dbflow.MarkerListDBFlowStorageImpl;

public abstract class DataStorageUtils {

    private DataStorageUtils() {
        throw new AssertionError();
    }

    public static MarkerInfoStorage getMarkerInfoStorageInstance() {
        return new MarkerInfoDBFlowStorageImpl();
    }

    public static MarkersListStorage getMarkersListStorageInstance() {
        return new MarkerListDBFlowStorageImpl();
    }
}
