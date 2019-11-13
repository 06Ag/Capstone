package com.example.singlanguage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//카테고리학습 메뉴의 정보학습 페이지
public class CategoryLearning_study_info extends AppCompatActivity{

    TextView tv_imageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_learning_info);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());

        Button stop = (Button) findViewById(R.id.bt_stop);  //학습 중단
        Button next = (Button) findViewById(R.id.bt_next);  //카메라 학습으로 넘어가기 버튼
        final ImageView imgview = (ImageView) findViewById(R.id.imageView); //수화 이미지
        final TextView explainview = (TextView)findViewById(R.id.explain);  //수화 설명
        tv_imageNum = findViewById(R.id.tv_image);  //수화 이름

        Intent intent = getIntent();    //넘겨 받은 값
        int id = getRawResIdByName(dbHelper.getResult_img(intent.getExtras().getString("name")));   //이미지 이름으로 아이디 받기
        String des = dbHelper.getResult_des(intent.getExtras().getString("name"));   //수화 설명
        Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
        imgview.setImageURI(uri_img);   //이미지뷰에 이미지 출력

        final String name = intent.getExtras().getString("name");
        tv_imageNum.setText(name);
        explainview.setText('\n' + des);


        //stop시 전페이지로
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();    //전페이지
            }
        });
        //카메라 학습 페이지로 이동
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                intent.putExtra("name", name);
                //HSVSetting 페이지로 보낼 때 현재 페이지에 대한 정보 보내기 (여기 페이지는 2번이다)
                intent.putExtra("page", 2);
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

}
