package com.vitaliyhtc.googlemaps1;

import android.graphics.Color;

public abstract class Config {

    public static final String KEY_MARKER_ID = "id";

    public static final int RV_SELECTED_ON_BACKGROUND_COLOR = Color.LTGRAY;
    public static final int RV_SELECTED_OFF_BACKGROUND_COLOR = Color.TRANSPARENT;

    private Config() {
        throw new AssertionError();
    }
}
