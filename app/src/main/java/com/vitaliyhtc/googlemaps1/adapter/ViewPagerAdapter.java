package com.vitaliyhtc.googlemaps1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vitaliyhtc.googlemaps1.view.MapsFragment;
import com.vitaliyhtc.googlemaps1.view.MarkersListFragment;

// TODO: 06/05/17 check https://boosting.atlassian.net/wiki/display/ACSS/2.+Code+style#id-2.Codestyle-Comments
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build exxpArray Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MapsFragment();
        } else {
            return new MarkersListFragment();
        }
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
