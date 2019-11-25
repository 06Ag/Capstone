package com.example.singlanguage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
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

//카테고리학습 메뉴의 카메라 학습 페이지
public class CategoryLearning_study_camera extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2{

    TextView tv_imageNum;

    int lh, ls, lv, uh, us, uv; //hsv값 가져오기
    //수어 list
    final String[][] list = new String[][] {{"애"},{"비읍"},{"지폐"},{"끓이다"},{"치읓"},{"다지다"},{"춥다"},{"기둥"},{"컴퓨터"},{"고객"},{"데이트"},{"디귿"},{"상의"},{"만두"},{"에"},{"8"},{"어"},{"으"},{"5"},{"4"},{"열매"},{"기역"}, {"건빵"}, {"히읗"}, {"집"},{"이"},{"가렵다"},{"지읒"},{"죽"},{"키읔"},{"사랑"},{"남자"},{"고기"},{"미음"},{"가장"},{"산"},{"버섯"},{"니은", "6"},{"9"},{"북쪽"},{"오"},{"외"},{"1", "아"},{"피읖"},{"가루"},{"발표하다"}, {"읽다"},{"녹음기"},{"관계"},{"갈비"},{"떡"},{"리을"},{"도로"},{"학교"},{"깨"},{"7"},{"시옷"},{"노예","심부름"},{"남쪽"},{"서다"},{"선생님"},{"10", "이응"},{"3"},{"티읕"},{"2"},{"우"},{"의"},{"산골"},{"위"},{"여자"},{"야"},{"얘"},{"예"},{"여"},{"요"},{"유"},{"0"}};
    //정확한 동작을 하는지에 대한 토스트 띄우기 카운트
    CountDownTimer toastCount;
    int correct_wrong=0;  //correct: 1, wrong: -1
    int timer=0;    //countdowntimer 작동시 1, 아닐 경우 0
    String name;    //intent로 넘어온 수어 명칭 저장

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult, matHSV;

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

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("카메라 학습") ;

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_category_learning_camera);

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());

        Button stop = (Button) findViewById(R.id.bt_stop);
        final ImageView imgview = (ImageView) findViewById(R.id.imageView); //수화 이미지
        tv_imageNum = findViewById(R.id.tv_image);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(cameraType); // front-camera(1),  back-camera(0)

        Intent intent = getIntent();    //넘겨 받은 값
        lh = intent.getExtras().getInt("LH");
        ls = intent.getExtras().getInt("LS");
        lv = intent.getExtras().getInt("LV");
        uh = intent.getExtras().getInt("UH");
        us = intent.getExtras().getInt("US");
        uv = intent.getExtras().getInt("UV");
        int id = getRawResIdByName(dbHelper.getResult_img(intent.getExtras().getString("name")));   //이미지 이름으로 아이디 받기
        Uri uri_img = Uri.parse("android.resource://" + getPackageName() + "/" + id);   //uri에 이미지 주소 저장
        imgview.setImageURI(uri_img);   //이미지뷰에 이미지 출력

        name = intent.getExtras().getString("name");
        tv_imageNum.setText("동작을 따라해보세요.\n\n" + name);

        //임시로 stop시 전페이지로
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();    //전페이지
            }
        });

        //정확한 동작을 했는지 알려줄 toast문구 카운트 - 잘했을 경우/ 못했을 경우 모두 4초씩 유지할 때 문구 띄우기
        toastCount = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("countdown", millisUntilFinished/1000L+"  "+correct_wrong);
            }

            @Override
            public void onFinish() {
                Toast toast;
                if(correct_wrong == 1) {
                    toast = Toast.makeText(CategoryLearning_study_camera.this, "알맞은 동작입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(correct_wrong == -1){
                    toast = Toast.makeText(CategoryLearning_study_camera.this, "알맞지 않은 동작입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                timer = 0;  //타이머 끝났으므로 0으로 값 바꿈 - 시작시 다시 1로 바뀜
            }
        };
    }

    //onclick속성이 지정된 view를 클릭시 자동으로 호출됨
    public void mOnClick(View v) {
        Intent intent = getIntent();
        //int num = intent.getExtras().getInt("num");    //학습할 단어 수 받기
    }

    //raw폴더의 이미지 아이디 불러오기
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    //퀴즈 단어와 손모양이 일치하는지 확인하여 맞았는지 유무를 알 수 있음 / 틀렸을 경우 빨간화면테두리, 맞으면 초록화면테두리
    public void recognise() {
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
        //인풋에 해당하는 결과 보기
        tflite.run(input, output);

        Log.d("predict", Arrays.toString(output[0]));

        Point point = new Point(245, 245);
        Scalar green = new Scalar(0, 255, 0, 3);
        Scalar red = new Scalar(255, 0, 0, 3);
        //현재 사용자가 하는 동작과 현재 단어가 일치하는지 확인
        for(int i=0; i<output[0].length; i++) {
            if (Math.round(output[0][i]) == 1) {
                for(int j=0; j<list[i].length; j++) {   //수화 동작 같은 단어들 모두를 비교
                    //현재 단어와 동작 일치할 경우 - 초록색
                    if (name.equals(list[i][j])) {
                        Imgproc.rectangle(matResult, new Point(15, 10), new Point(matResult.cols() - 15, matResult.rows() - 10), green, 30);
                        if (timer == 1 && correct_wrong == -1) {  //틀렸을 경우의 타이머가 작동 중이면 중단 후 다시 타이머 시작
                            toastCount.cancel();
                            correct_wrong = 1;
                            toastCount.start();
                        } else if (timer == 0) {    //현재 타이머 작동중이 아니면 타이머 시작
                            timer = 1;
                            correct_wrong = 1;
                            toastCount.start();
                        }
                        break;
                    }
                    //현재 단어와 동작 불일치 경우 - 빨간색
                    else {
                        Imgproc.rectangle(matResult, new Point(15, 10), new Point(matResult.cols() - 15, matResult.rows() - 10), red, 30);
                        if (timer == 1 && correct_wrong == 1) {  //맞았을 경우의 타이머가 작동 중이면 중단 후 다시 타이머 시작
                            toastCount.cancel();
                            correct_wrong = -1;
                            toastCount.start();
                        } else if (timer == 0) {    //현재 타이머 작동중이 아니면 타이머 시작
                            timer = 1;
                            correct_wrong = -1;
                            toastCount.start();
                        }
                    }
                }
                break;
            }
        }
    }

    //모델 파일 인터프리터를 생성하는 함수
    private Interpreter getTfliteInterpreter(String modelPath) {
        try{
            return new Interpreter(loadModelFile(CategoryLearning_study_camera.this, modelPath));
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
    public void onBackPressed() {
        toastCount.cancel();
        super.onBackPressed();
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

        Core.flip(matInput,matResult, 1);    //수평-양수, 수직-0, 모두-음수
        //손모양 인식하여 맞는지 유무 검사
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

        AlertDialog.Builder builder = new AlertDialog.Builder( CategoryLearning_study_camera.this);
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
