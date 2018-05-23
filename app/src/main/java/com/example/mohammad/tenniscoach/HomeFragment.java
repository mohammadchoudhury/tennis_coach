package com.example.mohammad.tenniscoach;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SessionActivity.class));
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getPosition() == 0) {
                    Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    fab.setVisibility(View.VISIBLE);
                    fab.startAnimation(fadeIn);
                } else {
                    Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                    fab.startAnimation(fadeOut);
                    fab.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NavigationView) getActivity().findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_home);
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SessionTabFragment();
            }
            return new BookingTabFragment().newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}

