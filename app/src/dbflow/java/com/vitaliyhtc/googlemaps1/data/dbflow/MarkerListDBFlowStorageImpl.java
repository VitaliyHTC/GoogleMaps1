package com.vitaliyhtc.googlemaps1.data.dbflow;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.vitaliyhtc.googlemaps1.data.MarkersListStorage;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.presenter.DataChangesListener;
import com.vitaliyhtc.googlemaps1.util.MainThreadUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarkerListDBFlowStorageImpl implements MarkersListStorage {

    private DataChangesListener<MarkerInfo> mChangesListener;
    private Map<String, MarkerInfo> mMarkers;
    private DirectModelNotifier.ModelChangedListener<MarkerInfo> mModelChangedListener;

    @Override
    public void initResources() {
        mMarkers = new LinkedHashMap<>();
    }

    @Override
    public void releaseResources() {
        DirectModelNotifier.get().unregisterForModelChanges(MarkerInfo.class, mModelChangedListener);
    }

    @Override
    public void subscribeForMarkersInfoData(DataChangesListener<MarkerInfo> listener) {
        listener.setData(new ArrayList<MarkerInfo>());
        mChangesListener = listener;
        SQLite.select()
                .from(MarkerInfo.class)
                .async()
                .queryResultCallback(new QueryTransaction.QueryResultCallback<MarkerInfo>() {
                    @Override
                    public void onQueryResult(QueryTransaction<MarkerInfo> transaction, @NonNull CursorResult<MarkerInfo> tResult) {
                        mMarkers.clear();
                        for (MarkerInfo markerInfo : tResult.toListClose()) {
                            mMarkers.put(markerInfo.getId(), markerInfo);
                        }
                        onInitialDataReceived();
                    }
                }).execute();
    }

    private void onInitialDataReceived() {
        pushNewDataToAdapter(mMarkers);
        registerForFutureChanges();
    }

    private void pushNewDataToAdapter(final Map<String, MarkerInfo> markersMap) {
        MainThreadUtils.post(new Runnable() {
            @Override
            public void run() {
                List<MarkerInfo> markers = new ArrayList<>();
                markers.addAll(markersMap.values());
                mChangesListener.setData(markers);
                mChangesListener.notifyDataSetChanged();
            }
        });
    }

    private void registerForFutureChanges() {
        mModelChangedListener = new DirectModelNotifier.ModelChangedListener<MarkerInfo>() {
            @Override
            public void onModelChanged(MarkerInfo model, BaseModel.Action action) {
                // react to model changes
                //Log.e("_DBFlow", "onModelChanged: action: " + action.name() + "; model: " + model.getTitle());
                if (action == BaseModel.Action.CHANGE ||
                        action == BaseModel.Action.INSERT ||
                        action == BaseModel.Action.SAVE ||
                        action == BaseModel.Action.UPDATE) {
                    mMarkers.put(model.getId(), model);
                }
                if (action == BaseModel.Action.DELETE) {
                    mMarkers.remove(model.getId());
                }
                pushNewDataToAdapter(mMarkers);
            }

            @Override
            public void onTableChanged(BaseModel.Action action) {
                // react to table changes.
                // nothing...
            }
        };
        DirectModelNotifier.get().registerForModelChanges(MarkerInfo.class, mModelChangedListener);
    }

}
