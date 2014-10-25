package com.project.pes.kicheko.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.pes.kicheko.ReadActivity;
import com.project.pes.kicheko.topFunnyFragment;
import com.project.pes.kicheko.topScaryFragment;

/**
 * Created by apple on 25/10/14.
 */
/**
 * Created by apple on 20/07/14.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ReadActivity();
            case 1:
                // Games fragment activity
                return new topFunnyFragment();
            case 2:
                // Movies fragment activity
                return new topScaryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}