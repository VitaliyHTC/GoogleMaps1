package com.vitaliyhtc.googlemaps1.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface SelectionObserver {
    void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected);
    void onSelectableChanged(boolean isSelectable);
}
