package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Bundle;

public class HsvSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsv_setting);

        int devalue = 50;
        SeekBar  seekBar;

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView tv=(TextView)findViewById(R.id.LHval);

        seekBar.setProgress(devalue); //초기값을 50으로 설정
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 메소드 이름대로 사용자가 SeekBar를 터치했을때 실행됩니다
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
// 메소드 이름대로 사용자가 SeekBar를 손에서 땠을때 실행됩니다
// TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
// 메소드 이름대로 사용자가 SeekBar를 움직일때 실행됩니다
// 주로 사용되는 메소드 입니다
                tv.setText(""+progress);
            }
        });


        Button bt_st= (Button)findViewById(R.id.finish);
        bt_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), Translation.class);
                startActivity(intent);
            }
        });

    }
}
