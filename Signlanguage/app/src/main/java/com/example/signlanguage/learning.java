package com.example.signlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;

public class learning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        Button bt_today = findViewById(R.id.button);
        bt_today.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
