package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CategoryLearning extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        final Intent intent = new Intent(CategoryLearning.this , learning.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("명사"));
        tabs.addTab(tabs.newTab().setText("동사"));
        tabs.addTab(tabs.newTab().setText("형용사"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        //탭 클릭 -> 해당 프래그먼트로 변경, 싱크
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

    }
    public void mOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.button_one)   intent.putExtra("name", "1");
        else if(v.getId() == R.id.button_two)   intent.putExtra("name", "2");
        else if(v.getId() == R.id.button_three)   intent.putExtra("name", "3");

        startActivity(intent);
    }

}
