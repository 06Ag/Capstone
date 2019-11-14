package com.example.singlanguage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainSetting extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private TextView show;
    private LinearLayout tlinear; //전체 화면 linear
    private LinearLayout linear1;
    private LinearLayout linear2;

    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        final Intent intent = new Intent(MainSetting.this , MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        final DBToday dbToday = DBToday.getInstance(getApplicationContext());
        //이름 변경하기
        Button nachange = (Button)findViewById(R.id.nachange);
        //단어 갯수 변경하기
        Button wochange = (Button)findViewById(R.id.wochange);
        tlinear = (LinearLayout)findViewById(R.id.tlinear); //전체화면 linear
        linear1= (LinearLayout)findViewById(R.id.linear1); //이름입력
        linear2= (LinearLayout)findViewById(R.id.linear2); //단어변경
        show = findViewById(R.id.show);  //사용자 이름이나 현재 단어 갯수가 뭔지 알려주는 textview

        final EditText Name = (EditText) findViewById(R.id.nickname);
        final RadioGroup rGroup = (RadioGroup) findViewById(R.id.wordchGroup); //  단어 수

        // edittext말고 다른데 터치시 키보드 창 없앰
        tlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Name.getWindowToken(), 0);
            }
        });

        nachange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = dbToday.getName(); //기존닉네임
                String chname = Name.getText().toString(); //새로운 닉네임
                dbToday.updatename(name,chname);

                builder = new AlertDialog.Builder(MainSetting.this);
                // 제목셋팅
                builder.setTitle("*알림*");
                // AlertDialog 셋팅
                builder.setMessage("이름 변경이 완료되었습니다.\n메인화면으로 돌아갑니다.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니요.",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                            linear1.setVisibility(View.GONE);
                                            show.setVisibility(View.GONE);
                                        // 다이얼로그를 취소한다
                                        dialog.cancel(); }
                                });

                builder.create().show();
            }
        });
        wochange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = dbToday.getName();
                int num =0; //바뀔학습단어수
                int rb = ((RadioGroup) rGroup.findViewById(R.id.wordchGroup)).getCheckedRadioButtonId();
                if(rb == R.id.word1){ num = 3;}
                else if(rb == R.id.word2){ num = 5;}
                else if(rb == R.id.word3){ num = 7;}
                dbToday.updatecount(name,num);
                builder = new AlertDialog.Builder(MainSetting.this);
                // 제목셋팅
                builder.setTitle("*알림*");
                // AlertDialog 셋팅
                builder.setMessage("단어 수 변경이 완료되었습니다.\n메인화면으로 돌아갑니다.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니요.",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        show.setVisibility(View.GONE);
                                        linear2.setVisibility(View.GONE);
                                        // 다이얼로그를 취소한다
                                        dialog.cancel(); }
                                });

                builder.create().show();
            }
        });

    }

    public void onButtonClick(View view) {
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());
        linear1= (LinearLayout)findViewById(R.id.linear1); //이름입력
        linear2= (LinearLayout)findViewById(R.id.linear2); //단어변경
        //사용자 이름이나 현재 단어 갯수가 뭔지 알려주는 textview
        show = findViewById(R.id.show);
        switch (view.getId()) {
            //이름 변경 layout보이게
            case R.id.nickchange :
                show.setVisibility(View.VISIBLE);
                String name = dbToday.getName();
                show.setText("현재 사용자의 이름은 " + name+"입니다.\n바꿀 이름을 입력하세요");
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.GONE);
                break ;
            //단어 갯수 변경 layout보이게
            case R.id.wordchange :
                show.setVisibility(View.VISIBLE);
                int countword= dbToday.getCount();
                show.setText("현재 사용자의 학습 단어 수는 " + countword +"입니다.\n바꿀 단어 수를 고르세요");
                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);
                break ;

        }
    }
}
