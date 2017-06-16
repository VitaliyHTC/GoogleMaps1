package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.data.MarkerInfoAllMarkersDeletedListener;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MarkersListPresenter extends BasePresenter {
    void subscribeForMarkersInfoData(DataChangesListener<MarkerInfo> listener);
    void actionDeleteAllMarkers(MarkerInfoAllMarkersDeletedListener listener);
}
