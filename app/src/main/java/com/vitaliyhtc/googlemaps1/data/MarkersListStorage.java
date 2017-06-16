package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.presenter.DataChangesListener;

public interface MarkersListStorage extends DataStorage {
    void subscribeForMarkersInfoData(DataChangesListener<MarkerInfo> adapter);
}
