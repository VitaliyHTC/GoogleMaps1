package com.vitaliyhtc.googlemaps1.data;

public interface RealmStorageForRecyclerViewAdapterInterface {
    void onAttachedToRecyclerView();
    void onDetachedFromRecyclerView();
    long getItemId(final int index);
    int getItemCount();
}
