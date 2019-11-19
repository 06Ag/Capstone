package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CategoryLearning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("숫자"));
        tabs.addTab(tabs.newTab().setText("자/모음"));
        tabs.addTab(tabs.newTab().setText("사물"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        //탭 클릭 -> 해당 프래그먼트로 변경, 싱크
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

    }

}
