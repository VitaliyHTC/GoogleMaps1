package com.vitaliyhtc.googlemaps1.presenter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorage;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.dialog.MapTypeDialog;
import com.vitaliyhtc.googlemaps1.dialog.MarkerDialog;
import com.vitaliyhtc.googlemaps1.dialog.MarkerInfoOptionsDialog;
import com.vitaliyhtc.googlemaps1.model.FragmentWrap;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.util.MapStateUtils;
import com.vitaliyhtc.googlemaps1.view.BaseView;
import com.vitaliyhtc.googlemaps1.view.MapsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;

public class MapsPresenterImpl
        implements MapsPresenter, OnMapReadyCallback {

    private FragmentWrap mFragment;
    private MapsView mMapsView;

    private GoogleMap mMap;

    private Map<String, Marker> mMarkers;

    private MarkerInfoRealmStorage mMarkerInfoRealmStorage;

    private Realm mRealm;


    public MapsPresenterImpl(FragmentWrap fragment) {
        mFragment = fragment;
    }

    @Override
    public void onAttachView(BaseView baseView) {
        mMapsView = (MapsView) baseView;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetachView() {
        mMapsView = null;
        if (mMarkerInfoRealmStorage != null) {
            mMarkerInfoRealmStorage.onStop();
        }
        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Override
    public void onCreate() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) mFragment.getFragment().getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsPresenterImpl.this);
    }

    @Override
    public void onStop() {
        MapStateUtils.saveCameraPositionFromMap(mFragment.getFragment().getContext(), mMap);
        MapStateUtils.saveMapType(mFragment.getFragment().getContext(), mMap);
    }

    @Override
    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other work here
        mMarkers = new HashMap<>();
        mMarkerInfoRealmStorage = new MarkerInfoRealmStorageImpl();
        initUiSettings();
        MapStateUtils.restoreCameraPositionOnMap(mFragment.getFragment().getContext(), mMap);
        MapStateUtils.restoreMapType(mFragment.getFragment().getContext(), mMap);
        mMapsView.enableMyLocation();
        initListeners();
        restoreMarkersOnMapAndListenForChanges();
    }

    @Override
    public void onMapTypeSwitchClick() {
        MapTypeDialog mapTypeDialog = new MapTypeDialog();
        mapTypeDialog.setSelectedMapType(mMap.getMapType());
        mapTypeDialog.setMapTypeSelectedListener(new MapTypeDialog.MapTypeSelectedListener() {
            @Override
            public void onMapTypeSelected(int mapType) {
                mMap.setMapType(mapType);
            }
        });
        mapTypeDialog.show(mFragment.getFragment().getChildFragmentManager(), "MapTypeDialog");
    }

    private void initUiSettings() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMapsView.showToast(mFragment.getFragment().getContext().getString(R.string.map_ui_my_location_button_click_toast_message));
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
                return false;
            }
        });
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
                // realm fire callback with new set of MarkerInfo objects
            }
        });
        markerDialog.show(mFragment.getFragment().getChildFragmentManager(), "MarkerDialog");
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
        dialog.show(mFragment.getFragment().getChildFragmentManager(), "MarkerInfoOptionsDialog");
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
                // realm fire callback with new set of MarkerInfo objects
            }
        });
        markerDialog.show(mFragment.getFragment().getChildFragmentManager(), "MarkerDialog");
    }

    private void actionDeleteMarker(Marker marker) {
        mMarkerInfoRealmStorage.deleteMarkerById(mRealm, (String) marker.getTag());
        // realm fire callback with new set of MarkerInfo objects
    }

    // listen for new, edited or deleted markers.
    private void restoreMarkersOnMapAndListenForChanges() {
        mMarkerInfoRealmStorage.getAllMarkersAsync(mRealm,
                new MarkerInfoRealmStorageImpl.AllMarkersResultListener() {
                    @Override
                    public void onAllMarkersFinded(List<MarkerInfo> markers) {
                        // verify for new or edited markers
                        for (MarkerInfo markerInfo : markers) {
                            if (mMarkers.containsKey(markerInfo.getId())) {
                                mMarkers.get(markerInfo.getId()).remove();
                            }
                            placeMarkerOnMap(markerInfo);
                        }
                        // verify for removed markers
                        boolean isPresent;
                        List<String> idsForRemoval = new ArrayList<String>();
                        for (String id : mMarkers.keySet()) {
                            isPresent = false;
                            for (MarkerInfo markerInfo : markers) {
                                if (id.equals(markerInfo.getId())) {
                                    isPresent = true;
                                }
                            }
                            if (!isPresent) {
                                idsForRemoval.add(id);
                            }
                        }
                        for (String id : idsForRemoval) {
                            mMarkers.get(id).remove();
                            mMarkers.remove(id);
                        }
                    }
                }
        );
    }

}
