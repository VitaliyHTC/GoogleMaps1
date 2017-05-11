package com.vitaliyhtc.googlemaps1.render;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.vitaliyhtc.googlemaps1.model.MarkerInfoItem;

public class IconRender extends DefaultClusterRenderer<MarkerInfoItem> {


    public IconRender(Context context, GoogleMap map,
                      ClusterManager<MarkerInfoItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerInfoItem item, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(item.getMarkerInfo().getIconHue()));
    }
}
