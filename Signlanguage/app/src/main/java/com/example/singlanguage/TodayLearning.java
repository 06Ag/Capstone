package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TodayLearning extends AppCompatActivity {

    TextView tv_UserName;   //이름
    TextView tv_userIntro;  //@ 님의 학습일지
    TextView tv_progress;   // # %
    TextView tv_day;    //학습일수
    TextView tv_Nday;   // #
    TextView tv_cnt;    //외운 단어 수
    TextView tv_Ncnt;   //#
    TextView tv_proTitle;   //오늘의 성취도
    ProgressBar pb_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_learning);

        Button bt_start = findViewById(R.id.bt_start);
        Button bt_review = findViewById(R.id.bt_review);
        tv_UserName = findViewById(R.id.tv_userName);
        tv_Ncnt = findViewById(R.id.tv_Ncnt);
        tv_progress = findViewById(R.id.tv_progress);
        pb_progress = findViewById(R.id.pb_progress);

        //오늘의 학습페이지(TodayStart)에서 가져온 학습한 단어 수
        Intent intent = getIntent();
        int today_cnt = intent.getIntExtra("count", 0);
        //학습한 단어 수 progress bar에 적용
        pb_progress.setProgress(today_cnt*10);
        tv_progress.setText(Integer.toString(today_cnt*10) + " %");

        //오늘의 학습 페이지로 이동 TodayStart
        bt_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //hsv setting 페이지로 넘길 때 이전페이지가 뭐였는지에 대한 정보 보내기
                intent.putExtra("page", 1);
                startActivity(intent);
            }
        });
        //오답노트 및 총 배운 단어 페이지로 이동 TodayReview
        bt_review.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), TodayReview.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }
}
