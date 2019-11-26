package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CategoryLearning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("카테고리 학습") ;

        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("숫자"));
        tabs.addTab(tabs.newTab().setText("자/모음"));
        tabs.addTab(tabs.newTab().setText("사물"));
        tabs.addTab(tabs.newTab().setText("사람"));
        tabs.addTab(tabs.newTab().setText("음식"));
        tabs.addTab(tabs.newTab().setText("기타"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 6);
        viewPager.setAdapter(myPagerAdapter);

        //탭 클릭 -> 해당 프래그먼트로 변경, 싱크
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

    }

    //액션바에 검색 기능 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;

        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setQueryHint("수화 이름으로 검색합니다.");
        final MenuItem item_setting = menu.add(0,0,0,"환경설정");
        item_setting.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {    //환경설정으로 넘어감
            public boolean onMenuItemClick (MenuItem item){
                Intent intent = new Intent(getApplicationContext(), MainSetting.class);
                intent.putExtra("page", 2); //환경설정페이지에 보낼 때 카테고리 학습에서 이동된 것을 알려줌
                startActivity(intent);
                return true;
            }
        });

        return true ;
    }

    //액션바 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            //String na = Name.getText().toString();
            final Intent intent = new Intent(getApplicationContext(), SignSearch.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("name","기역");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), learning.class);
        startActivity(intent);
    }
}
