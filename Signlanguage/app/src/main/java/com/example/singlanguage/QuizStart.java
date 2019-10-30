package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class QuizStart extends AppCompatActivity {
    //1: 학습 단어수, 2: 출제 범위
    private RadioGroup rGroup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        //라디오 그룹 설정
        rGroup1 = (RadioGroup) findViewById(R.id.RadioGrp1);
        //학습 시작 버튼
        Button start = (Button)findViewById(R.id.start);

        //퀴즈 시작 버튼 -> Quiz.java로 이동
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //체크한 학습할 단어 수 받기
                int rb = ((RadioGroup) rGroup1.findViewById(R.id.RadioGrp1)).getCheckedRadioButtonId();
                int num = 0;
                if(rb == R.id.radioButton1){ num = 5;}
                else if(rb == R.id.radioButton2){ num = 10;}
                else if(rb == R.id.radioButton3){ num = 15;}

                final Intent intent = new Intent(getApplicationContext(), Quiz.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                //학습할 단어 수 넘기기
                intent.putExtra("num", num);

                startActivity(intent);
            }
        });
    }
}
