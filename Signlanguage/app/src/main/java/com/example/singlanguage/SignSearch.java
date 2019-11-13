package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");


        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
        // 데이터 출력
        final TextView result = (TextView) findViewById(R.id.signresult);
        result.setText(dbHelper.selectResult(name.trim()));
        Toast.makeText(getApplicationContext(), "데이터 조회", Toast.LENGTH_SHORT).show();
    }
}
