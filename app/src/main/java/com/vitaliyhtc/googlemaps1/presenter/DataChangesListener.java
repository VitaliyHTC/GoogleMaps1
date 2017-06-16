package com.vitaliyhtc.googlemaps1.presenter;

import java.util.List;

/**
 * Created by vitaliyhtc on 16.06.17.
 */

public interface DataChangesListener<T> {
    void setData(List<T> data);
    void notifyDataSetChanged();
    void notifyItemRangeRemoved(int positionStart, int itemCount);
    void notifyItemRangeInserted(int positionStart, int itemCount);
    void notifyItemRangeChanged(int positionStart, int itemCount);
}
