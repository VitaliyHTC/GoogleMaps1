package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.data.MarkersListRealmStorageImpl;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.view.BaseView;
import com.vitaliyhtc.googlemaps1.view.MarkersListView;

public class MarkersListPresenterImpl implements MarkersListPresenter {

    private MarkersListView mView;

    private MarkersListStorage mMarkersListStorage;

    public MarkersListPresenterImpl() {
    }

    @Override
    public void onAttachView(BaseView baseView) {
        mView = (MarkersListView) baseView;
        mMarkersListStorage = new MarkersListRealmStorageImpl();
        mMarkersListStorage.initResources();
    }

    @Override
    public void onDetachView() {
        mView = null;
        mMarkersListStorage.releaseResources();
    }

    @Override
    public void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter) {
        mMarkersListStorage.subscribeForMarkersInfoData(adapter);
    }
}
