package com.vitaliyhtc.googlemaps1.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface HolderClickObserver {
    void onHolderClick(RecyclerView.ViewHolder holder);
    boolean onHolderLongClick(RecyclerView.ViewHolder holder);
}
