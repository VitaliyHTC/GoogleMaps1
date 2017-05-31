package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MarkersListStorage extends DataStorage {
    // TODO: 31/05/17 addapter is ui part and it should not be connected with storage.
    // you need to use listener to listen for DB update and send data to presenter -> view -> adapter
    void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter);
}
