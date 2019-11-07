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
        final DBToday dbToday = DBToday.getInstance(getApplicationContext()); //DB가져오기 progressbar를 위헤
        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기 지금까지 배운 총 단어 갯수 위해
        Button bt_start = findViewById(R.id.bt_start);
        Button bt_review = findViewById(R.id.bt_review);
        tv_UserName = findViewById(R.id.tv_userName);
        tv_Ncnt = findViewById(R.id.tv_Ncnt);
        tv_progress = findViewById(R.id.tv_progress);
        pb_progress = findViewById(R.id.pb_progress);
        tv_Nday = findViewById(R.id.tv_Nday);

        //db에서 가져온 학습 일자
        final int day = dbToday.getDay();
        //db에서 가져온 학습 단어 수
        final int countword = dbToday.getCount();
        System.out.println("가져온 day:::"+ day) ;
        System.out.println("가져온 count:::"+ countword) ;

        String temp = String.valueOf(day);
        tv_Nday.setText(temp); //학습 일자 update해주기

        final int wholeword = dbHelper.getchlearn();
        System.out.println("지금까지 배운 단어:::"+ wholeword) ;
        temp = String.valueOf(wholeword);
        tv_Ncnt.setText(temp);


        //바 부분을 DBTODAY에다가 pos 컬럼을 만들어서 구현해야함
        //학습한 단어 수 progress bar에 적용
        float today_cnt = 0;
        today_cnt = dbToday.getPos(); //오늘 학습한 단어 가지고 오기
        System.out.println("오늘 학습한 단어수? "+today_cnt);
        System.out.println("오늘 학습해야 했을 단어수? "+countword);
        today_cnt /= countword;
        System.out.println("진행 값:"+today_cnt);
        today_cnt *= 100;
        int t = (int)today_cnt;
        pb_progress.setProgress(t);
        tv_progress.setText(Integer.toString(t) + " %");


        //오늘의 학습 페이지로 이동 TodayStart
        bt_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //hsv setting 페이지로 넘길 때 이전페이지가 뭐였는지에 대한 정보 보내기
                intent.putExtra("page", 1);
                intent.putExtra("day",day) ; //오늘의 학습 페이지로 학습 날짜 넘기기
                intent.putExtra("countword",countword); //오늘의 학습 페이지로 학습할 단어 수 넘기기

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
