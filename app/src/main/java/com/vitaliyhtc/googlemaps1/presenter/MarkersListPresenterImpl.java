package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.data.DataStorageUtils;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersDeletedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoStorage;
import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.view.BaseView;
import com.vitaliyhtc.googlemaps1.view.MarkersListView;

public class MarkersListPresenterImpl implements MarkersListPresenter {

    private MarkersListView mView;

    private MarkersListStorage mMarkersListStorage;
    private MarkerInfoStorage mMarkerInfoStorage;

    public MarkersListPresenterImpl() {
    }

    @Override
    public void onAttachView(BaseView baseView) {
        mView = (MarkersListView) baseView;
        mMarkersListStorage = DataStorageUtils.getMarkersListStorageInstance();
        mMarkersListStorage.initResources();
        mMarkerInfoStorage = DataStorageUtils.getMarkerInfoStorageInstance();
        mMarkerInfoStorage.initResources();
    }

    @Override
    public void onDetachView() {
        mView = null;
        mMarkersListStorage.releaseResources();
        mMarkerInfoStorage.releaseResources();
    }

    // TODO: 31/05/17 adapter should not be connected to storage !!!
    @Override
    public void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter) {
        mMarkersListStorage.subscribeForMarkersInfoData(adapter);
    }

    @Override
    public void actionDeleteAllMarkers(MarkerInfoAllMarkersDeletedListener listener) {
        mMarkerInfoStorage.deleteAllMarkers(listener);
    }
}
