package com.vitaliyhtc.googlemaps1.model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.data.dbflow.DBFlowDatabase;


@Table(database = DBFlowDatabase.class)
public class MarkerInfo extends BaseModel {

    public static final float[] hues = {
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_ROSE
    };
    public static final int huesSize = hues.length;

    @Column
    @PrimaryKey
    private String id;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private String title;
    @Column
    private float iconHue;


    public String getId() {
        return id;
    }

    public static MarkerInfo clone(MarkerInfo markerInfo){
        MarkerInfo markerInfo1 = new MarkerInfo();

        markerInfo1.setId(markerInfo.getId());
        markerInfo1.setTitle(markerInfo.getTitle());
        markerInfo1.setIconHue(markerInfo.getIconHue());
        markerInfo1.setLatitude(markerInfo.getLatitude());
        markerInfo1.setLongitude(markerInfo.getLongitude());

        return markerInfo1;
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

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getIconHue() {
        return iconHue;
    }

    public void setIconHue(float iconHue) {
        this.iconHue = iconHue;
    }
}
