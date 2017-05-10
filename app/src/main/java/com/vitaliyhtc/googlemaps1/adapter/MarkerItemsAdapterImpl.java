package com.vitaliyhtc.googlemaps1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitaliyhtc.googlemaps1.Config;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.MarkerColorItem;
import com.vitaliyhtc.googlemaps1.recyclerview.HolderClickObserver;
import com.vitaliyhtc.googlemaps1.recyclerview.SelectionHelper;
import com.vitaliyhtc.googlemaps1.recyclerview.SelectionObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerItemsAdapterImpl
        extends RecyclerView.Adapter<MarkerItemsAdapterImpl.ViewHolder>
        implements MarkerItemsAdapter, SelectionObserver, HolderClickObserver {

    private MarkerItemClickListener mMarkerItemClickListener;
    private List<MarkerColorItem> mMarkerColorItems;

    private SelectionHelper mSelectionHelper;

    private float mInitialSelectedMarkerHue;
    private boolean isInitialMarkerSet;


    public MarkerItemsAdapterImpl(MarkerItemClickListener markerItemClickListener, float hue) {
        mMarkerItemClickListener = markerItemClickListener;
        mInitialSelectedMarkerHue = hue;

        mSelectionHelper = new SelectionHelper();
        mSelectionHelper.registerSelectionObserver(this);
        mSelectionHelper.registerHolderClickObserver(this);
    }

    @Override
    public void setMarkerColorItems(List<MarkerColorItem> markerColorItems) {
        mMarkerColorItems = markerColorItems;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected) {
        int color = Config.RV_SELECTED_OFF_BACKGROUND_COLOR;
        if (isSelected) {
            color = Config.RV_SELECTED_ON_BACKGROUND_COLOR;
        }
        ((ViewHolder) holder).setBackgroundColor(color);
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition != RecyclerView.NO_POSITION && mMarkerItemClickListener != null) {
            mMarkerItemClickListener.onItemClick(adapterPosition);
        }
    }

    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder holder) {
        // return event isConsumed flag.
        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return mSelectionHelper.wrapSingleSelectable(viewHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarkerColorItem markerColorItem = mMarkerColorItems.get(position);
        holder.bind(markerColorItem);
        mSelectionHelper.bindHolder(holder, position);
        if (!isInitialMarkerSet) {
            if (markerColorItem.getMarkerHue() == mInitialSelectedMarkerHue) {
                mSelectionHelper.clearSelection();
                mSelectionHelper.setItemSelected(holder, true);
                isInitialMarkerSet = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMarkerColorItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_wrap)
        RelativeLayout mRelativeLayout;
        @BindView(R.id.iv_marker)
        ImageView mImageView;
        @BindView(R.id.tv_MarkerColorItemTitle)
        TextView mTextView;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void setBackgroundColor(int color) {
            mRelativeLayout.setBackgroundColor(color);
        }

        void bind(MarkerColorItem item) {
            mImageView.setImageResource(item.getImgResId());
            mTextView.setText(item.getTitle());
        }
    }

    public interface MarkerItemClickListener {
        void onItemClick(int position);
    }
}
