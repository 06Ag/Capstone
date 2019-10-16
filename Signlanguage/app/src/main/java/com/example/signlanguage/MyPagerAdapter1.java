package com.example.signlanguage;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyPagerAdapter1 extends FragmentPagerAdapter {
    int mNumOfTabs; //tab 갯수

    public MyPagerAdapter1(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                    Fragment1_1 tab1 = new Fragment1_1();
                    return tab1;
            case 1:
                Fragment1_2 tab2 = new Fragment1_2();
                return tab2;
            case 2:
                Fragment1_3 tab3 = new Fragment1_3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}