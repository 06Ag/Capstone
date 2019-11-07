package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class learning extends AppCompatActivity {

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

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String date = simpleDateFormat.format(new Date());//오늘날짜 가져오기

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
                    String firstdate = dbToday.getDate(); //db에 저장되있는 처음 학습 날짜 불러옴
                    int countword = dbToday.getCount(); //db에 저장되어있는 오늘의 학습 단어 수
                    int day = 0; //학습한지 며칠째

                    System.out.println("최초 학습 시작일 : " +firstdate);
                    System.out.println("현재 학습일 : " +date);
                    System.out.println("학습할 단어 수: " +dbToday.getCount());
                    Date beginDate = null;
                    Date endDate = null;
                    try {
                        beginDate = simpleDateFormat.parse(firstdate);
                        endDate = simpleDateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
                    long diff = endDate.getTime() - beginDate.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    day = (int)diffDays;
                    day += 1; //0일째면 1일째라고 하기 위해서 더해줌
                    System.out.println("학습 일수 : "+ day);
                    dbToday.updateday(countword,day);

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
