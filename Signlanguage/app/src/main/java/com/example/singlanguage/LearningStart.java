package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LearningStart extends AppCompatActivity {
    String date; //오늘의 날짜
    int countword; //초기 학습 단어수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_start);

        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());//오늘날짜 가져오기

        TextView today = (TextView) findViewById(R.id.start_day);
        today.setText(date);

        //라디오 버튼 값 가져오기
        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
        Button b = (Button)findViewById(R.id.learnstart);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();
                //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
                RadioButton rb = (RadioButton) findViewById(id);
                countword = Integer.parseInt(rb.getText().toString());
                String name= dbToday.getName();
                dbToday.insert(name,date,countword,0,0); //db에 현재 날짜와 학습 단어 수 삽입
                Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                startActivity(intent); //todaylearning activity로 가기
            } // end onClick()
        });  // end Listener




    }
}
