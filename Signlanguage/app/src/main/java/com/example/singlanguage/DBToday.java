package com.example.singlanguage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBToday extends SQLiteOpenHelper {
    public final static String MY_CARDB_NAME = "TODAY";
    public final static String MY_CARDB_TABLE = "TODAY";
    public final static int MY_CARDB_VERSION = 1;

    private static volatile DBToday dbToday;

    public static DBToday getInstance(Context context) {
        if (dbToday == null) {
            synchronized (DBToday.class) {
                if (dbToday == null) {
                    dbToday = new DBToday(context);
                }
            }
        }

        return dbToday;

    }

    public DBToday(@Nullable Context context) {
        super(context, MY_CARDB_NAME , null, MY_CARDB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS TODAY");
        //name -> 사용자이름, date -> 처음 학습 시작한 날짜, count -> 학습하기로 한 단어 갯수, day -> 학습한지 며칠째, pos -> 오늘 학습한 단어수(진행사항 바를 위해)
        db.execSQL( "CREATE TABLE TODAY (name TEXT DEFAULT '', date TEXT DEFAULT '', count INTEGER DEFAULT 0, day INTEGER DEFAULT 0,pos INTEGER DEFAULT 0);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertname(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "INSERT INTO TODAY(name) VALUES('" + name+ "');");
        db.close();
    }

    //LearningStart.java 에서 날짜와 학습할 단어수를 저장하기 위한 함수
    public void insert(String name, String date, int count,int day,int pos) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "UPDATE TODAY SET date = '"+date+"',count = '"+count+"',day = '"+day+"',pos = '"+pos+"' WHERE name = '"+name+"';");
        db.close();
    }

    //TodayLearning.java에서 학습한 일수를 update하기 위한 함수
    //count를 통해 day 바꿀 행 찾아서 변경
    public  void updateday(int count, int day){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "UPDATE TODAY SET day = '"+day+"' WHERE count = '"+count+"';");
        db.close();
    }

    //TodayStart.java에서 오늘의 학습 단어 중에 배운 단어수를 update하기 위한 함수
    //TodayStart에서 date를 전달받지 않고 count만 전달받으니깐 count를 통해 pos 바꿀 행 찾아서 변경
    public void updatepos(int count, int pos){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "UPDATE TODAY SET pos = '"+pos+"' WHERE count = '"+count+"';");
        db.close();
    }

    //Setting.java에서 사용자의 이름을 변경하기 위한 함수
    //name -> 기존닉네임, chname ->변경할 이름
    public void updatename(String name, String chname){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "UPDATE TODAY SET name = '"+chname+"' WHERE name = '"+name+"';");
        db.close();
    }

    //Setting.java에서 학습 단어수를 변경하기 위한 함수
    public void updatecount(String name, int count){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "UPDATE TODAY SET count = '"+count+"' WHERE name = '"+name+"';");
        db.close();
    }

    public  String getName(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT name FROM TODAY", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0); //사용자이름
        }
        return result;
    }


    public String getDate() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT date FROM TODAY", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0); //오늘의 날짜
        }
        return result;
    }

    public Integer getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT count FROM TODAY", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0); //학습할 단어수
        }
        return Integer.valueOf(result);
    }

    public Integer getDay(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT day FROM TODAY", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0); //학습 일자
        }
        return Integer.valueOf(result);
    }

    public Integer getPos(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT pos FROM TODAY", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0); //학습한 단어수
        }
        return Integer.valueOf(result);
    }


}
