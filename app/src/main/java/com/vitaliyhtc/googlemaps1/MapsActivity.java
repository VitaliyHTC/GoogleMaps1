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
import com.vitaliyhtc.googlemaps1.data.MapStateUtils;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorage;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.ui.MapTypeDialog;
import com.vitaliyhtc.googlemaps1.ui.MarkerDialog;
import com.vitaliyhtc.googlemaps1.ui.MarkerInfoOptionsDialog;
import com.vitaliyhtc.googlemaps1.util.PermissionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

// TODO: 03/05/17 mvp missing
// TODO: 03/05/17 ViewPager with 2 tabs. 1-st to show all markers on map, 2-st to show all markers in list

// 03/05/17 options to switch between map styles (satalite, terrain, mixed, custom)
// Ooops. MapType selection done. For mapStyles i can use this example:
// https://github.com/googlemaps/android-samples/blob/master/ApiDemos/app/src/main/java/com/example/mapdemo/StyledMapDemoActivity.java
// https://developers.google.com/maps/documentation/android-api/styling
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private Map<String, Marker> mMarkers;

    private MarkerInfoRealmStorage mMarkerInfoRealmStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapStateUtils.saveCameraPositionFromMap(MapsActivity.this, mMap);
        MapStateUtils.saveMapType(MapsActivity.this, mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other work here
        mMarkers = new HashMap<>();
        mMarkerInfoRealmStorage = new MarkerInfoRealmStorageImpl();
        initUiSettings();
        MapStateUtils.restoreCameraPositionOnMap(MapsActivity.this, mMap);
        MapStateUtils.restoreMapType(MapsActivity.this, mMap);
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

    @OnClick(R.id.iv_map_type_switch)
    protected void onMapTypeSwitchClick() {
        MapTypeDialog mapTypeDialog = new MapTypeDialog();
        mapTypeDialog.setSelectedMapType(mMap.getMapType());
        mapTypeDialog.setMapTypeSelectedListener(new MapTypeDialog.MapTypeSelectedListener() {
            @Override
            public void onMapTypeSelected(int mapType) {
                mMap.setMapType(mapType);
            }
        });
        mapTypeDialog.show(getSupportFragmentManager(), "MapTypeDialog");
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
            public void onMarkerDialogSuccess(MarkerInfo markerInfo) {
                if (markerInfo.getId() == null) {
                    markerInfo.setId(UUID.randomUUID().toString());
                }
                mMarkerInfoRealmStorage.saveMarker(markerInfo);
                placeMarkerOnMap(markerInfo);
            }
        });
        markerDialog.show(getSupportFragmentManager(), "MarkerDialog");
    }

    private void placeMarkerOnMap(MarkerInfo markerInfo) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(markerInfo.getLatitude(), markerInfo.getLongitude()))
                .title(markerInfo.getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(markerInfo.getIconHue()));
        Marker marker1 = mMap.addMarker(markerOptions);
        marker1.setTag(markerInfo.getId());
        mMarkers.put(markerInfo.getId(), marker1);
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

        Realm realmInstance = Realm.getDefaultInstance();
        MarkerInfo markerInfo = new MarkerInfo();
        MarkerInfo markerInfo1 = mMarkerInfoRealmStorage.getMarkerById(realmInstance, (String) marker.getTag());
        markerInfo.setId(markerInfo1.getId());
        markerInfo.setTitle(markerInfo1.getTitle());
        markerInfo.setLatitude(markerInfo1.getLatitude());
        markerInfo.setLongitude(markerInfo1.getLongitude());
        markerInfo.setIconHue(markerInfo1.getIconHue());
        realmInstance.close();

        markerDialog.setMarkerInfo(markerInfo);
        markerDialog.setMarkerDialogCallback(new MarkerDialog.MarkerDialogCallback() {
            @Override
            public void onMarkerDialogSuccess(MarkerInfo markerInfo) {
                mMarkerInfoRealmStorage.updateMarker(markerInfo);
                if (mMarkers.containsKey(markerInfo.getId())) {
                    mMarkers.get(markerInfo.getId()).remove();
                }
                placeMarkerOnMap(markerInfo);
            }
        });
        markerDialog.show(getSupportFragmentManager(), "MarkerDialog");
    }

    private void actionDeleteMarker(final Marker marker) {
        mMarkerInfoRealmStorage.deleteMarkerById((String) marker.getTag());
        mMarkers.remove(marker.getTag());
        marker.remove();
    }

    private void restoreMarkersOnMap() {
        // TODO: 03/05/17 marker request async
        Realm realmInstance = Realm.getDefaultInstance();
        for (MarkerInfo marker : mMarkerInfoRealmStorage.getAllMarkers(realmInstance)) {
            placeMarkerOnMap(marker);
        }
        realmInstance.close();
    }

}
