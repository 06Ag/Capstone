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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.Random;

import static android.Manifest.permission.CAMERA;

public class Quiz extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2{

    Button bt_next;
    TextView tv_imageNum, tv_correct, tv_progress;
    ProgressBar pb_count;
    int num, pos = 1;    //학습할 단어 수, 수화 순서
    int lh, ls, lv, uh, us, uv;

    final String[] list = {"애","비읍","치읓","춥다", "컴퓨터", "손님","디귿","상의하다","에","8","어","으","5","4","열매","기역", "건빵", "히읗", "집","이","간지럽다","지읒","키읔", "남자", "고기", "약", "미음","니은","9","북쪽","오","외","1","피읖","가루","발표하다", "읽다", "갈비", "떡", "리을","학교", "7","시옷","남쪽", "선생님","10","3","티읕","2","우","의","위", "여자","야","얘","예","여","요","유","0"};
    String[] quiz_list;
    String[] quiz_result;   //X, 0, PASS 로 뜨게 된다.

    int lastword =0; //마지막으로 학습할 단어의 _id
    int day = 0; //학습 일 수
    int countword =0; //학습할 단어 수
    int dbcount =0; //db안에 있는 수어 수
    //타이머 설정
    CountDownTimer quizStart;   //퀴즈 단어 보여주고 생각할 시간주기 4초
    CountDownTimer quizCount;   //맞추는 제한시간 10초
    CountDownTimer quizCorrect; //맞는동작 유지시간 3초
    CountDownTimer quizFinish;  //한 단어에 대한 퀴즈 맞춘 후 다음 단어로 자동 넘어가기 전 타임 2초
    int correctSet = 0; //동작 유지시간 작동되고있는지 유무 / 1 - start , 0 - stop , -1 - 퀴즈 맞추기 불가
    int countSet = 0;   //제한시간 count 시에만 퀴즈 맞추기 가능/ 0 - 아직 시작 안함 , 1 - 시작 , 2 - 끝
    int correctN=0; //퀴즈 맞은 개수

    private AlertDialog.Builder builder;    //퀴즈 중단시 보이는 메세지 창

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
    public void getquiz( String jan){
        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        int a[] = new int[num];
        Random r = new Random(); //객체생성
        countword = dbHelper.getClassCount(jan);
        dbcount = dbHelper.getCount(); //db안에 저장된 수어 갯수 구해오기
        for (int i = 0; i < num; i++)    //숫자 n개를 뽑기위한 for문
        {
            //만약 학습하려는 단어 수보다 명사 단어 총 개수가 적은경우
            if(i+1 > countword){
                num = i; //퀴즈갯수바꾸기
                break;
            }
            a[i] = r.nextInt(dbcount) + 1; //db에서 랜덤으로 하나를 뽑아 a[0]~a[n-1]에 저장
            if(dbHelper.getClassName(jan,a[i]) == ""){
                i--;
            }
            for (int j = 0; j < i; j++) //중복제거를 위한 for문
            {
                if (a[i] == a[j]) {
                    i--;
                }else if(dbHelper.getClassName(jan,a[i]) == ""){
                    i--;
                }
            }

        }
        quiz_list = new String[num]; //db에서 가져올 수화내용저장
        for (int i = 0; i < num; i++)   //뽑은 수화를 list에 넣어줌
        {
            quiz_list[i] = dbHelper.getName(a[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_quiz);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("퀴즈") ;

        Button stop = (Button) findViewById(R.id.bt_stop);
        tv_imageNum = findViewById(R.id.tv_image);
        tv_correct = findViewById(R.id.tv_correct);
        tv_progress = findViewById(R.id.tv_progress);
        pb_count = findViewById(R.id.pb_progress);
        bt_next = (Button) findViewById(R.id.bt_next);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(cameraType); // front-camera(1),  back-camera(0)
        //HSV setting값 받아오기 - 카메라에 적용 / onCameraFrame()에서 적용됨
        Intent intent = getIntent();
        lh = intent.getExtras().getInt("LH");
        ls = intent.getExtras().getInt("LS");
        lv = intent.getExtras().getInt("LV");
        uh = intent.getExtras().getInt("UH");
        us = intent.getExtras().getInt("US");
        uv = intent.getExtras().getInt("UV");
        int intent_value = intent.getExtras().getInt("num", -1);    //학습할 단어 수 받기
        String range = intent.getExtras().getString("range"); //학습 범위 받기
        if(intent_value != -1)
            num = intent_value;

        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext()); //db가져오기
        final DBToday dbToday = DBToday.getInstance(getApplicationContext());

        //퀴즈 개수의 맞게 quiz_result 배열 크기 설정 및 초기 'X'로 값 지정
        quiz_result = new String[num];
        for(int i=0; i<num; i++)
            quiz_result[i] = "X";
        //오늘의 학습 범위 - 당일만
        if(range.equals("오늘학습")){
            countword = dbToday.getCount(); //하루 당 학습해야하는 단어 가져옴
            day = dbToday.getDay(); //며칠째인지 가져옴
            lastword = dbToday.getFirstword() + countword - 1; //마지막으로 배울 단어

            quiz_list = new String[num];
            for(int i=dbToday.getFirstword(), j=0; i<=lastword; i++, j++){   // i = DB에서 값 불러올때 사용, j = LIST_MENU 배열에 값 넣을 때 사용
                quiz_list[j] = dbHelper.getName(i);
            }
        }
        //오늘까지 총 학습 범위
        else if(range.equals("오늘")) {
            int a[] = new int[num];
            Random r = new Random(); //객체생성
            lastword = dbHelper.getchlearn(); //마지막까지 배운단어
            for (int i = 0; i < num; i++)    //숫자 num개를 뽑기위한 for문
            {
                //만약 학습하려는 단어 수보다 지금까지 오늘의 학습을 통해 학습한 총 개수가 적은경우
                if(i+1 > lastword){
                    num = i; //퀴즈갯수바꾸기
                    break;
                }
                a[i] = r.nextInt(lastword) + 1; //1부터 지금까지 배운 단어id중에 랜덤으로 하나를 뽑아 a[0]~a[num-1]에 저장
                for (int j = 0; j < i; j++) //중복제거를 위한 for문
                {
                    if (a[i] == a[j]) {
                        i--;
                    }
                }
            }
            quiz_list = new String[num]; //db에서 가져올 수화내용저장
            for (int i = 0; i < num; i++)   //뽑은 수화를 list에 넣어줌
            {
                quiz_list[i] = dbHelper.getName(a[i]);
            }
        }
        else if(range.equals("자음")){ //장르중에 자음일때
           getquiz("자음");
        }
        else if(range.equals("모음")){ //장르중에 모음일때
           getquiz("모음");
        }
        else if(range.equals("숫자")) { //장르중에 숫자일때
           getquiz("숫자");
        }
        else if(range.equals("사물")) { //장르중에 사물일때
            getquiz("사물");
        }
        else if(range.equals("사람")) { //장르중에 사람일때
            getquiz("사람");
        }
        else if(range.equals("음식")) { //장르중에 음식일때
            getquiz("음식");
        }
        else if(range.equals("방향")) { //장르중에 방향일때
            getquiz("방향");
        }
        else if(range.equals("기타")) { //장르중에 기타일때
            getquiz("기타");
        }


        //맨처음 퀴즈문제
        tv_imageNum.setText("#" + Integer.toString(pos) + "      " + quiz_list[pos - 1]);

        //퀴즈 종료시 화면에 나타날 메세지 창 설정
        builder = new AlertDialog.Builder(Quiz.this);
        // 제목셋팅
        builder.setTitle("퀴즈 중단");
        // AlertDialog 셋팅
        builder.setMessage("퀴즈를 중단하시겠습니까?");
        builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                //결과 보여주는 창으로 이동 - quizResult class
                                Intent intent = new Intent(getApplicationContext(), QuizResult.class);
                                intent.putExtra("list_result", quiz_result);
                                intent.putExtra("list", quiz_list);
                                intent.putExtra("Total", num);
                                intent.putExtra("Correct", correctN);
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

        //stop시 결과 창으로 이동
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();    //전페이지
                //메세지 창 띄우기
                builder.create().show();
            }
        });
        //PASS 버튼 누를 시 리스너
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //모든 타이머를 중지시킨다.
                quizStart.cancel();
                quizCorrect.cancel();
                quizCount.cancel();
                countSet = correctSet = 0;
                pb_count.setProgress(100);
                tv_progress.setText("10");
                //다음 버튼 - 타이머 설정
                if (pos < num) {
                    if (correctSet != -1) {   // WRONG/CORRECT 결과 나온 뒤 PASS버튼 누를 경우 어떤기능도 수행안함
                        quiz_result[pos - 1] = "PASS";    //pass버튼 누르면 result배열에 PASS로 표시되도록하기
                        pos = pos + 1;
                        tv_imageNum.setText("#" + Integer.toString(pos) + "      " + quiz_list[pos - 1]);
                        //타이머 다시 시작
                        quizStart.start();
                    }
                }
                else if(pos == num){
                    if(correctSet != -1) {   // Wrong/Correct 결과 안나왔을 경우만 버튼 기능 수행
                        quiz_result[pos - 1] = "PASS";    //pass버튼 누르면 result배열에 PASS로 표시되도록하기
                        pos++;
                        tv_correct.setText("");
                        tv_imageNum.setText("Finish!!");
                        //모든 단어 끝나면 다음 PASS 버튼이 결과보기 버튼을 바뀌도록
                        bt_next.setText("결과 확인");
                    }
                } else {
                    //결과 보여주는 창으로 이동 - quizResult class
                    Intent intent = new Intent(getApplicationContext(), QuizResult.class);
                    intent.putExtra("list_result", quiz_result);
                    intent.putExtra("list", quiz_list);
                    intent.putExtra("Total", num);
                    intent.putExtra("Correct", correctN);
                    startActivity(intent);
                }
            }
        });

        //타이머들 설정
        //단어 보여주고 생각할시간 주는 타이머 4초
        quizStart = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //1초마다 여기로 와서 해당 작업 수행
                Log.d("predict", millisUntilFinished/1000L+"");
                tv_correct.setText(""+millisUntilFinished/1000L);
            }

            @Override
            public void onFinish() {
                //정해둔 시간이 끝나면 여기로 옴 - quizCorrect 타이머 실행
                quizCount.start();
                countSet = 1;   //
                tv_correct.setText("");
            }
        };
        //제한시간 10초
        quizCount = new CountDownTimer(11000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int)(millisUntilFinished/1000L);
                if(sec > 10) sec = 10;  //간혹 11이 출력되는 경우가 있어서 10부터 값이 나오도록 지정
                pb_count.setProgress(sec * 10);
                tv_progress.setText(""+ sec);
            }

            @Override
            public void onFinish()
            {
                pb_count.setProgress(0);
                tv_progress.setText("0");
                if(correctSet == 0) { //count 끝나는 때에 정답 3초유지 안하고 있으면 바로 wrong
                    correctSet = -1;
                    tv_imageNum.setText("WRONG");
                    //다음 단어로 넘어가는 타이머 시작
                    quizFinish.start();
                }
                else    //count 끝났지만 정답 3초유지 과정이라면 일단 대기
                    tv_imageNum.setText("0");
                countSet = 2;
            }
        };
        //맞는 동착 3초 유지
        quizCorrect = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_correct.setText(""+millisUntilFinished/1000L);
            }

            @Override
            public void onFinish() {
                quizCount.cancel(); //제한 시간 이내에 단어 맞았을 경우
                tv_imageNum.setText("CORRECT");
                correctN++;
                quiz_result[pos-1] = "O";
                countSet = 2;
                correctSet = -1;
                //다음 단어로 넘어가는 타이머 시작
                quizFinish.start();
            }
        };
        //단어 한개에 대해 퀴즈 끝내고 다음 단어로 넘어가기 전 텀 2초
        quizFinish = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //모든 타이머를 중지시킨다.
                quizCount.cancel();
                //progress bar 10초 시작 단계로 세팅
                pb_count.setProgress(100);
                tv_progress.setText("10");
                if (pos < num) {
                    countSet = correctSet = 0;
                    pos = pos + 1;
                    tv_imageNum.setText("#" + Integer.toString(pos) + "      " + quiz_list[pos-1]);
                    quizStart.start();
                } else {
                    pos++;
                    tv_imageNum.setText("Finish!!");
                    //모든 단어 끝나면 다음 PASS 버튼이 결과보기 버튼을 바뀌도록
                    bt_next.setText("결과 확인");
                }
            }
        };
        quizStart.start();
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

        for(int i=0; i<60; i++) {
            if (Math.round(output[0][i]) == 1) {
                //현재 단어와 동작 일치할 경우 - 초록색
                if (quiz_list[pos - 1].equals(list[i])) {

                    if (correctSet == 0) { //quizCorrect 타이머 작동안할때만 시작시키기
                        quizCorrect.start();  //일치할경우 3초 세기 시작
                        correctSet = 1;
                    }
                    Imgproc.rectangle(matResult, new Point(15, 10), new Point(matResult.cols() - 15, matResult.rows() - 10), green, 30);
                }
                //현재 단어와 동작 불일치 경우 - 빨간색
                else {
                    if (correctSet == 1) { //quizCorrect 타이머 작동중에만 중단
                        quizCorrect.cancel();  //틀릴경우 타이머 중단
                        if (countSet == 2) {
                            correctSet = -1;    //10초 카운트가 끝나고 마지막 기회에서 단어 틀리면 더이상 퀴즈 맞추기 불가
                            tv_imageNum.setText("WRONG");
                            //다음 단어로 넘어가는 타이머 시작
                            quizFinish.start();
                        } else
                            correctSet = 0;
                        tv_correct.setText("");
                    }
                    Imgproc.rectangle(matResult, new Point(15, 10), new Point(matResult.cols() - 15, matResult.rows() - 10), red, 30);
                    //   Imgproc.circle(matResult, new Point(mOpenCvCameraView.getHeight()/2, mOpenCvCameraView.getWidth()/2), 200, red, 20,0);
                }
                break;
            }
        }
    }

    //모델 파일 인터프리터를 생성하는 함수
    private Interpreter getTfliteInterpreter(String modelPath) {
        try{
            return new Interpreter(loadModelFile(Quiz.this, modelPath));
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
    //카메라 작동은 여기에서 모두 수행됨
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();
        if ( matResult == null )
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        Core.flip(matInput,matResult, 1);    //수평-양수, 수직-0, 모두-음수
        //손모양 인식하여 맞는지 유무 검사
        if(countSet != 0 && correctSet != -1)   //countSet(0)=단어생각3초간시간 , correctSet(-1)=이미결과나와더이상안해도되는상태
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

        AlertDialog.Builder builder = new AlertDialog.Builder( Quiz.this);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //이전에 퀴즈를 중단하시겠습니까? 물어보는 창 보여주기
        builder.create().show();
    }
}
