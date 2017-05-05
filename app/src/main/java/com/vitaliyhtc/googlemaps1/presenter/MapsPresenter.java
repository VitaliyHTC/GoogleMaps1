package com.vitaliyhtc.googlemaps1.presenter;

import com.google.android.gms.maps.GoogleMap;

public interface MapsPresenter extends BasePresenter {
    void onMapTypeSwitchClick();
    GoogleMap getMap();
}
