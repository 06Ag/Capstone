package com.example.singlanguage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QuizStart extends AppCompatActivity {
    private LinearLayout ttlinear; //전체 화면 linear
    //1: 학습 단어수, 2: 출제 범위
    private EditText wordnum;
    private RadioGroup rGroup2;
    private RadioGroup rGroup3;
    private AlertDialog.Builder builder;
    int num = 0;
    int temp =0;
    String result="";
    int lastword = 0; //마지막으로 학습할 단어의 _id
    int day = 0; //학습 일 수
    int countword =0; //학습할 단어 수

    //라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup rGroup2, @IdRes int i) {
            if(i == R.id.radioButton5){
                rGroup3.setVisibility(View.VISIBLE);
            }
            else {
                rGroup3.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        ttlinear = (LinearLayout)findViewById(R.id.ttlinear); //전체화면 linear
        //학습 시작 버튼
        Button start = (Button)findViewById(R.id.start);
        //단어 더하기, 빼기 버튼
        Button add = (Button)findViewById(R.id.add);
        Button sub = (Button)findViewById(R.id.sub);

        wordnum = (EditText) findViewById(R.id.wordnum); // 퀴즈 학습 단어 수;
        //라디오 그룹 설정
        rGroup2= (RadioGroup) findViewById(R.id.RadioGrp2); // 퀴즈 범위;
        rGroup3= (RadioGroup) findViewById(R.id.RadioGrp3); // 품사
        rGroup2.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        countword = dbToday.getCount(); //하루 당 학습해야하는 단어 가져옴
        day = dbToday.getDay(); //며칠째인지 가져옴

        // edittext말고 다른데 터치시 키보드 창 없앰
        ttlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(wordnum.getWindowToken(), 0);
                wordnum.clearFocus();
            }
        });
        wordnum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                wordnum.clearFocus();
                return false;
            }
        });


        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int temp=0;
                String temp2 ="";
                temp =Integer.parseInt(wordnum.getText().toString());
                temp += 1;
                temp2 = Integer.toString(temp);
                wordnum.setText(temp2);
            }
        });

        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int temp=0;
                String temp2 ="";
                temp =Integer.parseInt(wordnum.getText().toString());
                temp -= 1;
                if(temp <1){
                    temp = 1;
                }
                temp2 = Integer.toString(temp);
                wordnum.setText(temp2);
            }
        });
        //퀴즈 시작 버튼 -> Quiz.java로 이동
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //체크한 학습할 단어 수 받기
                num =Integer.parseInt(wordnum.getText().toString());
                //퀴즈 범위 받아 오기
                int rb2 = ((RadioGroup) rGroup2.findViewById(R.id.RadioGrp2)).getCheckedRadioButtonId();
                //품사중에서 어떤 품사를 골랐는지
                int rb3 = ((RadioGroup) rGroup3.findViewById(R.id.RadioGrp3)).getCheckedRadioButtonId();


                //오늘의 학습 범위
                if(rb2 == R.id.radioButton4){
                    result = "오늘";
                    lastword = day * countword; //마지막으로 배울 단어
                    temp = num - lastword;
                }
                //품사별
                else if(rb2 == R.id.radioButton5){
                    if(rb3 == R.id.noun){
                        result = "명사";
                        lastword = dbHelper.getClassCount("명사"); //수어중에 명사인 갯수 구해오기
                        temp = num - lastword;}
                    else if(rb3 == R.id.verb){
                        result = "동사";
                        lastword = dbHelper.getClassCount("동사");
                        temp = num - lastword;}
                    else if(rb3 == R.id.adverse){
                        result = "형용사";
                        lastword = dbHelper.getClassCount("형용사");
                        temp = num - lastword;}
                }
                //장르별 아직 구현x
                else if(rb2 == R.id.radioButton6){

                }

                builder = new AlertDialog.Builder(QuizStart.this);
                // 제목셋팅
                builder.setTitle("*경고*");

                // AlertDialog 셋팅
                //퀴즈범위를 안골랐을때
                if(temp == 0){
                    builder.setMessage("퀴즈 범위를 고르지 않았습니다. 고르세요");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });
                }
                //버튼이 아니라 직접 입력했을 때 0입력하면
                else if(num == 0){
                    builder.setMessage("학습할 단어 수가 0일 수는 없습니다.\n 다시 학습할 단어를 고르세요");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });
                }
                else{
                    builder.setMessage("학습한 단어 수가 퀴즈 학습할 단어 수보다 " + temp+"개 적습니다\n그래도 퀴즈를 보시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    //hsv setting 페이지로 넘길 때 이전페이지가 뭐였는지에 대한 정보 보내기
                                    intent.putExtra("page", 3);
                                    //학습할 단어 수 넘기기
                                    intent.putExtra("num", num);
                                    //학습 범위 고르기
                                    intent.putExtra("range",result);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니요.\n다시 단어 수를 고를게요",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel(); }
                                    });
                }
                if(num > lastword || num == 0){
                    //메세지 창 띄우기
                    builder.create().show();
                }
                else{
                    final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //hsv setting 페이지로 넘길 때 이전페이지가 뭐였는지에 대한 정보 보내기
                    intent.putExtra("page", 3);
                    //학습할 단어 수 넘기기
                    intent.putExtra("num", num);
                    //학습 범위 고르기
                    intent.putExtra("range",result);
                    startActivity(intent);
                }

            }
        });
    }
}
