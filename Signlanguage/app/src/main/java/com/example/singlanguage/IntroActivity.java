package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroThread introThread = new IntroThread(handler);
        introThread.start();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                final DBToday dbToday = DBToday.getInstance(getApplicationContext()); //DB가져오기
                String na = dbToday.getName();
                Intent intent;
                //이름이 비워있다면
                if(na.equals("")){
                    intent = new Intent(IntroActivity.this, IntroName.class);
                }else{
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                }
                startActivity(intent);
            }
        }
    };
}
