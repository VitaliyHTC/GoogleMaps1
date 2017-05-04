package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerWrap;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

// write operation need to be moved to realm.executeTransactionAsync() ???
public class MarkerInfoRealmStorageImpl implements MarkerInfoRealmStorage {

    public MarkerInfoRealmStorageImpl() {
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

    public void deleteMarkerById(final String markerId) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MarkerInfo markerInfo1 = realm
                        .where(MarkerInfo.class)
                        .equalTo(KEY_MARKER_ID, markerId)
                        .findFirst();
                markerInfo1.deleteFromRealm();
            }
        });
        realm.close();
    }

    /**
     * @param realmInstance must be closed after after markers usage to release resources.
     * @return All Markers that stored in Realm db
     */
    public List<MarkerInfo> getAllMarkers(Realm realmInstance) {
        final List<MarkerInfo> markers1 = new ArrayList<>();
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                markers1.addAll(realm.where(MarkerInfo.class).findAll());
            }
        });
        return markers1;
    }
}
