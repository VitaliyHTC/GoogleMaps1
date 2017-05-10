package com.vitaliyhtc.googlemaps1.adapter;

import java.util.List;

/**
 * Used for {@code RealmStorageForRecyclerViewAdapter}
 *
 * @param <T> type of Model stored in the adapter
 */
public interface RecyclerViewAdapter<T> {
    void setData(List<T> data);
    void notifyDataSetChanged();
    void notifyItemRangeRemoved(int positionStart, int itemCount);
    void notifyItemRangeInserted(int positionStart, int itemCount);
    void notifyItemRangeChanged(int positionStart, int itemCount);
}
