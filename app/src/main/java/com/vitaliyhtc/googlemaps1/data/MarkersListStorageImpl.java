package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.Realm;
import io.realm.RealmResults;

public class MarkersListStorageImpl implements MarkersListStorage {

    private Realm mRealmInstance;

    @Override
    public void initResources() {
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void releaseResources() {
        mRealmInstance.close();
    }

    @Override
    public RealmResults<MarkerInfo> getRealmResultWithMarkerInfo() {
        return mRealmInstance.where(MarkerInfo.class).findAll();
    }
}
