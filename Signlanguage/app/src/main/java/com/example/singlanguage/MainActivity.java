package com.example.singlanguage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "opencv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml화면 보임
        setContentView(R.layout.activity_main);
        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        //카메라 permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        Button b = (Button)findViewById(R.id.Learning);
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
