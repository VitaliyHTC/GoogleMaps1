package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.Marker;
import com.vitaliyhtc.googlemaps1.model.MarkerWrap;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

// write operation need to be moved to realm.executeTransactionAsync() ???
public abstract class MarkerRealmStorage {

    private MarkerRealmStorage() {
        throw new AssertionError();
    }

    public static void saveMarker(final Marker marker) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                realm.copyToRealm(marker);
            }
        });
        realm.close();
    }

    public static void updateMarker(final Marker marker) {
        if (marker != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Marker marker1 = realm
                            .where(Marker.class)
                            .equalTo(KEY_MARKER_ID, marker.getId())
                            .findFirst();

                    marker1.setLatitude(marker.getLatitude());
                    marker1.setLongitude(marker.getLongitude());
                    marker1.setTitle(marker.getTitle());
                    marker1.setIconHue(marker.getIconHue());

                }
            });
            realm.close();
        }
    }

    /**
     *
     * @param realmInstance    must be closed after after marker usage to release resources.
     * @param markerId         id to retrieve Marker instance from Realm db.
     * @return  instance of Marker corresponding to given id.
     */
    public static Marker getMarkerById(Realm realmInstance, final String markerId) {
        final MarkerWrap markerWrap = new MarkerWrap();
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                markerWrap.setMarker(realm
                        .where(com.vitaliyhtc.googlemaps1.model.Marker.class)
                        .equalTo(KEY_MARKER_ID, markerId)
                        .findFirst()
                );
            }
        });
        return markerWrap.getMarker();
    }

    public static void deleteMarkerById(final String markerId) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Marker marker1 = realm
                        .where(Marker.class)
                        .equalTo(KEY_MARKER_ID, markerId)
                        .findFirst();
                marker1.deleteFromRealm();
            }
        });
        realm.close();
    }

    /**
     *
     * @param realmInstance    must be closed after after markers usage to release resources.
     * @return  All Markers that stored in Realm db
     */
    public static List<Marker> getAllMarkers(Realm realmInstance) {
        final List<Marker> markers1 = new ArrayList<>();
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                markers1.addAll(realm.where(com.vitaliyhtc.googlemaps1.model.Marker.class).findAll());
            }
        });
        return markers1;
    }
}
