package com.vitaliyhtc.googlemaps1.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.view.dialog.MapTypeDialog;
import com.vitaliyhtc.googlemaps1.view.dialog.MarkerDialog;
import com.vitaliyhtc.googlemaps1.view.dialog.MarkerInfoOptionsDialog;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerInfoItem;
import com.vitaliyhtc.googlemaps1.presenter.MapsPresenter;
import com.vitaliyhtc.googlemaps1.presenter.MapsPresenterImpl;
import com.vitaliyhtc.googlemaps1.util.MapStateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsFragment extends Fragment
        implements MapsView, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private MapsPresenter mMapsPresenter;

    private GoogleMap mMap;
    private ClusterManager<MarkerInfoItem> mClusterManager;

    private Map<String, MarkerInfoItem> mMarkerItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        // TO.DO: 09/05/17 did you checked this after refactoring?
        // present on 4.0.3 api 15, not reproduced on 6.0.1 api 23
        // fix for mysterious black view
        // http://stackoverflow.com/questions/13837697/viewpager-with-google-maps-api-v2-mysterious-black-view
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) view).addView(frameLayout,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMarkerItems = new HashMap<>();

        mMapsPresenter = new MapsPresenterImpl();
        mMapsPresenter.onAttachView(MapsFragment.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsFragment.this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapsPresenter.onDetachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        MapStateUtils.saveCameraPositionFromMap(getContext(), mMap);
        MapStateUtils.saveMapType(getContext(), mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mClusterManager = new ClusterManager<>(getContext(), googleMap);
        mClusterManager.setRenderer(new IconRender(getContext(), googleMap, mClusterManager));
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerInfoItem>() {
            @Override
            public boolean onClusterItemClick(MarkerInfoItem markerInfoItem) {
                mMapsPresenter.actionMarkerOptions(markerInfoItem);
                return false;
            }
        });

        initUiSettings();
        MapStateUtils.restoreCameraPositionOnMap(getContext(), mMap);
        MapStateUtils.restoreMapType(getContext(), mMap);
        requestPermissionAndEnableMyLocation();
        initListeners();
        mMapsPresenter.getAllMarkers();
    }

    @OnClick(R.id.iv_map_type_switch)
    protected void onMapTypeSwitchClick() {
        mMapsPresenter.onMapTypeSwitchClick();
    }

    @Override
    public void displayMapTypePickDialog() {
        MapTypeDialog mapTypeDialog = new MapTypeDialog();
        mapTypeDialog.setSelectedMapType(mMap.getMapType());
        mapTypeDialog.setMapTypeSelectedListener(new MapTypeDialog.MapTypeSelectedListener() {
            @Override
            public void onMapTypeSelected(int mapType) {
                mMapsPresenter.onMapTypeSelected(mapType);
            }
        });
        mapTypeDialog.show(getChildFragmentManager(), "MapTypeDialog");
    }

    @Override
    public void applyMapType(int mapType) {
        mMap.setMapType(mapType);
    }

    @Override
    public void onAllMarkersRetrieved(List<MarkerInfo> markers) {
        for (MarkerInfo markerInfo : markers) {
            placeNewMarkerOnMap(markerInfo);
        }
    }

    @Override
    public void placeNewMarkerOnMap(MarkerInfo markerInfo) {
        MarkerInfoItem marker = placeMarkerOnMapAndGet(markerInfo);
        mMarkerItems.put(marker.getMarkerInfo().getId(), marker);
    }

    @Override
    public void displayNewMarkerDialog(LatLng latLng) {
        MarkerDialog markerDialog = new MarkerDialog();
        markerDialog.setLatLng(latLng);
        markerDialog.setMarkerDialogCallback(new MarkerDialog.MarkerDialogCallback() {
            @Override
            public void onMarkerDialogSuccess(MarkerInfo markerInfo) {
                mMapsPresenter.onNewMarkerDialogSuccess(markerInfo);
            }
        });
        markerDialog.show(getChildFragmentManager(), "MarkerDialog");
    }

    @Override
    public void displayMarkerInfoAndOptionsDialog(MarkerInfoItem markerInfoItem) {
        MarkerInfoOptionsDialog dialog = new MarkerInfoOptionsDialog();
        dialog.setMarker(markerInfoItem);
        dialog.setCallback(new MarkerInfoOptionsDialog.MarkerInfoOptionsDialogCallback() {
            @Override
            public void onMarkerEdit(MarkerInfoItem markerInfoItem) {
                mMapsPresenter.actionEditMarker(markerInfoItem);
            }

            @Override
            public void onMarkerDelete(MarkerInfoItem markerInfoItem) {
                mMapsPresenter.actionDeleteMarker(markerInfoItem);
            }
        });
        dialog.show(getChildFragmentManager(), "MarkerInfoOptionsDialog");
    }

    @Override
    public void displayEditMarkerDialog(MarkerInfo markerInfo) {
        MarkerDialog markerDialog = new MarkerDialog();
        markerDialog.setMarkerInfo(markerInfo);
        markerDialog.setMarkerDialogCallback(new MarkerDialog.MarkerDialogCallback() {
            @Override
            public void onMarkerDialogSuccess(MarkerInfo markerInfo) {
                mMapsPresenter.onEditMarkerDialogSuccess(markerInfo);
            }
        });
        markerDialog.show(getChildFragmentManager(), "MarkerDialog");
    }

    @Override
    public void updateMarkerUi(MarkerInfo markerInfo) {
        if (mMarkerItems.containsKey(markerInfo.getId())) {
            mClusterManager.removeItem(mMarkerItems.get(markerInfo.getId()));
            mMarkerItems.remove(markerInfo.getId());
        }
        placeNewMarkerOnMap(markerInfo);
    }

    @Override
    public void deleteMarkerUi(MarkerInfoItem markerInfoItem) {
        mClusterManager.removeItem(mMarkerItems.get(markerInfoItem.getMarkerInfo().getId()));
        mMarkerItems.remove(markerInfoItem.getMarkerInfo().getId());
        mClusterManager.cluster();
    }


    private void requestPermissionAndEnableMyLocation() {
        requestAccessFineLocationPermission();
    }

    @SuppressWarnings({"MissingPermission"})
    private void requestAccessFineLocationPermission() {
        Dexter.withActivity(MapsFragment.this.getActivity())
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (mMap != null) {
                            // Access to the location has been granted to the app.
                            mMap.setMyLocationEnabled(true);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        onPermissionDeniedResume(response);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        onPermissionRationaleShouldBeShownResume(permission, token);
                    }
                }).check();
    }

    private void onPermissionDeniedResume(PermissionDeniedResponse response) {
        // Do nothing.
    }

    private void onPermissionRationaleShouldBeShownResume(PermissionRequest permission, final PermissionToken token) {
        new AlertDialog.Builder(getContext()).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }

    private void initUiSettings() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                showToastShort(getString(R.string.map_ui_my_location_button_click_toast_message));
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
                return false;
            }
        });
    }

    // TODO: 22.05.17 This method name is very abstract, give it more concrete name  or put all listeners initialization here
    private void initListeners() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMapsPresenter.actionNewMarker(latLng);
            }
        });
    }

    private MarkerInfoItem placeMarkerOnMapAndGet(MarkerInfo markerInfo) {
        MarkerInfoItem markerInfoItem = new MarkerInfoItem(markerInfo);
        mClusterManager.addItem(markerInfoItem);
        mClusterManager.cluster();
        return markerInfoItem;
    }

    private void showToastShort(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}