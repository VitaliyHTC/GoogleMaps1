package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerWrap;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

public class MarkerInfoRealmStorageImpl implements MarkerInfoStorage {

    private RealmResults<MarkerInfo> mAllMarkersResult;

    private Realm mRealmInstance;

    public MarkerInfoRealmStorageImpl() {
    }

    @Override
    public void initResources() {
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void releaseResources() {
        mRealmInstance.close();
        if (mAllMarkersResult != null) {
            mAllMarkersResult.removeAllChangeListeners();
        }
    }

    @Override
    public void saveMarker(final MarkerInfo markerInfo) {
        mRealmInstance.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealm(markerInfo);
            }
        });
    }

    @Override
    public void updateMarker(final MarkerInfo markerInfo) {
        if (markerInfo != null) {
            mRealmInstance.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MarkerInfo markerInfo1 = realm
                            .where(MarkerInfo.class)
                            .equalTo(KEY_MARKER_ID, markerInfo.getId())
                            .findFirst();

                    markerInfo1.setLatitude(markerInfo.getLatitude());
                    markerInfo1.setLongitude(markerInfo.getLongitude());
                    markerInfo1.setTitle(markerInfo.getTitle());
                    markerInfo1.setIconHue(markerInfo.getIconHue());

                }
            });
        }
    }

    @Override
    public void getMarkerById(final String markerId, final MarkerInfoRetrievedListener listener) {
        final MarkerWrap markerWrap = new MarkerWrap();
        mRealmInstance.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MarkerInfo markerInfo = realm
                                .where(MarkerInfo.class)
                                .equalTo(KEY_MARKER_ID, markerId)
                                .findFirst();
                        markerWrap.setMarkerInfo(MarkerInfo.clone(markerInfo));
                    }
                },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        listener.onMarkerRetrieved(markerWrap.getMarkerInfo());
                    }
                }
        );
    }

    @Override
    public void deleteMarkerById(final String markerId) {
        mRealmInstance.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MarkerInfo markerInfo1 = realm
                                .where(MarkerInfo.class)
                                .equalTo(KEY_MARKER_ID, markerId)
                                .findFirst();
                        markerInfo1.deleteFromRealm();
                    }
                }
        );
    }

    @Override
    public void getAllMarkersAsync(final MarkerInfoAllMarkersResultListener listener) {
        OrderedRealmCollectionChangeListener<RealmResults<MarkerInfo>> allMarkersCallback = new OrderedRealmCollectionChangeListener<RealmResults<MarkerInfo>>() {
            @Override
            public void onChange(RealmResults<MarkerInfo> results, OrderedCollectionChangeSet changeSet) {
                if (changeSet == null) {
                    // The first time async returns with an null changeSet.
                    listener.onAllMarkersFind(results);
                } else {
                    // Called on every update.
                }
            }
        };
        mAllMarkersResult = mRealmInstance.where(MarkerInfo.class).findAllAsync();
        mAllMarkersResult.addChangeListener(allMarkersCallback);
    }
}
