package com.example.singlanguage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SignSearch extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        final Intent intent = new Intent(SignSearch.this , learning.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_search);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("수화 검색") ;

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
        final String[][] result = dbHelper.selectResult(name.trim());
        // 데이터 출력
        final ListView lv_result = (ListView) findViewById(R.id.signresult);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result[1]);
        lv_result.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "데이터 조회", Toast.LENGTH_SHORT).show();

        //결과 누르면 학습 페이지로 넘어감
        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), CategoryLearning_study_info.class);
                intent.putExtra("name", result[0][position]);
                startActivity(intent);
            }
        });
    }
}
