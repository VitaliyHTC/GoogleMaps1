package com.vitaliyhtc.googlemaps1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.vitaliyhtc.googlemaps1.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.vitaliyhtc.googlemaps1.Config.KEY_MARKER_ID;

public class MarkerDialog extends DialogFragment {

    @BindView(R.id.et_title)
    EditText mEditTextTitle;

    @BindView(R.id.iv_marker_red)
    ImageView mMarkerRed;
    @BindView(R.id.iv_marker_orange)
    ImageView mMarkerOrange;
    @BindView(R.id.iv_marker_yellow)
    ImageView mMarkerYellow;
    @BindView(R.id.iv_marker_green)
    ImageView mMarkerGreen;
    @BindView(R.id.iv_marker_cyan)
    ImageView mMarkerCyan;
    @BindView(R.id.iv_marker_azure)
    ImageView mMarkerAzure;
    @BindView(R.id.iv_marker_blue)
    ImageView mMarkerBlue;
    @BindView(R.id.iv_marker_violet)
    ImageView mMarkerViolet;
    @BindView(R.id.iv_marker_magenta)
    ImageView mMarkerMagenta;
    @BindView(R.id.iv_marker_rose)
    ImageView mMarkerRose;

    List<ImageView> mMarkers;
    private ImageView mSelectedImageView;
    private LatLng mLatLng;
    private float mSelectedMarkerHue;
    private MarkerDialogCallback mMarkerDialogCallback;

    private com.google.android.gms.maps.model.Marker mMarker;

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public void setMarkerDialogCallback(MarkerDialogCallback markerDialogCallback) {
        mMarkerDialogCallback = markerDialogCallback;
    }

    public void setMarker(com.google.android.gms.maps.model.Marker marker) {
        mMarker = marker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_marker_dialog, null);
        ButterKnife.bind(this, view);
        fillMarkersList();
        builder.setView(view);
        String positiveButtonString;
        if (mMarker == null) {
            builder.setTitle(R.string.new_marker_dialog_title);
            positiveButtonString = getString(R.string.new_marker_dialog_add_marker_button);
        } else {
            builder.setTitle(R.string.edit_marker_dialog_title);
            positiveButtonString = getString(R.string.marker_dialog_save_marker_button);
            fillDataFromMarker(mMarker);
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
                // Do some action. Nothing. We don't save shoppingCart to DB.
            }
        });
        return builder.create();
    }

    private void onClickSubmit() {
        if (mMarker != null && mLatLng == null) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    com.vitaliyhtc.googlemaps1.model.Marker marker = realm
                            .where(com.vitaliyhtc.googlemaps1.model.Marker.class)
                            .equalTo(KEY_MARKER_ID, (String) mMarker.getTag())
                            .findFirst();

                    mLatLng = new LatLng(marker.getLatitude(), marker.getLongitude());
                }
            });
            realm.close();
        }
        if (mLatLng != null && mMarkerDialogCallback != null) {
            Marker marker = new Marker();
            if (mMarker != null) {
                marker.setId((String) mMarker.getTag());
            } else {
                marker.setId(UUID.randomUUID().toString());
            }
            marker.setTitle(mEditTextTitle.getText().toString());
            marker.setLatitude(mLatLng.latitude);
            marker.setLongitude(mLatLng.longitude);
            marker.setIconHue(mSelectedMarkerHue);

            mMarkerDialogCallback.onMarkerDialogSuccess(marker);
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
                mSelectedImageView = mMarkerRed;
                break;
            case R.id.iv_marker_orange:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_ORANGE;
                mSelectedImageView = mMarkerOrange;
                break;
            case R.id.iv_marker_yellow:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_YELLOW;
                mSelectedImageView = mMarkerYellow;
                break;
            case R.id.iv_marker_green:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_GREEN;
                mSelectedImageView = mMarkerGreen;
                break;
            case R.id.iv_marker_cyan:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_CYAN;
                mSelectedImageView = mMarkerCyan;
                break;
            case R.id.iv_marker_azure:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_AZURE;
                mSelectedImageView = mMarkerAzure;
                break;
            case R.id.iv_marker_blue:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_BLUE;
                mSelectedImageView = mMarkerBlue;
                break;
            case R.id.iv_marker_violet:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_VIOLET;
                mSelectedImageView = mMarkerViolet;
                break;
            case R.id.iv_marker_magenta:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_MAGENTA;
                mSelectedImageView = mMarkerMagenta;
                break;
            case R.id.iv_marker_rose:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_ROSE;
                mSelectedImageView = mMarkerRose;
                break;
            default:
                mSelectedMarkerHue = BitmapDescriptorFactory.HUE_RED;
                mSelectedImageView = mMarkerRed;
                break;
        }
        updateSelectedMarkerUi();
    }

    private void fillMarkersList() {
        mMarkers = new ArrayList<>();
        ImageView[] markers = {
                mMarkerRed,
                mMarkerOrange,
                mMarkerYellow,
                mMarkerGreen,
                mMarkerCyan,
                mMarkerAzure,
                mMarkerBlue,
                mMarkerViolet,
                mMarkerMagenta,
                mMarkerRose
        };
        mMarkers.addAll(Arrays.asList(markers));
        mSelectedImageView = mMarkerRed;
    }

    private void fillDataFromMarker(final com.google.android.gms.maps.model.Marker marker) {
        if (marker != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    com.vitaliyhtc.googlemaps1.model.Marker marker1 = realm
                            .where(com.vitaliyhtc.googlemaps1.model.Marker.class)
                            .equalTo(KEY_MARKER_ID, (String) marker.getTag())
                            .findFirst();

                    mEditTextTitle.setText(marker1.getTitle());
                    mSelectedMarkerHue = marker1.getIconHue();
                    setSelectedImageViewFromHue(mSelectedMarkerHue);
                }
            });
            realm.close();
        }
    }

    private void setSelectedImageViewFromHue(float hue) {
        if (hue == BitmapDescriptorFactory.HUE_RED) {
            mSelectedImageView = mMarkerRed;
        } else if (hue == BitmapDescriptorFactory.HUE_ORANGE) {
            mSelectedImageView = mMarkerOrange;
        } else if (hue == BitmapDescriptorFactory.HUE_YELLOW) {
            mSelectedImageView = mMarkerYellow;
        } else if (hue == BitmapDescriptorFactory.HUE_GREEN) {
            mSelectedImageView = mMarkerGreen;
        } else if (hue == BitmapDescriptorFactory.HUE_CYAN) {
            mSelectedImageView = mMarkerCyan;
        } else if (hue == BitmapDescriptorFactory.HUE_AZURE) {
            mSelectedImageView = mMarkerAzure;
        } else if (hue == BitmapDescriptorFactory.HUE_BLUE) {
            mSelectedImageView = mMarkerBlue;
        } else if (hue == BitmapDescriptorFactory.HUE_VIOLET) {
            mSelectedImageView = mMarkerViolet;
        } else if (hue == BitmapDescriptorFactory.HUE_MAGENTA) {
            mSelectedImageView = mMarkerMagenta;
        } else if (hue == BitmapDescriptorFactory.HUE_ROSE) {
            mSelectedImageView = mMarkerRose;
        } else {
            mSelectedImageView = mMarkerRed;
        }
        updateSelectedMarkerUi();
    }

    private void updateSelectedMarkerUi() {
        for (ImageView markerImageView : mMarkers) {
            if (markerImageView.equals(mSelectedImageView)) {
                markerImageView.setBackgroundColor(Color.LTGRAY);
            } else {
                markerImageView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public interface MarkerDialogCallback {
        void onMarkerDialogSuccess(Marker marker);
    }
}
