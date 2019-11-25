package com.example.singlanguage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
        // 데이터 출력
        final TextView result = (TextView) findViewById(R.id.signresult);
        result.setText(dbHelper.selectResult(name.trim()));
        Toast.makeText(getApplicationContext(), "데이터 조회", Toast.LENGTH_SHORT).show();

        //결과 누르면 학습 페이지로 넘어감
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_res = getIntent();
                String name = intent_res.getExtras().getString("name");
                result.setTextColor(Color.BLACK);
                final Intent intent = new Intent(getApplicationContext(), CategoryLearning_study_info.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }


        });
    }
}
