package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MarkersListStorage extends DataStorage {
    void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter);
}
