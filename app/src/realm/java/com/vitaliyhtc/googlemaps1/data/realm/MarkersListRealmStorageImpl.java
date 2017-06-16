package com.vitaliyhtc.googlemaps1.data.realm;

import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.presenter.DataChangesListener;

import io.realm.Realm;

public class MarkersListRealmStorageImpl implements MarkersListStorage {

    private Realm mRealmInstance;
    private RealmStorageDataChangesListener<MarkerInfo> storage;

    @Override
    public void initResources() {
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void releaseResources() {
        mRealmInstance.close();
        storage.onDetachedFromChangesListener();
    }

    @Override
    public void subscribeForMarkersInfoData(DataChangesListener<MarkerInfo> changesListener) {
        storage =
                new RealmStorageDataChangesListener<>(
                        mRealmInstance.where(MarkerInfo.class).findAll(),
                        true,
                        changesListener
                );
        storage.onAttachedToChangesListener();
    }
}
