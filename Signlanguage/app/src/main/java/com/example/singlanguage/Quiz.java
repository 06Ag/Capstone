package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.viewpager.widget.*;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class Quiz extends AppCompatActivity {

    /*ViewPager pager;
    Button bt_stop;*/
    TextView tv_imageNum;
    int pos = 1;    //수화 순서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Button stop = (Button) findViewById(R.id.bt_stop);
        tv_imageNum = findViewById(R.id.tv_image);

        //임시로 stop시 전페이지로
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();    //전페이지

                //퀴즈 스타트 페이지로
                /*final Intent intent = new Intent(getApplicationContext(), QuizStart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);*/
            }
        });
    }

    //onclick속성이 지정된 view를 클릭시 자동으로 호출됨
    //넘겨받은 단어 수만큼 수화 퀴즈 출제
    public void mOnClick(View v) {
        Intent intent = getIntent();
        int num = intent.getExtras().getInt("num");    //학습할 단어 수 받기

        if (v.getId() == R.id.bt_previous) {
            if (pos > 1) {
                pos = pos - 1;
                tv_imageNum.setText("수화 #" + Integer.toString(pos));
            } else {
                Toast toast = Toast.makeText(Quiz.this, "첫단어 입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            if (pos < num) {
                pos = pos + 1;
                tv_imageNum.setText("수화 #" + Integer.toString(pos));
            } else {
                Toast toast = Toast.makeText(Quiz.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
