package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.RealmResults;

public interface MarkersListPresenter extends BasePresenter {
    RealmResults<MarkerInfo> getRealmResultWithMarkerInfo();
}
