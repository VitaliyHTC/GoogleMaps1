package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerWrap;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

// write operation need to be moved to realm.executeTransactionAsync() ???
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

    public void saveMarker(final MarkerInfo markerInfo) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                realm.copyToRealm(markerInfo);
            }
        });
        realm.close();
    }

    public void updateMarker(final MarkerInfo markerInfo) {
        if (markerInfo != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
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
            realm.close();
        }
    }

    /**
     * @param realmInstance must be closed after after marker usage to release resources.
     * @param markerId      id to retrieve MarkerInfo instance from Realm db.
     * @return instance of MarkerInfo corresponding to given id.
     */
    public MarkerInfo getMarkerById(Realm realmInstance, final String markerId) {
        final MarkerWrap markerWrap = new MarkerWrap();
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                markerWrap.setMarkerInfo(realm
                        .where(MarkerInfo.class)
                        .equalTo(KEY_MARKER_ID, markerId)
                        .findFirst()
                );
            }
        });
        return markerWrap.getMarkerInfo();
    }

    public void deleteMarkerById(Realm realm, final String markerId) {
        realm.executeTransaction(
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

    public interface AllMarkersResultListener {
        void onAllMarkersFinded(List<MarkerInfo> markers);
    }
}
