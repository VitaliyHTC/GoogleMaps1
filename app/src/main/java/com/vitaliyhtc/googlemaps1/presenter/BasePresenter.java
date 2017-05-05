package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.view.BaseView;

public interface BasePresenter {
    void onAttachView(BaseView baseView);
    void onDetachView();
}
