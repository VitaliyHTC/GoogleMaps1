package com.vitaliyhtc.googlemaps1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vitaliyhtc.googlemaps1.data.CameraPositionUtils;
import com.vitaliyhtc.googlemaps1.data.MarkerRealmStorage;
import com.vitaliyhtc.googlemaps1.ui.MarkerDialog;
import com.vitaliyhtc.googlemaps1.ui.MarkerInfoOptionsDialog;
import com.vitaliyhtc.googlemaps1.util.PermissionUtils;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

// TODO: 03/05/17 mvp missing
// TODO: 03/05/17 ViewPager with 2 tabs. 1-st to show all markers on map, 2-st to show all markers in list
// TODO: 03/05/17 options to switch between map styles (satalite, terrain, mixed, custom)
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

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
        CameraPositionUtils.saveCameraPositionFromMap(MapsActivity.this, mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other work here
        mMarkers = new HashMap<>();
        initUiSettings();
        CameraPositionUtils.restoreCameraPositionOnMap(MapsActivity.this, mMap);
        enableMyLocation();
        initListeners();
        restoreMarkersOnMap();
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
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(MapsActivity.this, R.string.map_ui_my_location_button_click_toast_message, Toast.LENGTH_SHORT).show();
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
                return false;
            }
        });
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
                MarkerRealmStorage.saveMarker(marker);
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

    private void actionOnMarkerClick(Marker marker) {
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
                MarkerRealmStorage.updateMarker(marker);
                if (mMarkers.containsKey(marker.getId())) {
                    mMarkers.get(marker.getId()).remove();
                }
                placeMarkerOnMap(marker);
            }
        });
        markerDialog.show(getSupportFragmentManager(), "MarkerDialog");
    }

    private void actionDeleteMarker(final Marker marker) {
        MarkerRealmStorage.deleteMarkerById((String) marker.getTag());
        mMarkers.remove(marker.getTag());
        marker.remove();
    }

    private void restoreMarkersOnMap() {
        Realm realmInstance = Realm.getDefaultInstance();
        // TODO: 03/05/17 marker request async
        // TODO: 03/05/17 clean up(rename marker model
        for (com.vitaliyhtc.googlemaps1.model.Marker marker : MarkerRealmStorage.getAllMarkers(realmInstance)) {
            placeMarkerOnMap(marker);
        }
        realmInstance.close();
    }

}
