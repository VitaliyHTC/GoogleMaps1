package com.vitaliyhtc.googlemaps1.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class MarkerInfoRecyclerViewAdapter extends RealmRecyclerViewAdapter<MarkerInfo, MarkerInfoRecyclerViewAdapter.ViewHolder> {

    private OnMarkerIconClickListener mOnMarkerIconClickListener;

    public MarkerInfoRecyclerViewAdapter(
            @Nullable RealmResults<MarkerInfo> data,
            boolean autoUpdate,
            OnMarkerIconClickListener markerIconClickListener
    ) {
        super(data, autoUpdate);
        setHasStableIds(true);
        mOnMarkerIconClickListener = markerIconClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(mOnMarkerIconClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
        //holder.setOnClickListener(mOnMarkerIconClickListener);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_marker)
        ImageView mImageView;
        @BindView(R.id.tv_MarkerTitle)
        TextView mTextView;

        private OnMarkerIconClickListener mOnMarkerIconClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 06/05/17 highlight selected item
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION && mOnMarkerIconClickListener != null) {
                        mOnMarkerIconClickListener.onItemClick(adapterPosition);
                    }
                }
            });
        }

        void setOnClickListener(OnMarkerIconClickListener onMarkerIconClickListener) {
            mOnMarkerIconClickListener = onMarkerIconClickListener;
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
