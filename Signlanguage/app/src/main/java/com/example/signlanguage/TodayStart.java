package com.example.signlanguage;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.viewpager.widget.*;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class TodayStart extends AppCompatActivity {

    ViewPager pager;
    Button bt_stop;
    TextView tv_imageNum;
    int pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_start);

        tv_imageNum = findViewById(R.id.tv_image);
        bt_stop = findViewById(R.id.bt_stop);

        //stop시 일일학습페이지로 넘어가며 현 학습한 단어 개수(임의로 pos) 반환
        bt_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("count", pos);
                startActivity(intent);
            }
        });

    }

    //onclick속성이 지정된 view를 클릭시 자동으로 호출됨
    //10개로 단어개수 지정함
    //previous button, next button
    public void mOnClick(View v){
        if(v.getId() == R.id.bt_previous){
            if(pos > 1) {
                pos = pos - 1;
                tv_imageNum.setText("수화 #" + Integer.toString(pos));
            }
            else{
                Toast toast = Toast.makeText(TodayStart.this, "처음 단어 입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            if(pos <10) {
                pos = pos + 1;
                tv_imageNum.setText("수화 #" + Integer.toString(pos));
            }
            else{
                Toast toast = Toast.makeText(TodayStart.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
