package com.vitaliyhtc.googlemaps1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vitaliyhtc.googlemaps1.view.MapsFragment;
import com.vitaliyhtc.googlemaps1.view.MarkersListFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[];
    private int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MapsFragment();
        } else {
            return new MarkersListFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
