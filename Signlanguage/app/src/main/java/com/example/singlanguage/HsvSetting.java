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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class HsvSetting extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2{

    int page =0;    //이전화면 정보가져오기 - finish button 클릭시 이동할 화면 설정시 사용
                    //일일학습 - 1, 카테고리 - 2, 퀴즈 - 3, 번역 - 4
    int lh, ls, lv, uh, us, uv;
    SeekBar sb_LH, sb_LS, sb_LV, sb_UH, sb_US, sb_UV;
    TextView tv_LH, tv_LS, tv_LV, tv_UH, tv_US, tv_UV;

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;

    private CameraBridgeViewBase mOpenCvCameraView;
    private int cameraType = 1; //초기 전면카메라

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_hsv_setting);
        //카메라 설정
        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(cameraType); // front-camera(1),  back-camera(0)

        //SeekBar
        sb_LH = (SeekBar)findViewById(R.id.sb_LH);
        sb_LS = (SeekBar)findViewById(R.id.sb_LS);
        sb_LV = (SeekBar)findViewById(R.id.sb_LV);
        sb_UH = (SeekBar)findViewById(R.id.sb_UH);
        sb_US = (SeekBar)findViewById(R.id.sb_US);
        sb_UV = (SeekBar)findViewById(R.id.sb_UV);
        tv_LH=(TextView)findViewById(R.id.tv_LHval);
        tv_LS=(TextView)findViewById(R.id.tv_LSval);
        tv_LV=(TextView)findViewById(R.id.tv_LVval);
        tv_UH=(TextView)findViewById(R.id.tv_UHval);
        tv_US=(TextView)findViewById(R.id.tv_USval);
        tv_UV=(TextView)findViewById(R.id.tv_UVval);
        //seekBar에서 값 받아오기
        lh = sb_LH.getProgress();
        ls = sb_LS.getProgress();
        lv = sb_LV.getProgress();
        uh = sb_UH.getProgress();
        us = sb_US.getProgress();
        uv = sb_UV.getProgress();
        //각 textview에 text 설정
        tv_LH.setText(Integer.toString(lh));
        tv_LS.setText(Integer.toString(ls));
        tv_LV.setText(Integer.toString(lv));
        tv_UH.setText(Integer.toString(uh));
        tv_US.setText(Integer.toString(us));
        tv_UV.setText(Integer.toString(uv));

        sb_LH.setOnSeekBarChangeListener(HSVcolor);
        sb_LS.setOnSeekBarChangeListener(HSVcolor);
        sb_LV.setOnSeekBarChangeListener(HSVcolor);
        sb_UH.setOnSeekBarChangeListener(HSVcolor);
        sb_US.setOnSeekBarChangeListener(HSVcolor);
        sb_UV.setOnSeekBarChangeListener(HSVcolor);

        Button bt_st= (Button)findViewById(R.id.finish);
        final Intent intent = getIntent();
        //이전페이지에서 intent값 받아옴
        int intent_value = intent.getIntExtra("page", -1);
        if(intent_value != -1)      //intent로 넘겨받은 값이 없을 경우 기존에 page값 유지
            page = intent_value;

        bt_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent_page;
                if(page == 1)
                    intent_page = new Intent(getApplicationContext(), TodayStart.class);
                else if(page == 2)
                    intent_page = new Intent(getApplicationContext(), TodayStart.class);
                else if(page == 3) {
                    intent_page = new Intent(getApplicationContext(), Quiz.class);
                    int num = intent.getIntExtra("num", -1);
                    String range = intent.getStringExtra("range");
                    intent_page.putExtra("num", num);
                    intent_page.putExtra("range",range);
                }
                else if(page == 4)
                    intent_page = new Intent(getApplicationContext(), Translation.class);
                else
                    intent_page = new Intent();
                //다음 화면으로 이동시 hsv 설정 값 전달
                intent_page.putExtra("LH", lh);
                intent_page.putExtra("LS", ls);
                intent_page.putExtra("LV", lv);
                intent_page.putExtra("UH", uh);
                intent_page.putExtra("US", us);
                intent_page.putExtra("UV", uv);
                startActivity(intent_page);
            }
        });
    }

    SeekBar.OnSeekBarChangeListener HSVcolor = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //움직임이 느껴지면 여기서 반응
            //어느 seekBar / 변경된 수치값 / 초기 false, 사용자가 움직이면 true
            if (seekBar == sb_LH) {
                lh = seekBar.getProgress();
                tv_LH.setText(Integer.toString(lh));
            }
            else if(seekBar == sb_LS){
                ls = seekBar.getProgress();
                tv_LS.setText(Integer.toString(ls));
            }
            else if(seekBar == sb_LV){
                lv = seekBar.getProgress();
                tv_LV.setText(Integer.toString(lv));
            }
            else if(seekBar == sb_UH){
                uh = seekBar.getProgress();
                tv_UH.setText(Integer.toString(uh));
            }
            else if(seekBar == sb_US){
                us = seekBar.getProgress();
                tv_US.setText(Integer.toString(us));
            }
            else if(seekBar == sb_UV){
                uv = seekBar.getProgress();
                tv_UV.setText(Integer.toString(uv));
            }
            //카메라 조정은 onCameraFrame() 함수에서 자동으로 됨
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //seekBar를 이동하기 위하여 선택하였을 때
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //SeekBar를 이동하다가 멈췄을 때
        }
    };

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
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        //Convert to HSV
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2HSV);
        Scalar lower = new Scalar(lh, ls, lv);
        Scalar upper = new Scalar(uh, us, uv);
        Core.inRange(matResult, lower, upper, matResult);

        Core.flip(matResult,matResult, 1);    //수평-양수, 수직-0, 모두-음수
        return matResult;
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

        AlertDialog.Builder builder = new AlertDialog.Builder( HsvSetting.this);
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
