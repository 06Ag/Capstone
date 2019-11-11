package com.example.singlanguage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Blob;



public class DBHelper extends SQLiteOpenHelper {
    private Context con; // excel 데이터 넘겨받기위해
    public final static String MY_CARDB_NAME = "SIGN_BOOK";
    public final static String MY_CARDB_TABLE = "SIGN_BOOK";
    public final static int MY_CARDB_VERSION = 1;

    private static volatile DBHelper dbHelper;

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
            }
        }

        return dbHelper;

    }

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context) {
        super(context, MY_CARDB_NAME , null, MY_CARDB_VERSION);
        this.con = context;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 앱을 삭제후 앱을 재설치하면 기존 DB파일은 앱 삭제시 지워지지 않기 때문에
        // 테이블이 이미 있다고 생성 에러남
        // 앱을 재설치시 데이터베이스를 삭제해줘야함.
        db.execSQL("DROP TABLE IF EXISTS SIGN_BOOK");

        // 새로운 테이블 생성
        /* 이름은 SIGN_BOOK이고,, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        name 문자열 컬럼, class1 문자열 컬럼, class2 문자열 컬럼,
        image BLOB형, des 문자열 컬럼, chlearn int형 컬럼으로 구성된 테이블을 생성. */
        //초기값을 0(false)로 맞춰둘게
        db.execSQL( "CREATE TABLE SIGN_BOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, class1 TEXT, class2 TEXT, image TEXT, des TEXT, chlearn INTEGER DEFAULT 0);");


        //excel데이터 읽어와서 db에 넣을려는 코드(일단 컬럼 3개만 넣어봄)
        InputStream is = con.getResources().openRawResource(R.raw.capdb);
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
                String image = tokens[3];
                String des = tokens[4];
                if(tokens.length>=3 && tokens[2].length() >0) {
                    cl2 = tokens[2]; //아직 class2구분안해놓은게 있어서..
                }
                // DB에 입력한 값으로 행 추가
                db.execSQL( "INSERT INTO SIGN_BOOK(_id,name,class1,class2,image,des) VALUES(null, " +
                        "'" + name + "', '" + cl1 + "', '" + cl2 + "', '" + image + "', '" + des + "');");
            }
        }catch(IOException e){
            Log.wtf( "MyActivity","Error reading data file on line" + line, e);
            e.printStackTrace();;
        }
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //나(영지)가 컬럼 insert할때
    //은진이가 이미지랑 설명이랑 학습여부 삽입할때 써야하는 함수는 만들어야함(보류)
    public void insert(String name, String class1, String class2, Blob image) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL( "INSERT INTO SIGN_BOOK(_id,name,class1,class2,image) VALUES(null, " +
                "'" + name + "', '" + class1 + "', '" + class2 + "', '" + image + "');");
        db.close();
    }


    // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM SIGN_BOOK", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0) //index번호
                    + ". 수화 이름: "
                    + cursor.getString(1) //수화이름
                    + "  분류1: "
                    + cursor.getString(2) //분류1
                    + "  분류2: "
                    + cursor.getString(3) //분류2
                    + "\n"
                    + "    이미지: "
                    + cursor.getString(4)//이미지
                    + "  설명: "
                    + cursor.getString(5) //설명
                    + "  학습여부: "
                    + cursor.getInt(6) //학습여부
                    + "\n\n";
        }
        return result;
    }

    //이름으로 이미지 파일 불러오기
    public String getResult_img(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT image FROM SIGN_BOOK WHERE name = '"+name+"';", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0); //수화 이름
        }
        return result;
    }

    //이름으로 설명 불러오기
    public String getResult_des(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT des FROM SIGN_BOOK WHERE name = '"+name+"';", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0); //수화 설명
        }
        return result;
    }

    //수어 검색 기능을 위한 함수
    public String selectResult(String sel) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM SIGN_BOOK WHERE TRIM(name) = '"+sel.trim()+"' ;", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0) //index번호
                    + ". 수화 이름: "
                    + cursor.getString(1) //수화이름
                    + "  분류1: "
                    + cursor.getString(2) //분류1
                    + "  분류2: "
                    + cursor.getString(3) //분류2
                    + "\n"
                    + "    이미지: "
                    + cursor.getString(4)//이미지
                    + "  설명: "
                    + cursor.getString(5) //설명
                    + "  학습여부: "
                    + cursor.getInt(6) //학습여부
                    + "\n\n";
        }
        return result;
    }
     //db내에 있는 단어 수를 반환해주는 함수 -> TodayStart.java에서 필요
    public int getCount(){
        int cnt =0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SIGN_BOOK", null);
        cnt = cursor.getCount();
        return cnt;
    }
    //TodayStart.java에서 배울 단어의 이름의 반환해주는 함수
    public String getName(Integer i){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT name FROM SIGN_BOOK WHERE _id = '"+i+"';", null);
        while (cursor.moveToNext()) {
             result += cursor.getString(0); //수화이름
        }
        return result;
    }
    // TodayStart.java에서 학습을 한 후 db 컬럼의 chlearn 값을 1로 바꿔주는 함수
    public void setLearn(Integer i){
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL( "UPDATE SIGN_BOOK SET chlearn = 1 WHERE _id = '"+i+"';");
        db.close();

    }
    //TodayLearning.java에서 지금까지 외운 총 단어수를 나타내기 위한 함수
    public int getchlearn(){
        int cnt =0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SIGN_BOOK WHERE chlearn = 1", null);
        cnt = cursor.getCount();
        return cnt;
    }
    public int checkchlearn(int i){
        int cnt =0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT chlearn FROM SIGN_BOOK WHERE _id = '"+i+"';", null);
        while (cursor.moveToNext()) {
            cnt = cursor.getInt(0);
        }
        return cnt;
    }

}
