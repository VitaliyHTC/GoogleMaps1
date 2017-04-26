package com.vitaliyhtc.googlemaps1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewMarkerDialog extends DialogFragment {

    private LatLng mLatLng;
    private float mSelectedMarkerHue;

    @BindView(R.id.et_title)
    EditText mEditTextTitle;

    private NewMarkerDialogCallback mNewMarkerDialogCallback;

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public void setNewMarkerDialogCallback(NewMarkerDialogCallback newMarkerDialogCallback) {
        mNewMarkerDialogCallback = newMarkerDialogCallback;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_marker_dialog, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        builder.setTitle(R.string.new_marker_dialog_title);
        builder.setPositiveButton(R.string.new_marker_dialog_add_marker_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Do some actions
                onClickSubmit();
            }
        });
        builder.setNegativeButton(R.string.new_marker_dialog_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Do some action(). Nothing. We don't save shoppingCart to DB.
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    private void onClickSubmit() {
        if (mLatLng != null && mNewMarkerDialogCallback != null) {
            Marker marker = new Marker();
            marker.setTitle(mEditTextTitle.getText().toString());
            marker.setLatitude(mLatLng.latitude);
            marker.setLongitude(mLatLng.longitude);
            marker.setIconHue(mSelectedMarkerHue);

            mNewMarkerDialogCallback.onNewMarkerDialogSuccess(marker);
        }
    }

    @OnClick({
            R.id.iv_marker_red,
            R.id.iv_marker_orange,
            R.id.iv_marker_yellow,
            R.id.iv_marker_green,
            R.id.iv_marker_cyan,
            R.id.iv_marker_azure,
            R.id.iv_marker_blue,
            R.id.iv_marker_violet,
            R.id.iv_marker_magenta,
            R.id.iv_marker_rose
    })
    protected void onMarkerColorSelectClick(ImageView markerColor) {
        switch (markerColor.getId()) {
            case R.id.iv_marker_red:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_RED;
                break;
            case R.id.iv_marker_orange:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_ORANGE;
                break;
            case R.id.iv_marker_yellow:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case R.id.iv_marker_green:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_GREEN;
                break;
            case R.id.iv_marker_cyan:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_CYAN;
                break;
            case R.id.iv_marker_azure:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_AZURE;
                break;
            case R.id.iv_marker_blue:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_BLUE;
                break;
            case R.id.iv_marker_violet:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_VIOLET;
                break;
            case R.id.iv_marker_magenta:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_MAGENTA;
                break;
            case R.id.iv_marker_rose:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_ROSE;
                break;
            default:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_RED;
                break;
        }
    }

    public interface NewMarkerDialogCallback {
        void onNewMarkerDialogSuccess(Marker marker);
    }
}
