package com.vitaliyhtc.googlemaps1.data.dbflow;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersDeletedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersResultListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoRetrievedListener;
import com.vitaliyhtc.googlemaps1.data.MarkerInfoStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo_Table;

import java.util.List;

public class MarkerInfoDBFlowStorageImpl implements MarkerInfoStorage {

    @Override
    public void initResources() {

    }

    @Override
    public void releaseResources() {

    }

    @Override
    public void saveMarker(final MarkerInfo markerInfo) {
        FastStoreModelTransaction
                .saveBuilder(FlowManager.getModelAdapter(MarkerInfo.class))
                .add(markerInfo)
                .build()
                .execute(FlowManager.getWritableDatabase(DBFlowDatabase.class));
    }

    @Override
    public void saveMarkersListSynchronously(List<MarkerInfo> markers) {
        FastStoreModelTransaction
                .saveBuilder(FlowManager.getModelAdapter(MarkerInfo.class))
                .addAll(markers)
                .build()
                .execute(FlowManager.getWritableDatabase(DBFlowDatabase.class));
    }

    @Override
    public void updateMarker(final MarkerInfo markerInfo) {
        saveMarker(markerInfo);
    }

    @Override
    public void getMarkerById(String markerId, final MarkerInfoRetrievedListener listener) {
        SQLite.select()
                .from(MarkerInfo.class)
                .where(MarkerInfo_Table.id.is(markerId))
                .async()
                .queryResultCallback(new QueryTransaction.QueryResultCallback<MarkerInfo>() {
                    @Override
                    public void onQueryResult(QueryTransaction<MarkerInfo> transaction, @NonNull CursorResult<MarkerInfo> tResult) {
                        listener.onMarkerRetrieved(tResult.toModelClose());
                    }
                }).execute();
    }

    @Override
    public void deleteMarkerById(String markerId) {
        getMarkerById(markerId,
                new MarkerInfoRetrievedListener() {
                    @Override
                    public void onMarkerRetrieved(final MarkerInfo markerInfo) {
                        FlowManager.getDatabase(DBFlowDatabase.class).executeTransaction(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                markerInfo.delete();
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void getAllMarkersAsync(final MarkerInfoAllMarkersResultListener listener) {
        SQLite.select()
                .from(MarkerInfo.class)
                .async()
                .queryResultCallback(new QueryTransaction.QueryResultCallback<MarkerInfo>() {
                    @Override
                    public void onQueryResult(QueryTransaction<MarkerInfo> transaction, @NonNull CursorResult<MarkerInfo> tResult) {
                        listener.onAllMarkersFind(tResult.toListClose());
                    }
                }).execute();
    }

    @Override
    public void deleteAllMarkers(final MarkerInfoAllMarkersDeletedListener listener) {
        getAllMarkersAsync(new MarkerInfoAllMarkersResultListener() {
            @Override
            public void onAllMarkersFind(final List<MarkerInfo> markers) {
                FlowManager.getDatabase(DBFlowDatabase.class).executeTransaction(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for (MarkerInfo markerInfo : markers) {
                            markerInfo.delete();
                        }
                        listener.onAllMarkersDeleted();
                    }
                });
            }
        });
    }
}
