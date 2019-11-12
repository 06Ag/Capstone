package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TodayReview extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        final Intent intent = new Intent(TodayReview.this , TodayLearning.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_review);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기 지금까지 배운 총 단어 이름 위해
        final DBToday dbToday = DBToday.getInstance(getApplicationContext()); //DB가져오기

        int num = dbHelper.getCount(); //db에 저장되어있는 수화 수
        int countword = dbToday.getCount(); //db에 저장되어있는 오늘의 학습 단어 수
        //listview에 나타날 목록
        String[] LIST_NAME = new String[num];
        int[] LIST_LEARN = new int[num];

        int day = dbToday.getDay(); //학습한지 며칠째
        int firstword = 1;
        int lastword = day * countword; ; //학습 날짜에 맞게 학습했어야 한 마지막 단어의 _id

        int i = firstword;
        String name="";
        int chlearn = 0;
        for(; i <= num; i++){
            name = dbHelper.getName(i);
            chlearn = dbHelper.checkchlearn(i);
            LIST_NAME[i-1] = name;
            LIST_LEARN[i-1] = chlearn;
        }

        final ArrayList<String> list = new ArrayList<>();


        // ArrayList 객체에 데이터를 집어넣습니다.
        for (int t = 0; t < num; t++) {

            String temp;
            if(LIST_LEARN[t] ==1){
                temp = "o";
            }else{
                temp = "x";
            }
            //여기 나중에 간격조절해야할듯
            //temp가 너무 뒤죽박죽
            list.add("#"+LIST_NAME[t]+"                                        "+temp);

        }

        //listview에 LIST_NAME , LIST_LEARN내용 넣기
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        ListView listName = (ListView) findViewById(R.id.listname);
        listName.setAdapter(adapter);


        //listView 클릭 이벤트 처리
        listName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //textviwe의 텍스트 가져오기
                String strText = (String) parent.getItemAtPosition(position);
                //toast로 띄우기 test
                Toast toast = Toast.makeText(TodayReview.this, strText, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
