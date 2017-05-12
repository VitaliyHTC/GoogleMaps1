package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.Config;
import com.vitaliyhtc.googlemaps1.ConfigBuildFlavors;
import com.vitaliyhtc.googlemaps1.MyApplication;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.data.dbflow.MarkerInfoDBFlowStorageImpl;
import com.vitaliyhtc.googlemaps1.data.dbflow.MarkerListDBFlowStorageImpl;
import com.vitaliyhtc.googlemaps1.data.realm.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.data.realm.MarkersListRealmStorageImpl;

public abstract class DataStorageUtils {

    private DataStorageUtils() {
        throw new AssertionError();
    }

    @SuppressWarnings("ConstantConditions")
    public static MarkerInfoStorage getMarkerInfoStorageInstance() {
        MarkerInfoStorage markerInfoStorage;
        if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_REALM) {
            markerInfoStorage = new MarkerInfoRealmStorageImpl();
        } else if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_DBFLOW) {
            markerInfoStorage = new MarkerInfoDBFlowStorageImpl();
        } else {
            throw new IllegalArgumentException(MyApplication.getContext().getString(R.string.config_build_flavors_error));
        }
        return markerInfoStorage;
    }

    @SuppressWarnings("ConstantConditions")
    public static MarkersListStorage getMarkersListStorageInstance() {
        MarkersListStorage markersListStorage;
        if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_REALM) {
            markersListStorage = new MarkersListRealmStorageImpl();
        } else if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_DBFLOW) {
            markersListStorage = new MarkerListDBFlowStorageImpl();
        } else {
            throw new IllegalArgumentException(MyApplication.getContext().getString(R.string.config_build_flavors_error));
        }
        return markersListStorage;
    }
}
