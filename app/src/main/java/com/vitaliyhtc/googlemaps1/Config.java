package com.vitaliyhtc.googlemaps1;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

public abstract class Config {

    public static final String KEY_MARKER_ID = "id";

    public static final int RV_SELECTED_ON_BACKGROUND_COLOR = Color.LTGRAY;
    public static final int RV_SELECTED_OFF_BACKGROUND_COLOR = Color.TRANSPARENT;

    public static final LatLng LAT_LNG_KIEV = new LatLng(50.4501D, 30.5234D);
    public static final LatLng LAT_LNG_KAM_POD = new LatLng(48.6833D, 26.5833D);
    public static final LatLng LAT_LNG_CHERNIVTSI = new LatLng(48.2917D, 25.9352D);

    private Config() {
        throw new AssertionError();
    }
}
