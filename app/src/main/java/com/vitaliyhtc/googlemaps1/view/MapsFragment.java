package com.vitaliyhtc.googlemaps1.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.vitaliyhtc.googlemaps1.R;
import com.vitaliyhtc.googlemaps1.model.FragmentWrap;
import com.vitaliyhtc.googlemaps1.presenter.MapsPresenter;
import com.vitaliyhtc.googlemaps1.presenter.MapsPresenterImpl;
import com.vitaliyhtc.googlemaps1.util.PermissionUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

// 03/05/17 options to switch between map styles (satalite, terrain, mixed, custom)
// Ooops. MapType selection done. For mapStyles i can use this example:
// https://github.com/googlemaps/android-samples/blob/master/ApiDemos/app/src/main/java/com/example/mapdemo/StyledMapDemoActivity.java
// https://developers.google.com/maps/documentation/android-api/styling

public class MapsFragment extends Fragment
        implements MapsView, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MapsPresenter mMapsPresenter;

    private boolean mPermissionDenied = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        // fix for mysterious black view
        // http://stackoverflow.com/questions/13837697/viewpager-with-google-maps-api-v2-mysterious-black-view
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) view).addView(frameLayout,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapsPresenter = new MapsPresenterImpl(new FragmentWrap(MapsFragment.this));
        mMapsPresenter.onAttachView(MapsFragment.this);
        mMapsPresenter.onCreate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapsPresenter.onDetachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapsPresenter.onStop();
    }

    @OnClick(R.id.iv_map_type_switch)
    protected void onMapTypeSwitchClick() {
        mMapsPresenter.onMapTypeSwitchClick();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            GoogleMap map = mMapsPresenter.getMap();
            if (map != null) {
                // Access to the location has been granted to the app.
                map.setMyLocationEnabled(true);
            }
        }
    }

    // TODO: 06/05/17 use dexter for permissions
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }

}