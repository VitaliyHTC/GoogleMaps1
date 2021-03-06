package com.vitaliyhtc.googlemaps1.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.MarkerInfoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerInfoOptionsDialog extends DialogFragment {

    @BindView(R.id.marker_title)
    TextView mTitleTextView;

    private MarkerInfoItem mMarkerInfoItem;
    private MarkerInfoOptionsDialogCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_marker_info_and_options, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        builder.setTitle(R.string.marker_info_options);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.marker_dialog_edit_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                onClickEdit();
            }
        });
        builder.setNeutralButton(R.string.marker_dialog_delete_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickDelete();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Do some action(). Nothing. We don't save shoppingCart to DB.
            }
        });
        showMarkerInfo();
        return builder.create();
    }

    public void setMarker(MarkerInfoItem markerInfoItem) {
        mMarkerInfoItem = markerInfoItem;
    }

    public void setCallback(MarkerInfoOptionsDialogCallback callback) {
        this.callback = callback;
    }

    private void onClickEdit() {
        if (callback != null) {
            callback.onMarkerEdit(mMarkerInfoItem);
        }
    }

    private void onClickDelete() {
        if (callback != null) {
            callback.onMarkerDelete(mMarkerInfoItem);
        }
    }

    private void showMarkerInfo() {
        if (mMarkerInfoItem != null) {
            mTitleTextView.setText(mMarkerInfoItem.getTitle());
        }
    }

    public interface MarkerInfoOptionsDialogCallback {
        void onMarkerEdit(MarkerInfoItem markerInfoItem);
        void onMarkerDelete(MarkerInfoItem markerInfoItem);
    }
}
