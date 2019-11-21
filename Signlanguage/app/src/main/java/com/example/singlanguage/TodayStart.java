package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TodayStart extends AppCompatActivity {

    ListView listView;
    Button bt_start;    //학습 시작 버튼

    int countword = 0; //학습할 단어 수
    int firstword = 0; //처음으로 학습할 단어의 _id
    int lastword = 0; //마지막으로 학습할 단어의 _id
    int day = 0; //학습 일 수
    int dbcount = 0; //db안에 있는 수어 수
    String[] LIST_MENU; //학습할 단어들 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_start);

        listView = (ListView) findViewById(R.id.listview);
        bt_start = (Button) findViewById(R.id.bt_start);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        countword = dbToday.getCount(); //하루 당 학습해야하는 단어 가져옴
        day = dbToday.getDay(); //며칠째인지 가져옴

        dbcount = dbHelper.getCount();//db안에 있는 수어 수

        firstword = dbToday.getFirstword(); //처음으로 배울 단어
        System.out.println("fistword: "+firstword);
        lastword = dbToday.getFirstword() + countword - 1; //마지막으로 배울 단어

        //db에서 더이상 배울 단어가 없을 경우
        if (firstword > dbcount) {
            Toast.makeText(getApplicationContext(), "더이상 배울 단어가 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), TodayLearning.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
        }

        //LIST_MENU 배열에 오늘 학습할 단어 목록 DB로부터 불러우기
        LIST_MENU = new String[countword];
        for(int i=firstword, j=0; i<=lastword; i++, j++){   // i = DB에서 값 불러올때 사용, j = LIST_MENU 배열에 값 넣을 때 사용
            LIST_MENU[j] = dbHelper.getName(i);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        listView.setAdapter(adapter);

        //학습 시작 버튼 클릭시 단어 배우는 창으로 넘어감
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), TodayStart_study_info.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
        startActivity(intent);
    }
}