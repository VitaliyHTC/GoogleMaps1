package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerWrap;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

public class MarkerInfoRealmStorageImpl implements MarkerInfoRealmStorage {

    private RealmResults<MarkerInfo> mAllMarkersResult;

    public MarkerInfoRealmStorageImpl() {
    }

    @Override
    public void onStop() {
        if (mAllMarkersResult != null) {
            mAllMarkersResult.removeAllChangeListeners();
        }
    }

    public void saveMarker(Realm realmInstance, final MarkerInfo markerInfo) {
        realmInstance.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealm(markerInfo);
            }
        });
    }

    public void updateMarker(Realm realmInstance, final MarkerInfo markerInfo) {
        if (markerInfo != null) {
            realmInstance.executeTransactionAsync(new Realm.Transaction() {
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

    public void getMarkerById(Realm realmInstance, final String markerId, final MarkerRetrievedListener listener) {
        final MarkerWrap markerWrap = new MarkerWrap();
        realmInstance.executeTransactionAsync(
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

    public void deleteMarkerById(Realm realm, final String markerId) {
        realm.executeTransactionAsync(
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

    public void getAllMarkersAsync(Realm realm, final AllMarkersResultListener listener) {
        OrderedRealmCollectionChangeListener<RealmResults<MarkerInfo>> allMarkersCallback = new OrderedRealmCollectionChangeListener<RealmResults<MarkerInfo>>() {
            @Override
            public void onChange(RealmResults<MarkerInfo> results, OrderedCollectionChangeSet changeSet) {
                listener.onAllMarkersFinded(results);
            }
        };
        mAllMarkersResult = realm.where(MarkerInfo.class).findAllAsync();
        mAllMarkersResult.addChangeListener(allMarkersCallback);
    }

    public interface MarkerRetrievedListener {
        void onMarkerRetrieved(MarkerInfo markerInfo);
    }

    public interface AllMarkersResultListener {
        void onAllMarkersFinded(List<MarkerInfo> markers);
    }
}
