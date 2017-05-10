package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

public interface MarkersListPresenter extends BasePresenter {
    void subscribeForMarkersInfoData(RecyclerViewAdapter<MarkerInfo> adapter);
}
