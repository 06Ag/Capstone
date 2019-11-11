package com.example.singlanguage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;

//tensorflow

public class Translation extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    int lh, ls, lv, uh, us, uv;
    TextView tv_transResult;
    TextView tv_hsvValue;

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;
    private Mat matHSV;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //화면 켜진 상태 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_translation);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(cameraType); // front-camera(1),  back-camera(0)

        tv_transResult = (TextView) findViewById(R.id.transresult);
        tv_hsvValue = (TextView) findViewById(R.id.tv_hsv_Value);

        Intent intent = getIntent();
        lh = intent.getExtras().getInt("LH");
        ls = intent.getExtras().getInt("LS");
        lv = intent.getExtras().getInt("LV");
        uh = intent.getExtras().getInt("UH");
        us = intent.getExtras().getInt("US");
        uv = intent.getExtras().getInt("UV");

        tv_hsvValue.setText("HSV 값\nLH - " + lh + "  LS - " + ls + "  LV - " + lv + "\nUH - "+uh+"   US - " + us + "   UV - " + uv);
    }
    //딥러닝
    public void recognise() {
        String img_name = "1.png";
        //   Mat save_img;
        Size sz = new Size(64, 64);

        if (matHSV == null)
            matHSV = new Mat(matResult.rows(), matResult.cols(), matResult.type());

        //Convert to HSV
        Imgproc.cvtColor(matResult, matHSV, Imgproc.COLOR_RGB2HSV);
        Scalar lower = new Scalar(lh, ls, lv);
        Scalar upper = new Scalar(uh, us, uv);
        Core.inRange(matHSV, lower, upper, matHSV);
        Imgproc.resize(matHSV, matHSV, sz);
        //mat to bitmap
        Bitmap bmp = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matHSV, bmp);

        //input: 텐서플로 모델의 placeholder에 전달할 데이터
        //output: 텐서플로 모델로부터 결과를 넘겨받을 배열 덮어쓰기 때문에 초기값은 의미가 없다.
        float[][][][] input = new float[1][64][64][3];
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                int pixel = bmp.getPixel(x, y);
                // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                // model. For example, some models might require values to be normalized
                // to the range [0.0, 1.0] instead.
                input[0][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                input[0][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                input[0][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
            }
        }
        //인터프리터 생성
        Interpreter tflite = getTfliteInterpreter("Trained_model.tflite");

        float[][] output = new float[1][60];
        tflite.run(input, output);

        Log.d("predict", Arrays.toString(output[0]));

        String[] list = {"ㅐ","비읍","치읓","춥다", "컴퓨터", "고객","디귿","상의하다","ㅔ","8","ㅓ","ㅡ","5","4","과일","기역", "건빵", "히읗", "집","ㅣ","가렵다","지읒","키읔", "남자", "고기", "약", "미음","니은","9","북쪽","ㅗ","ㅚ","1","피읖","가루","발표", "읽다", "갈비", "떡", "리을","학교", "7","시옷","남쪽", "선생님","10","3","티읕","2","ㅜ","ㅢ","ㅟ", "여자","ㅑ","ㅒ","ㅖ","ㅕ","ㅛ","ㅠ","0"};
        for(int i=0; i<60; i++) {
            if (Math.round(output[0][i]) == 1)
                tv_transResult.setText(list[i]);
        }
    }

    //모델 파일 인터프리터를 생성하는 함수
    private Interpreter getTfliteInterpreter(String modelPath) {
        try{
            return new Interpreter(loadModelFile(Translation.this, modelPath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
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
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        /*
        //Convert to HSV
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2HSV);
        Scalar lower = new Scalar(lh, ls, lv);
        Scalar upper = new Scalar(uh, us, uv);
        Core.inRange(matResult, lower, upper, matResult);
*/
        Core.flip(matInput,matResult, 1);    //수평-양수, 수직-0, 모두-음수
        //번역기능
        recognise();
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

        AlertDialog.Builder builder = new AlertDialog.Builder( Translation.this);
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
