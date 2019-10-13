package com.example.signlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml화면 보임
        setContentView(R.layout.activity_main);

        Button b = (Button)findViewById(R.id.Learning);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        learning.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

    }

}
