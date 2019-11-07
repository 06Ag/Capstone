package com.example.singlanguage;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static com.example.singlanguage.MainActivity.PERMISSIONS_REQUEST_CODE;

public class TodayStart extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2{

    Button bt_stop;
    Button bt_previous;
    Button bt_next;
    TextView tv_imageNum; // 수어 이름 표시하는 textview

    int pos = 1; //학습한 단어 수
    int countword =0; //학습할 단어 수
    int firstword =0; //처음으로 학습할 단어의 _id
    int lastword =0; //마지막으로 학습할 단어의 _id
    int day = 0; //학습 일 수
    int dbcount =0; //db안에 있는 수어 수



    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;

    private CameraBridgeViewBase mOpenCvCameraView;
    private int cameraType = 1; //초기 전면카메라


 //   public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);


    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_start);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        bt_stop = findViewById(R.id.bt_stop);
        bt_next = findViewById(R.id.bt_next);
        bt_previous = findViewById(R.id.bt_previous);
        tv_imageNum = findViewById(R.id.tv_image);

        //learning.java로부터 며칠째 학습인지, 몇개를 학습해야하는지 받아옴
        Intent intent = getIntent();
        countword = intent.getIntExtra("countword", 0);
        day = intent.getIntExtra("day",0);


        dbcount = dbHelper.getCount();//db안에 있는 수어 수
        System.out.println("db안에 있는 단어수 : "+ dbcount);
        firstword = (day - 1)*countword + 1; //처음으로 배울 단어
        lastword = day * countword; //마지막으로 배울 단어
        System.out.println("첫번째로 배울 단어 index- "+firstword);
        System.out.println("마지막으로 배울 단어 index- "+lastword);

        //db에서 더이상 배울 단어가 없을 경우
        if(firstword > dbcount){
            Toast.makeText(getApplicationContext(), "더이상 배울 단어가 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), TodayLearning.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
        }

        //첫번째로 배울 단어
        String name = dbHelper.getName(firstword);
        tv_imageNum.setText("수화 #" + Integer.toString(pos) + "\n"+name);
        dbHelper.setLearn(firstword); //학습 여부 참으로 바꿔놓기
        dbToday.updatepos(countword,pos); // 오늘의 학습 단어중에서 배운 단어 수 update

        //stop시 일일학습페이지로 넘어가며 현 학습한 단어 개수(임의로 pos) 반환
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        bt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos > 1) {
                    pos = pos - 1;
                    int temp = 0; //db의 _id를 구하기 위한 임시변수
                    temp = (day - 1 )* countword + pos;
                    String name = dbHelper.getName(temp); // 여기서는 dbHelper.setLearn()함수 호출할 필요없음
                    tv_imageNum.setText("수화 #" + Integer.toString(pos) + "\n"+name);
                }
                else{
                    Toast toast = Toast.makeText(TodayStart.this, "처음 단어 입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos < countword) {
                    pos = pos + 1;
                    int temp = 0; //db의 _id를 구하기 위한 임시변수
                    temp = (day - 1)* countword + pos;

                    //다음 단어가 db에 없을경우
                    if(temp > dbcount){
                        Toast toast = Toast.makeText(TodayStart.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        final Intent intent = new Intent(getApplicationContext(), TodayLearning.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                    }

                    //새로운 단어를 배움
                    String name = dbHelper.getName(temp);
                    tv_imageNum.setText("수화 #" + Integer.toString(pos) + "\n"+name);
                    dbHelper.setLearn(temp); //학습 여부 참으로 바꿔놓기
                    dbToday.updatepos(countword,pos); //학습 단어 수 update
                }
                else{
                    Toast toast = Toast.makeText(TodayStart.this, "마지막 단어 입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });










        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //세로모드고정
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        setContentView(R.layout.activity_today_start);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(cameraType); // front-camera(1),  back-camera(0)

    }




    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();

        if ( matResult == null )

    //        matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

     //   ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());
        //  Core.transpose(matResult,matResult);
        Core.flip(matInput,matInput, 1);    //수평-양수, 수직-0, 모두-음수
        return matInput;
    }


    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }


    //여기서부턴 퍼미션 관련 메소드
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;


    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean havePermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if (havePermission) {
            onCameraPermissionGranted();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted();
        }else{
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( TodayStart.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }


}
