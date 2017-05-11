package com.vitaliyhtc.googlemaps1.data.realm;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.Realm;

public class MarkersListRealmStorageImpl implements MarkersListStorage {

    private Realm mRealmInstance;
    private RealmStorageForRecyclerViewAdapter<MarkerInfo> storage;

    @Override
    public void initResources() {
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void releaseResources() {
        mRealmInstance.close();
        storage.onDetachedFromRecyclerView();
    }

    @Override
    public void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter) {
        storage =
                new RealmStorageForRecyclerViewAdapter<>(
                        mRealmInstance.where(MarkerInfo.class).findAll(),
                        true,
                        adapter
                );
        storage.onAttachedToRecyclerView();
    }
}
