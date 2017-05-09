package com.vitaliyhtc.googlemaps1.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersResultListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRetrievedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.view.BaseView;
import com.vitaliyhtc.googlemaps1.view.MapsView;

import java.util.List;
import java.util.UUID;

public class MapsPresenterImpl
        implements MapsPresenter {

    private MapsView mMapsView;

    private MarkerInfoStorage mMarkerInfoStorage;


    public MapsPresenterImpl() {
    }

    @Override
    public void onAttachView(BaseView baseView) {
        mMapsView = (MapsView) baseView;
        mMarkerInfoStorage = new MarkerInfoRealmStorageImpl();
        mMarkerInfoStorage.initResources();
    }

    @Override
    public void onDetachView() {
        mMapsView = null;
        if (mMarkerInfoStorage != null) {
            mMarkerInfoStorage.releaseResources();
            mMarkerInfoStorage = null;
        }
    }

    @Override
    public void onMapTypeSwitchClick() {
        mMapsView.displayMapTypePickDialog();
    }

    @Override
    public void onMapTypeSelected(int mapType) {
        mMapsView.applyMapType(mapType);
    }


    @Override
    public void getAllMarkers() {
        // TODO: 09/05/17 what if this call to long an view is destroyed?
        mMarkerInfoStorage.getAllMarkersAsync(
                new MarkerInfoAllMarkersResultListener() {
                    @Override
                    public void onAllMarkersFind(List<MarkerInfo> markers) {
                        mMapsView.onAllMarkersRetrieved(markers);
                    }
                }
        );
    }

    @Override
    public void actionNewMarker(LatLng latLng) {
        mMapsView.displayNewMarkerDialog(latLng);
    }

    @Override
    public void onNewMarkerDialogSuccess(MarkerInfo markerInfo) {
        if (markerInfo.getId() == null) {
            markerInfo.setId(UUID.randomUUID().toString());
        }
        mMarkerInfoStorage.saveMarker(markerInfo);
        mMapsView.placeNewMarkerOnMap(markerInfo);
    }

    @Override
    public void actionMarkerOptions(Marker marker) {
        mMapsView.displayMarkerInfoAndOptionsDialog(marker);
    }

    @Override
    public void actionEditMarker(Marker marker) {
        mMarkerInfoStorage.getMarkerById((String) marker.getTag(),
                new MarkerInfoRetrievedListener() {
                    @Override
                    public void onMarkerRetrieved(MarkerInfo markerInfo) {
                        mMapsView.displayEditMarkerDialog(MarkerInfo.clone(markerInfo));
                    }
                }
        );
    }

    @Override
    public void onEditMarkerDialogSuccess(MarkerInfo markerInfo) {
        mMarkerInfoStorage.updateMarker(markerInfo);
        mMapsView.updateMarkerUi(markerInfo);
    }

    @Override
    public void actionDeleteMarker(Marker marker) {
        mMarkerInfoStorage.deleteMarkerById((String) marker.getTag());
        mMapsView.deleteMarkerUi(marker);
    }

}
