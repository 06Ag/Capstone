package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SignSearch extends AppCompatActivity {

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
