package com.example.signlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TodayLearning extends AppCompatActivity {

    TextView tv_UserNameView;
    TextView tv_progress;
    ProgressBar pb_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_learning);
        tv_UserNameView = (TextView) findViewById(R.id.tv_userName);

        Button bt_start = findViewById(R.id.bt_start);
        Button bt_review = findViewById(R.id.bt_review);
        tv_UserNameView = findViewById(R.id.tv_userName);
        tv_progress = findViewById(R.id.tv_progress);
        pb_progress = findViewById(R.id.pb_progress);

        //오늘의 학습 페이지로 이동 TodayStart
        bt_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), TodayStart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
