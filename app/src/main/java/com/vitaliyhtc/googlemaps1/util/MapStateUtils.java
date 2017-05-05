package com.vitaliyhtc.googlemaps1.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * This class save and restore map state to/from sharedPreferences.
 */
public abstract class MapStateUtils {

    private static final String KEY_CAMERA_POSITION_SETTINGS = "CameraPosition_Settings";
    private static final String KEY_CAMERA_POSITION_BEARING = "bearing";
    private static final String KEY_CAMERA_POSITION_TARGET_LAT = "target_lat";
    private static final String KEY_CAMERA_POSITION_TARGET_LNG = "target_lng";
    private static final String KEY_CAMERA_POSITION_TILT = "tilt";
    private static final String KEY_CAMERA_POSITION_ZOOM = "zoom";

    private static final String KEY_MAP_TYPE_SETTINGS = "MapType_Settings";
    private static final String KEY_MAP_TYPE_KEY = "MapType_key";

    private MapStateUtils() {
        throw new AssertionError();
    }

    public static void saveCameraPositionFromMap(Context ctxt, GoogleMap map) {
        CameraPosition cameraPosition = map.getCameraPosition();

        SharedPreferences.Editor editor = ctxt.getSharedPreferences(KEY_CAMERA_POSITION_SETTINGS, 0).edit();
        editor.putFloat(KEY_CAMERA_POSITION_BEARING, cameraPosition.bearing);
        editor.putFloat(KEY_CAMERA_POSITION_TARGET_LAT, (float) cameraPosition.target.latitude);
        editor.putFloat(KEY_CAMERA_POSITION_TARGET_LNG, (float) cameraPosition.target.longitude);
        editor.putFloat(KEY_CAMERA_POSITION_TILT, cameraPosition.tilt);
        editor.putFloat(KEY_CAMERA_POSITION_ZOOM, cameraPosition.zoom);
        editor.apply();
    }

    public static void restoreCameraPositionOnMap(Context ctxt, GoogleMap map) {
        // default values move camera to Sydney
        SharedPreferences pref = ctxt.getSharedPreferences(KEY_CAMERA_POSITION_SETTINGS, 0);
        float bearing = pref.getFloat(KEY_CAMERA_POSITION_BEARING, 0);
        double lat = (double) pref.getFloat(KEY_CAMERA_POSITION_TARGET_LAT, -34);
        double lng = (double) pref.getFloat(KEY_CAMERA_POSITION_TARGET_LNG, 151);
        float tilt = pref.getFloat(KEY_CAMERA_POSITION_TILT, 0);
        float zoom = pref.getFloat(KEY_CAMERA_POSITION_ZOOM, 9);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(lat, lng), zoom, tilt, bearing);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static void saveMapType(Context ctxt, GoogleMap map) {
        SharedPreferences.Editor editor = ctxt.getSharedPreferences(KEY_MAP_TYPE_SETTINGS, 0).edit();
        editor.putInt(KEY_MAP_TYPE_KEY, map.getMapType());
        editor.apply();
    }

    public static void restoreMapType(Context ctxt, GoogleMap map) {
        SharedPreferences pref = ctxt.getSharedPreferences(KEY_MAP_TYPE_SETTINGS, 0);
        map.setMapType(pref.getInt(KEY_MAP_TYPE_KEY, GoogleMap.MAP_TYPE_NORMAL));
    }
}
