package com.vitaliyhtc.googlemaps1.data.realm;

public interface RealmStorageForRecyclerViewAdapterInterface {
    void onAttachedToRecyclerView();
    void onDetachedFromRecyclerView();
    long getItemId(final int index);
    int getItemCount();
}
