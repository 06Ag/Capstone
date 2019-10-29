package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class TodayReview extends AppCompatActivity {
    //listview에 나타날 목록
    static final String[] LIST_MENU = {"기역", "니은", "디귿", "리을"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_review);

        //listview에 LIST_MENU 내영 넣기
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        //listView 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
