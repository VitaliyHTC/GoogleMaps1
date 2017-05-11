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
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;
import com.vitaliyhtc.googlemaps1.adapter.recyclerview.HolderClickObserver;
import com.vitaliyhtc.googlemaps1.adapter.recyclerview.SelectionHelper;
import com.vitaliyhtc.googlemaps1.adapter.recyclerview.SelectionObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerInfoRecyclerViewAdapterImpl
        extends RecyclerView.Adapter<MarkerInfoRecyclerViewAdapterImpl.ItemViewHolder>
        implements RecyclerViewAdapter<MarkerInfo>,
        MarkerInfoRecyclerViewAdapter,
        SelectionObserver,
        HolderClickObserver {

    private OnMarkerIconClickListener mOnMarkerIconClickListener;
    private List<MarkerInfo> mMarkers;

    private SelectionHelper mSelectionHelper;

    private int mSelectedPosition = RecyclerView.NO_POSITION;

    public MarkerInfoRecyclerViewAdapterImpl(OnMarkerIconClickListener markerIconClickListener) {
        mOnMarkerIconClickListener = markerIconClickListener;

        mSelectionHelper = new SelectionHelper();
        mSelectionHelper.registerSelectionObserver(this);
        mSelectionHelper.registerHolderClickObserver(this);
    }

    @Override
    public void setData(List<MarkerInfo> data) {
        mMarkers = data;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected) {
        int color = Config.RV_SELECTED_OFF_BACKGROUND_COLOR;
        if (isSelected) {
            color = Config.RV_SELECTED_ON_BACKGROUND_COLOR;
        }
        ((ItemViewHolder) holder).setBackgroundColor(color);
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition != RecyclerView.NO_POSITION && mOnMarkerIconClickListener != null) {
            mSelectedPosition = adapterPosition;
            mOnMarkerIconClickListener.onItemClick(adapterPosition);
        }
    }

    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder holder) {
        // return event isConsumed flag.
        return false;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker_info, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(itemView);
        return mSelectionHelper.wrapSingleSelectable(itemViewHolder);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(mMarkers.get(position));
        int color = Config.RV_SELECTED_OFF_BACKGROUND_COLOR;
        if (position != RecyclerView.NO_POSITION && position == mSelectedPosition) {
            color = Config.RV_SELECTED_ON_BACKGROUND_COLOR;
        }
        holder.setBackgroundColor(color);
        mSelectionHelper.bindHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mMarkers.size();
    }

    @Override
    public MarkerInfo getItem(int position) {
        return mMarkers.get(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_wrap)
        RelativeLayout mRelativeLayout;
        @BindView(R.id.iv_marker)
        ImageView mImageView;
        @BindView(R.id.tv_MarkerTitle)
        TextView mTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setBackgroundColor(int color) {
            mRelativeLayout.setBackgroundColor(color);
        }

        void bind(MarkerInfo markerInfo) {
            mImageView.setImageResource(MarkerInfo.getImageResIdFromHue(markerInfo.getIconHue()));
            mTextView.setText(markerInfo.getTitle());
        }
    }

    public interface OnMarkerIconClickListener {
        void onItemClick(int position);
    }
}
