package com.example.singlanguage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class Fragment1 extends Fragment {

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_fragment1);

        //findViewById 에러 막기 위해서 view에 담아서 진행
        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);
        v.setTag(3);
        //TabLayout
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs1);
        tabs.addTab(tabs.newTab().setText("사물"));
        tabs.addTab(tabs.newTab().setText("동물"));
        tabs.addTab(tabs.newTab().setText("음식"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager1);
        final MyPagerAdapter1 myPagerAdapter1 = new MyPagerAdapter1(getFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter1);

        //탭 클릭 -> 해당 프래그먼트로 변경, 싱크
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        return v;
    }
}
