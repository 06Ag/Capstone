package com.example.singlanguage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                    Fragment6 tab1 = new Fragment6();
                    return tab1;
            case 1:
                Fragment5 tab2 = new Fragment5();
                return tab2;
            case 2:
                Fragment4 tab3 = new Fragment4();
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