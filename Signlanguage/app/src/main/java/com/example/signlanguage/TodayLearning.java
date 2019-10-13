package com.example.signlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TodayLearning extends AppCompatActivity {

    TextView tv_UserNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_learning);
        tv_UserNameView = (TextView) findViewById(R.id.tv_userName);


    }
}
