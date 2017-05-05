package com.vitaliyhtc.googlemaps1.model;

import android.support.v4.app.Fragment;

public class FragmentWrap {
    private Fragment fragment;

    public FragmentWrap(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
