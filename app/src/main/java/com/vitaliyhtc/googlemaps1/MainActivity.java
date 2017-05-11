package com.vitaliyhtc.googlemaps1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.vitaliyhtc.googlemaps1.adapter.ViewPagerAdapter;
import com.vitaliyhtc.googlemaps1.view.lib.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private int numberOfTabs = 2;

    private CharSequence tabsTitles[] = new CharSequence[numberOfTabs];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
    }

    private void initViewPager() {

        tabsTitles[0] = getString(R.string.title_activity_maps);
        tabsTitles[1] = getString(R.string.title_activity_markers);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabsTitles, numberOfTabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(false);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        slidingTabLayout.setViewPager(viewPager);
    }
}
