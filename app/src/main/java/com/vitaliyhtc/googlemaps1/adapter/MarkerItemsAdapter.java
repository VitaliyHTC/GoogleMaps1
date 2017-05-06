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

// TODO: 06/05/17 check todo's for MarkerInfoRecyclerViewAdapter
public class MarkerItemsAdapter extends RecyclerView.Adapter<MarkerItemsAdapter.ViewHolder> {

    private ClickListener mClickListener;
    private List<MarkerColorItem> mMarkerColorItems;

    public MarkerItemsAdapter(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setMarkerColorItems(List<MarkerColorItem> markerColorItems) {
        mMarkerColorItems = markerColorItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setOnClickListener(mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mMarkerColorItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mMarkerColorItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        private ClickListener mClickListener;

        ViewHolder(View v){
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.iv_marker);
            mTextView = (TextView) v.findViewById(R.id.tv_MarkerColorItemTitle);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        void setOnClickListener(ClickListener clickListener) {
            mClickListener = clickListener;
        }

        void bind(MarkerColorItem item) {
            mImageView.setImageResource(item.getImgResId());
            mTextView.setText(item.getTitle());
        }
    }

    public interface ClickListener {
        void onItemClick(int position);
    }
}
