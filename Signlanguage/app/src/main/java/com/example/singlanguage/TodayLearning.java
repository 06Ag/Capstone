package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
            final Intent intent = new Intent(TodayLearning.this , learning.class);
            startActivity(intent);
    }

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

        String firstdate = dbToday.getDate(); //db에 저장되있는 처음 학습 날짜 불러옴

        int day = 0; //학습한지 며칠째
        int oday = 0; //기존의 일

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String date = simpleDateFormat.format(new Date());//오늘날짜 가져오기

        String name = dbToday.getName();

        tv_UserName.setText(name);// db에 저장되어있는 사람이름

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

        if(day!=dbToday.getDay()){
            oday = dbToday.getDay(); //기존의 일수
            int firstword = dbHelper.getchlearn()+1;
            dbToday.updateday(oday,day); //새로운 일수로 update
            dbToday.updatefirstword(day,firstword);
            dbToday.updatepos(day,0);
            int chchange = dbToday.getChchange();
            //setting.java에서 count를 변경했을 경우
            if(chchange == 1){
                int ncount = dbToday.getNcount();
                dbToday.updatecount(name,ncount); //count 변경해주기
                dbToday.updatechchange(name); //변경했으니깐 chchange를 다시 0으로 만들어주기
            }
        }

        String temp = String.valueOf(day);
        tv_Nday.setText(temp); //학습 일자 update해주기

        final int wholeword = dbHelper.getchlearn();
        temp = String.valueOf(wholeword);
        tv_Ncnt.setText(temp);

        int countword = dbToday.getCount(); //db에 저장되어있는 오늘의 학습 단어 수

        //바 부분을 DBTODAY에다가 pos 컬럼을 만들어서 구현해야함
        //학습한 단어 수 progress bar에 적용
        float today_cnt = 0;
        today_cnt = dbToday.getPos(); //오늘 학습한 단어 가지고 오기

        today_cnt /= countword;
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
