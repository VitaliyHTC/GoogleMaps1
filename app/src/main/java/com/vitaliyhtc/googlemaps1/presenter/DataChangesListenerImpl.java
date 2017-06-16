package com.vitaliyhtc.googlemaps1.presenter;

import com.vitaliyhtc.googlemaps1.adapter.RecyclerViewAdapter;

import java.util.List;

public class DataChangesListenerImpl<T> implements DataChangesListener<T> {

    private RecyclerViewAdapter<T> mAdapter;

    public DataChangesListenerImpl(RecyclerViewAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @Override
    public void setData(List<T> data) {
        mAdapter.setData(data);
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }
}
