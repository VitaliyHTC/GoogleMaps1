package com.vitaliyhtc.googlemaps1.data;

import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.List;

public interface MarkerInfoAllMarkersResultListener {
    void onAllMarkersFind(List<MarkerInfo> markers);
}
