package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.data.MarkersListStorageImpl;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.view.BaseView;
import com.vitaliyhtc.googlemaps1.view.MarkersListView;

import io.realm.RealmResults;

public class MarkersListPresenterImpl implements MarkersListPresenter {

    private MarkersListView mView;

    private MarkersListStorage mMarkersListStorage;

    public MarkersListPresenterImpl() {
    }

    @Override
    public void onAttachView(BaseView baseView) {
        mView = (MarkersListView) baseView;
        mMarkersListStorage = new MarkersListStorageImpl();
        mMarkersListStorage.initResources();
    }

    @Override
    public void onDetachView() {
        mView = null;
        mMarkersListStorage.releaseResources();
    }

    @Override
    public RealmResults<MarkerInfo> getRealmResultWithMarkerInfo(){
        return mMarkersListStorage.getRealmResultWithMarkerInfo();
    }
}
