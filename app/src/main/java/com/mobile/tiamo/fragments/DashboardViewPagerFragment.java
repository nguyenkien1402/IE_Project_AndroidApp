package com.mobile.tiamo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.DashboardViewPagerAdapter;
import com.mobile.tiamo.dao.TiamoDatabase;


/**
 A dashboard fragment of the application
 This fragment contain a slide fragment
 which is Overview of the weekly activity
 Sleeping Time
 Step Taken
 **/

public class DashboardViewPagerFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    View view;
    TiamoDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    /*
    Init view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Find the view pager id
        viewPager = (ViewPager) view.findViewById(R.id.schedule_viewpager_1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        MainActivity.textToolbar.setText(R.string.app_name);

        // Set the tab layout
        tabLayout.setTabTextColors(Color.parseColor("#e3e3e3"), Color.parseColor("#ffffff"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));

        return view;
    }

    /*
     Init view pager of the dashboard
     */
    private void setupViewPager(ViewPager viewPager) {
        Log.d("TAG","Call again");
        DashboardViewPagerAdapter adapter = new DashboardViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DashboardViewActivityFragment(), "Activity");
        adapter.addFragment(new DashboardViewSleepingFragment(), "Sleeping");
        adapter.addFragment(new DashboardViewStepCounterFragment(), "Step");
        viewPager.setAdapter(adapter);
    }

}
