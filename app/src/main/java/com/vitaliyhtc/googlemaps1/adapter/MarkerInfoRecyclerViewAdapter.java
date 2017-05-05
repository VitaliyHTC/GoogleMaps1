package com.vitaliyhtc.googlemaps1.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class MarkerInfoRecyclerViewAdapter extends RealmRecyclerViewAdapter<MarkerInfo, MarkerInfoRecyclerViewAdapter.MyViewHolder> {

    private ClickListener mClickListener;

    public MarkerInfoRecyclerViewAdapter(@Nullable RealmResults<MarkerInfo> data, boolean autoUpdate) {
        super(data, autoUpdate);
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_marker_info, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        viewHolder.setOnClickListener(mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setOnClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public static int getImageResIdFromHue(float hue) {
        if (hue == BitmapDescriptorFactory.HUE_RED) {
            return R.drawable.ic_place_red_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_ORANGE) {
            return R.drawable.ic_place_orange_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_YELLOW) {
            return R.drawable.ic_place_yellow_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_GREEN) {
            return R.drawable.ic_place_green_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_CYAN) {
            return R.drawable.ic_place_cyan_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_AZURE) {
            return R.drawable.ic_place_azure_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_BLUE) {
            return R.drawable.ic_place_blue_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_VIOLET) {
            return R.drawable.ic_place_violet_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_MAGENTA) {
            return R.drawable.ic_place_magenta_48dp;
        } else if (hue == BitmapDescriptorFactory.HUE_ROSE) {
            return R.drawable.ic_place_rose_48dp;
        } else {
            return R.drawable.ic_place_red_48dp;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        private ClickListener mClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_marker);
            mTextView = (TextView) itemView.findViewById(R.id.tv_MarkerTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION && mClickListener != null) {
                        mClickListener.onItemClick(adapterPosition);
                    }
                }
            });
        }

        void setOnClickListener(ClickListener clickListener) {
            mClickListener = clickListener;
        }

        void bind(MarkerInfo markerInfo) {
            mImageView.setImageResource(getImageResIdFromHue(markerInfo.getIconHue()));
            mTextView.setText(markerInfo.getTitle());
        }
    }

    public interface ClickListener {
        void onItemClick(int position);
    }
}
