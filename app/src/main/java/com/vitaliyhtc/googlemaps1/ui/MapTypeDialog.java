package com.vitaliyhtc.googlemaps1.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.maps.GoogleMap;
import com.vitaliyhtc.googlemaps1.R;

public class MapTypeDialog extends DialogFragment {

    private int mSelectedMapType;
    private MapTypeSelectedListener mMapTypeSelectedListener;

    public void setSelectedMapType(int selectedMapType) {
        mSelectedMapType = selectedMapType;
    }

    public void setMapTypeSelectedListener(MapTypeSelectedListener mapTypeSelectedListener) {
        mMapTypeSelectedListener = mapTypeSelectedListener;
    }

    /*
     0 MAP_TYPE_NONE
     1 MAP_TYPE_NORMAL
     2 MAP_TYPE_SATELLITE
     3 MAP_TYPE_TERRAIN
     4 MAP_TYPE_HYBRID
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] mapTypeItems = {
                getString(R.string.map_type_normal),
                getString(R.string.map_type_satellite),
                getString(R.string.map_type_terrain),
                getString(R.string.map_type_hybrid)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.map_type_dialog_title);
        builder.setSingleChoiceItems(
                mapTypeItems,
                mSelectedMapType - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mapType;
                        switch (which) {
                            case 1:
                                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                                break;
                            case 2:
                                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                                break;
                            case 3:
                                mapType = GoogleMap.MAP_TYPE_HYBRID;
                                break;
                            default:
                                mapType = GoogleMap.MAP_TYPE_NORMAL;
                        }
                        mMapTypeSelectedListener.onMapTypeSelected(mapType);
                        dialog.dismiss();
                    }
                }
        );
        return builder.create();
    }

    public interface MapTypeSelectedListener {
        void onMapTypeSelected(int mapType);
    }
}
