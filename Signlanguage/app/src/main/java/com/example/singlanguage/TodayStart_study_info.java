package com.example.singlanguage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TodayStart_study_info extends AppCompatActivity {

    Button bt_stop;
    Button bt_previous;
    Button bt_next;
    Button bt_camera;
    TextView tv_imageNum; // 수어 이름 표시하는 textview
    TextView tv_explain;    // 수어 설명
    ImageView iv_img;   //수어 이미지

    int pos = 0; //학습한 단어 수
    int dbpos = 0; //db에 저장할 pos값
    int countword = 0; //학습할 단어 수
    int firstword = 0; //처음으로 학습할 단어의 _id
    int lastword = 0; //마지막으로 학습할 단어의 _id
    int day = 0; //학습 일 수
    int dbcount = 0; //db안에 있는 수어 수
    int chlearn = 0; //countword를 바꾸고나서 어디까지 단어를 학습했는지 알기위한 _id

    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        Toast toast;
        toast = Toast.makeText(this, "뒤로 가기 버튼을 사용할 수 없습니다.\n학습 중단을 눌러주세요", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_start_study_info);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());


        bt_stop = findViewById(R.id.bt_stop);
        bt_camera = findViewById(R.id.bt_camera);
        bt_next = findViewById(R.id.bt_next);
        bt_previous = findViewById(R.id.bt_previous);
        tv_imageNum = findViewById(R.id.tv_image);
        tv_explain = findViewById(R.id.tv_explain);
        iv_img = findViewById(R.id.iv_img);

        pos = 1; //학습한 단어 수
        dbpos = dbToday.getPos();
        countword = dbToday.getCount(); //하루 당 학습해야하는 단어 가져옴
        day = dbToday.getDay(); //며칠째인지 가져옴

        dbcount = dbHelper.getCount();//db안에 있는 수어 수

        firstword = dbToday.getFirstword(); //처음으로 배울 단어
        lastword = dbToday.getFirstword() + countword - 1; //마지막으로 배울 단어

        //db에서 더이상 배울 단어가 없을 경우
        if (firstword > dbcount) {
            Toast.makeText(getApplicationContext(), "더이상 배울 단어가 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), TodayLearning.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
        }

        //첫번째로 배울 단어
        final String name = dbHelper.getName(firstword);
        tv_imageNum.setText("#" + Integer.toString(pos) + "      " + name);
        int id = getRawResIdByName(dbHelper.getResult_img(name));   //이미지 이름으로 아이디 받기
        String des = dbHelper.getResult_des(name);   //수화 설명
        Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
        iv_img.setImageURI(uri_img);   //이미지뷰에 이미지 출력
        dbHelper.setLearn(firstword); //학습 여부 참으로 바꿔놓기
        if (dbpos < pos) {
            dbpos += 1;
            dbToday.updatepos(day, pos); // 오늘의 학습 단어중에서 배운 단어 수 update
        }


        //stop시 일일학습페이지로 넘어가며 현 학습한 단어 개수(임의로 pos) 반환
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //이전 버튼
        bt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos > 1) {
                    pos = pos - 1;
                    int temp = 0; //db의 _id를 구하기 위한 임시변수
                    temp = firstword + pos - 1;
                    String name = dbHelper.getName(temp); // 여기서는 dbHelper.setLearn()함수 호출할 필요없음
                    tv_imageNum.setText("#" + Integer.toString(pos) + "     " + name);
                    int id = getRawResIdByName(dbHelper.getResult_img(name));   //이미지 이름으로 아이디 받기
                    String des = dbHelper.getResult_des(name);   //수화 설명
                    Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
                    iv_img.setImageURI(uri_img);   //이미지뷰에 이미지 출력
                } else {
                    Toast toast = Toast.makeText(TodayStart_study_info.this, "처음 단어 입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //다음 버튼
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < countword) {
                    pos = pos + 1;

                    int temp = 0; //db의 _id를 구하기 위한 임시변수
                    temp = firstword + pos - 1;

                    //다음 단어가 db에 없을경우
                    if (temp > dbcount) {
                        Toast toast = Toast.makeText(TodayStart_study_info.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                    }

                    //새로운 단어를 배움
                    String name = dbHelper.getName(temp);
                    tv_imageNum.setText("#" + Integer.toString(pos) + "     " + name);
                    int id = getRawResIdByName(dbHelper.getResult_img(name));   //이미지 이름으로 아이디 받기
                    String des = dbHelper.getResult_des(name);   //수화 설명
                    Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
                    iv_img.setImageURI(uri_img);   //이미지뷰에 이미지 출력
                    dbHelper.setLearn(temp); //학습 여부 참으로 바꿔놓기
                    if (dbpos < pos) {
                        dbpos += 1;
                        dbToday.updatepos(day, pos); // 오늘의 학습 단어중에서 배운 단어 수 update
                    }

                } else {
                    Toast toast = Toast.makeText(TodayStart_study_info.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    //마지막 단어 학습 시 화면에 나타날 메세지 창 설정
                    AlertDialog.Builder builder = new AlertDialog.Builder(TodayStart_study_info.this);
                    // 제목셋팅
                    builder.setTitle("퀴즈 시작");
                    // AlertDialog 셋팅
                    builder.setMessage("오늘의 학습을 모두 수행하였습니다.\n퀴즈를 보시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    //퀴즈페이지로 이동 - quiz class
                                    Intent intent = new Intent(getApplicationContext(), Quiz.class);
                                    intent.putExtra("num", countword);
                                    intent.putExtra("range", "오늘학습");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();
                                        }
                                    });
                    //메세지 창 띄우기
                    builder.create().show();
                }
            }
        });

        //동작 배우기 버튼 - 카메라 세팅 패이지로 넘어감
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                //hsv setting 페이지로 넘길 때 이전페이지가 뭐였는지에 대한 정보 보내기
                int temp = 0; //db의 _id를 구하기 위한 임시변수
                temp = firstword + pos - 1;
                String name = dbHelper.getName(temp);
                intent.putExtra("page", 1);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    //raw폴더의 이미지 아이디 불러오기
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
