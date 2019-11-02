package com.example.singlanguage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "opencv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml화면 보임
        setContentView(R.layout.activity_main);
        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        //excel데이터 읽어와서 db에 넣을려는 코드(일단 컬럼 3개만 넣어봄)
        InputStream is = getResources().openRawResource(R.raw.capdb);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line="";
        try {
            while((line = reader.readLine()) != null){
                Log.d("My Activity","Line: "+ line);
                //Split by ','
                String[] tokens = line.split(",");
                //Read the data
                String  name = tokens[0];
                String  cl1   = tokens[1];
                String  cl2 = "";
                if(tokens.length>=3 && tokens[2].length() >0){
                    cl2 = tokens[2]; //아직 class2구분안해놓은게 있어서..
                }
                dbHelper.insert(name,cl1,cl2);
                //은진이 데이터? 여기다가 읽어와서 넣는거를 만들어도 될듯
            }
        }catch(IOException e){
            Log.wtf( "MyActivity","Error reading data file on line" + line, e);
            e.printStackTrace();;
        }

        Button b = (Button)findViewById(R.id.Learning);

        //카메라 permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자

                        learning.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        Button bt_trans= (Button)findViewById(R.id.Translation);
        bt_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), HsvSetting.class);
                startActivity(intent);
            }
        });

        Button bt_db= (Button)findViewById(R.id.DBtesting);
        bt_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), DBtest.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // permission
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};


    private boolean hasPermissions(String[] permissions) {
        int result;
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
