package com.vitaliyhtc.googlemaps1.data.realm;

public interface DataStorageForRecyclerViewAdapterInterface {
    void onAttachedToRecyclerView();
    void onDetachedFromRecyclerView();
    long getItemId(final int index);
    int getItemCount();
}
