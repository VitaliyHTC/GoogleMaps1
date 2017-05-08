package com.vitaliyhtc.googlemaps1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.MarkerColorItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerItemsAdapter extends RecyclerView.Adapter<MarkerItemsAdapter.ViewHolder> {

    private MarkerItemClickListener mMarkerItemClickListener;
    private List<MarkerColorItem> mMarkerColorItems;

    public MarkerItemsAdapter(MarkerItemClickListener markerItemClickListener) {
        mMarkerItemClickListener = markerItemClickListener;
    }

    public void setMarkerColorItems(List<MarkerColorItem> markerColorItems) {
        mMarkerColorItems = markerColorItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setOnClickListener(mMarkerItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mMarkerColorItems.get(position));
        //holder.setOnClickListener(mMarkerItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mMarkerColorItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_marker)
        ImageView mImageView;
        @BindView(R.id.tv_MarkerColorItemTitle)
        TextView mTextView;

        private MarkerItemClickListener mMarkerItemClickListener;

        ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 06/05/17 highlight selected item
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mMarkerItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        void setOnClickListener(MarkerItemClickListener markerItemClickListener) {
            mMarkerItemClickListener = markerItemClickListener;
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
