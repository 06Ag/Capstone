package com.example.singlanguage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class learning extends AppCompatActivity {
    private LinearLayout tttlinear; //전체 화면 linear
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        final Intent intent = new Intent(learning.this , MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        final String temp = dbToday.getDate();
        System.out.println("DB저장 날짜: "+temp);

        //sign 검색 결과 페이지로 이동 SignSearch.java
        final EditText Name = (EditText) findViewById(R.id.searchtext);
        Button bt_search = findViewById(R.id.search);
        tttlinear = (LinearLayout)findViewById(R.id.tttlinear); //전체화면 linear

        //입력키보드 & 커서 사라지게
        tttlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Name.getWindowToken(), 0);
                Name.clearFocus();
            }
        });
        //입력완료후 커서 사라지게
        Name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Name.clearFocus();
                return false;
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String na = Name.getText().toString();
                final Intent intent = new Intent(getApplicationContext(), SignSearch.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("name",na);
                startActivity(intent);
            }
        });

        //일일학습 페이지로 이동 TodayLearning.java
        Button bt_today = findViewById(R.id.button);
        bt_today.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //처음 학습 시작한 날 저장
                if(temp == ""){
                    Intent intent = new Intent(getApplicationContext(), LearningStart.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                }else{
                    final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

            }
        });


        //카테고리학습 페이지로 이동 CategoryLearning.java
        Button bt_category = findViewById(R.id.button2);
        bt_category.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), CategoryLearning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //퀴즈 페이지로 이동 QuizStart.java
        Button bt_quiz = findViewById(R.id.button3);
        bt_quiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), QuizStart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
