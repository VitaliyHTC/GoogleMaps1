package com.vitaliyhtc.googlemaps1.data.realm;

public interface DataStorageChangesListener {
    void onAttachedToChangesListener();
    void onDetachedFromChangesListener();
    long getItemId(final int index);
    int getItemCount();
}
