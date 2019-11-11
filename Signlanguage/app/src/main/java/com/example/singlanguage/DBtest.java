package com.example.singlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import java.io.IOException;

//DB 출력하는 실험용 java code
public class DBtest extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());

        // 테이블에 있는 모든 데이터 출력
        final TextView result = (TextView) findViewById(R.id.result);
        final ImageView imgview = (ImageView) findViewById(R.id.imageView);

        // DB에 있는 데이터 조회
        Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(dbHelper.getResult());

                int id = getRawResIdByName(dbHelper.getResult_img("1"));   //이미지 이름으로 아이디 받기
                Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
                imgview.setImageURI(uri_img);   //이미지뷰에 이미지 출력

                Toast.makeText(getApplicationContext(), "데이터 조회", Toast.LENGTH_SHORT).show();

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
