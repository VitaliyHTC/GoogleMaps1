package com.vitaliyhtc.googlemaps1;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vitaliyhtc.googlemaps1.util.PermissionUtils;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String KEY_CAMERA_POSITION_SETTINGS = "CameraPosition_Settings";
    private static final String KEY_CAMERA_POSITION_BEARING = "bearing";
    private static final String KEY_CAMERA_POSITION_TARGET_LAT = "target_lat";
    private static final String KEY_CAMERA_POSITION_TARGET_LNG = "target_lng";
    private static final String KEY_CAMERA_POSITION_TILT = "tilt";
    private static final String KEY_CAMERA_POSITION_ZOOM = "zoom";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private Map<String, Marker> mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCameraPosition();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other work here
        mMarkers = new HashMap<>();
        initUiSettings();
        restoreCameraPosition();
        enableMyLocation();
        initListeners();
        restoreMarkerOnMap();
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, R.string.map_ui_my_location_button_click_toast_message, Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private void initUiSettings() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private void initListeners() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                actionOnMapLongClick(latLng);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                actionOnMarkerClick(marker);
                return false;
            }
        });
    }

    private void actionOnMapLongClick(LatLng latLng) {
        MarkerDialog markerDialog = new MarkerDialog();
        markerDialog.setLatLng(latLng);
        markerDialog.setMarkerDialogCallback(new MarkerDialog.MarkerDialogCallback() {
            @Override
            public void onMarkerDialogSuccess(com.vitaliyhtc.googlemaps1.model.Marker marker) {
                saveMarkerToRealm(marker);
                placeMarkerOnMap(marker);
            }
        });
        markerDialog.show(getSupportFragmentManager(), "MarkerDialog");
    }

    private void placeMarkerOnMap(com.vitaliyhtc.googlemaps1.model.Marker marker) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(marker.getLatitude(), marker.getLongitude()))
                .title(marker.getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(marker.getIconHue()));
        Marker marker1 = mMap.addMarker(markerOptions);
        marker1.setTag(marker.getId());
        mMarkers.put(marker.getId(), marker1);
    }

    private void saveMarkerToRealm(final com.vitaliyhtc.googlemaps1.model.Marker marker) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
        realm.close();
    }

    private void actionOnMarkerClick(Marker marker) {
        // TODO. 6. onMarkerClick > open new screen with info about this marker.
        // Every marker you can edit or delete.

        MarkerInfoOptionsDialog dialog = new MarkerInfoOptionsDialog();
        dialog.setMarker(marker);
        dialog.setCallback(new MarkerInfoOptionsDialog.MarkerInfoOptionsDialogCallback() {
            @Override
            public void onMarkerEdit(Marker marker) {
                actionEditMarker(marker);
            }

            @Override
            public void onMarkerDelete(Marker marker) {
                actionDeleteMarker(marker);
            }
        });
        dialog.show(getSupportFragmentManager(), "MarkerInfoOptionsDialog");
    }

    private void actionEditMarker(Marker marker) {
        MarkerDialog markerDialog = new MarkerDialog();
        markerDialog.setMarker(marker);
        markerDialog.setMarkerDialogCallback(new MarkerDialog.MarkerDialogCallback() {
            @Override
            public void onMarkerDialogSuccess(com.vitaliyhtc.googlemaps1.model.Marker marker) {
                updateMarkerInRealm(marker);
                if(mMarkers.containsKey(marker.getId())){
                    mMarkers.get(marker.getId()).remove();
                }
                placeMarkerOnMap(marker);
            }
        });
        markerDialog.show(getSupportFragmentManager(), "MarkerDialog");
    }

    private void updateMarkerInRealm(final com.vitaliyhtc.googlemaps1.model.Marker marker){
        if (marker != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    com.vitaliyhtc.googlemaps1.model.Marker marker1 = realm
                            .where(com.vitaliyhtc.googlemaps1.model.Marker.class)
                            .equalTo(KEY_MARKER_ID, marker.getId())
                            .findFirst();

                    marker1.setTitle(marker.getTitle());
                    marker1.setIconHue(marker.getIconHue());
                }
            });
            realm.close();
        }
    }

    private void actionDeleteMarker(final Marker marker) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                com.vitaliyhtc.googlemaps1.model.Marker marker1 = realm
                        .where(com.vitaliyhtc.googlemaps1.model.Marker.class)
                        .equalTo(KEY_MARKER_ID, (String) marker.getTag())
                        .findFirst();
                marker1.deleteFromRealm();
            }
        });
        realm.close();
        mMarkers.remove((String) marker.getTag());
        marker.remove();
    }

    private void restoreMarkerOnMap() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<com.vitaliyhtc.googlemaps1.model.Marker> markers =
                        realm.where(com.vitaliyhtc.googlemaps1.model.Marker.class).findAll();
                for (com.vitaliyhtc.googlemaps1.model.Marker marker : markers) {
                    placeMarkerOnMap(marker);
                }
            }
        });
        realm.close();
    }

    private void saveCameraPosition() {
        CameraPosition cameraPosition = mMap.getCameraPosition();

        SharedPreferences.Editor editor = MapsActivity.this.getSharedPreferences(KEY_CAMERA_POSITION_SETTINGS, 0).edit();
        editor.putFloat(KEY_CAMERA_POSITION_BEARING, cameraPosition.bearing);
        editor.putFloat(KEY_CAMERA_POSITION_TARGET_LAT, (float) cameraPosition.target.latitude);
        editor.putFloat(KEY_CAMERA_POSITION_TARGET_LNG, (float) cameraPosition.target.longitude);
        editor.putFloat(KEY_CAMERA_POSITION_TILT, cameraPosition.tilt);
        editor.putFloat(KEY_CAMERA_POSITION_ZOOM, cameraPosition.zoom);
        editor.apply();
    }

    private void restoreCameraPosition() {
        // default values move camera to Sydney
        SharedPreferences pref = MapsActivity.this.getSharedPreferences(KEY_CAMERA_POSITION_SETTINGS, 0);
        float bearing = pref.getFloat(KEY_CAMERA_POSITION_BEARING, 0);
        double lat = (double) pref.getFloat(KEY_CAMERA_POSITION_TARGET_LAT, -34);
        double lng = (double) pref.getFloat(KEY_CAMERA_POSITION_TARGET_LNG, 151);
        float tilt = pref.getFloat(KEY_CAMERA_POSITION_TILT, 0);
        float zoom = pref.getFloat(KEY_CAMERA_POSITION_ZOOM, 9);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(lat, lng), zoom, tilt, bearing);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
