package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResult extends AppCompatActivity {

    TextView tv_result, tv_list_title;
    Button bt_quit;
    String[] quiz_list;
    String[] quiz_result;
    String[] LIST_MENU;
    int total, correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        tv_list_title = (TextView)findViewById(R.id.tv_list_title);
        tv_result = (TextView)findViewById(R.id.tv_result);
        bt_quit = (Button)findViewById(R.id.bt_quit);

        Intent intent = getIntent();
        quiz_list = intent.getStringArrayExtra("list");
        quiz_result = intent.getStringArrayExtra("list_result");
        total = intent.getIntExtra("Total", 0);
        correct = intent.getIntExtra("Correct", 0);
       //퀴즈 결과 list에 들어갈 문자열 포맷 작성
        LIST_MENU = new String[total];
        for(int i=0; i<total; i++){
            LIST_MENU[i] = String.format("#%02d   %s %s",(i+1), getRPad(quiz_list[i],35," "), quiz_result[i]);
        }
        //총 맞춘 개수 set
        tv_result.setText(correct + " / " + total);
        //리스트 title
        tv_list_title.setText(String.format("   #    %s 결과",getRPad("단어",27," ")));
        //listview에 LIST_MENU 내영 넣기
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        bt_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), learning.class);
                startActivity(intent1);
            }
        });
    }
    //str 오른쪽으로 자리수만큼 문자 채우기
    public static String getRPad(String str, int size, String strFillText) {
        for(int i = (str.getBytes()).length; i < size; i++) {
            str += strFillText;
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //back 버튼 누르면 learning페이지로 이동
        Intent intent = new Intent(getApplicationContext(), learning.class);
        startActivity(intent);
    }
}
