package com.vitaliyhtc.googlemaps1.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.adapter.MarkerItemsAdapter;
import com.vitaliyhtc.googlemaps1.model.MarkerColorItem;
import com.vitaliyhtc.googlemaps1.model.MarkerInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerDialog extends DialogFragment {

    @BindView(R.id.et_title)
    EditText mEditTextTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private LatLng mLatLng;
    private float mSelectedMarkerHue;
    private MarkerDialogCallback mMarkerDialogCallback;

    private MarkerInfo mMarkerInfo;

    private List<MarkerColorItem> mMarkerColorItems;


    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public void setMarkerInfo(MarkerInfo markerInfo) {
        mMarkerInfo = markerInfo;
    }

    public void setMarkerDialogCallback(MarkerDialogCallback markerDialogCallback) {
        mMarkerDialogCallback = markerDialogCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_marker_dialog, null);
        ButterKnife.bind(this, view);
        initMarkerColorsRecyclerView();
        builder.setView(view);
        String positiveButtonString;
        if (mMarkerInfo == null) {
            builder.setTitle(R.string.new_marker_dialog_title);
            positiveButtonString = getString(R.string.new_marker_dialog_add_marker_button);
        } else {
            builder.setTitle(R.string.edit_marker_dialog_title);
            positiveButtonString = getString(R.string.marker_dialog_save_marker_button);
            fillDataFromMarker(mMarkerInfo);
        }
        builder.setCancelable(true);
        builder.setPositiveButton(positiveButtonString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                onClickSubmit();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Do some action. Nothing.
            }
        });
        return builder.create();
    }

    private void onClickSubmit() {
        MarkerInfo markerInfo = new MarkerInfo();
        if (mMarkerInfo != null && mLatLng == null) {
            mLatLng = new LatLng(mMarkerInfo.getLatitude(), mMarkerInfo.getLongitude());
            markerInfo.setId(mMarkerInfo.getId());
        }
        if (mLatLng != null && mMarkerDialogCallback != null) {
            markerInfo.setTitle(mEditTextTitle.getText().toString());
            markerInfo.setLatitude(mLatLng.latitude);
            markerInfo.setLongitude(mLatLng.longitude);
            markerInfo.setIconHue(mSelectedMarkerHue);

            mMarkerDialogCallback.onMarkerDialogSuccess(markerInfo);
        }
    }

    private void initMarkerColorsRecyclerView() {
        initMarkerColorsList();
        MarkerItemsAdapter adapter = new MarkerItemsAdapter(new MarkerItemsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                mSelectedMarkerHue = mMarkerColorItems.get(position).getMarkerHue();
            }
        });
        adapter.setMarkerColorItems(mMarkerColorItems);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initMarkerColorsList() {
        mMarkerColorItems = new ArrayList<>();
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_red_48dp,
                getString(R.string.new_marker_dialog_red_color_marker),
                BitmapDescriptorFactory.HUE_RED
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_orange_48dp,
                getString(R.string.new_marker_dialog_orange_color_marker),
                BitmapDescriptorFactory.HUE_ORANGE
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_yellow_48dp,
                getString(R.string.new_marker_dialog_yellow_color_marker),
                BitmapDescriptorFactory.HUE_YELLOW
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_green_48dp,
                getString(R.string.new_marker_dialog_green_color_marker),
                BitmapDescriptorFactory.HUE_GREEN
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_cyan_48dp,
                getString(R.string.new_marker_dialog_cyan_color_marker),
                BitmapDescriptorFactory.HUE_CYAN
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_azure_48dp,
                getString(R.string.new_marker_dialog_azure_color_marker),
                BitmapDescriptorFactory.HUE_AZURE
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_blue_48dp,
                getString(R.string.new_marker_dialog_blue_color_marker),
                BitmapDescriptorFactory.HUE_BLUE
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_violet_48dp,
                getString(R.string.new_marker_dialog_violet_color_marker),
                BitmapDescriptorFactory.HUE_VIOLET
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_magenta_48dp,
                getString(R.string.new_marker_dialog_magenta_color_marker),
                BitmapDescriptorFactory.HUE_MAGENTA
        ));
        mMarkerColorItems.add(new MarkerColorItem(
                R.drawable.ic_place_rose_48dp,
                getString(R.string.new_marker_dialog_rose_color_marker),
                BitmapDescriptorFactory.HUE_ROSE
        ));
    }


    private void fillDataFromMarker(final MarkerInfo markerInfo) {
        if (markerInfo != null) {
            mEditTextTitle.setText(markerInfo.getTitle());
            mSelectedMarkerHue = markerInfo.getIconHue();
        }
    }

    public interface MarkerDialogCallback {
        void onMarkerDialogSuccess(MarkerInfo markerInfo);
    }
}
