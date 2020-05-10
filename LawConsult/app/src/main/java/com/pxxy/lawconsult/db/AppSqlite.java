package com.pxxy.lawconsult.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pxxy.lawconsult.constant.DBConstant;

public class AppSqlite extends SQLiteOpenHelper {
    //单例
    private static AppSqlite appSqlite = null;

    public static AppSqlite getInstance(Context context) {
        if (appSqlite == null) {
            appSqlite = new AppSqlite(context);
        }
        return appSqlite;
    }

    public AppSqlite(Context context) {
        super(context, DBConstant.DB_NAME, null, DBConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建主贴信息表
        String tb_posttie = "create table if not exists " + DBConstant.TB_POSTTIE + " (" + DBConstant.POSTTIE_T_ID +
                " integer primary key," +
                DBConstant.POSTTIE_USERAVATAR + " text," +
                DBConstant.POSTTIE_NICKNAME + " text," +
                DBConstant.POSTTIE_CONTENT + " text," +
                DBConstant.POSTTIE_DATE + " text)";
        //创建回帖信息表
        String tb_restoretie = "create table if not exists " + DBConstant.TB_RESTORETIE + "(" +
                "" + DBConstant.RESTORETIE_H_ID + "integer primary key," +
                "" + DBConstant.RESTORETIE_T_ID + "integer," +
                "" + DBConstant.RESTORETIE_H_CONTNET + "text)";
        //创建案例分析表
        String tb_cases = "create table if not exists " + DBConstant.TB_CASES + "(" + DBConstant.CASES_ID
                + " integer  primary key," + DBConstant.CASES_TITLE + " text," + DBConstant.CASES_TYPE + " text,"
                + DBConstant.CASES_CONTENT + " text," + DBConstant.CASES_IMAGE + " text)";


        //创建律师表
        String tb_lawer = "create table if not exists " + DBConstant.TB_LAWERUSER + "(" + DBConstant.LAWERUSER_ID
                + " integer primary key," + DBConstant.LAWERUSER_NAME + " text," + DBConstant.LAWERUSER_AGE
                + " integer," + DBConstant.LAWERUSER_SEX + " integer," + DBConstant.LAWERUSER_EMAIL + " text,"
                + DBConstant.LAWERUSER_NUMBER + " text," + DBConstant.LAWERUSER_SPECIALITY + " text," + DBConstant.LAWERUSER_OFFICE
                + " text," + DBConstant.LAWERUSER_ADDRESS + " text," + DBConstant.LAWERUSER_TEL + " text,"
                + DBConstant.LAWERUSER_PHOTO + " text," + DBConstant.LAWERUSER_TYPE + " text,"
                + DBConstant.LAWERUSER_INTRODUCTION + " text" + ")";
        //创建法条表
        String tb_statute = "create table if not exists " + DBConstant.TB_STATUTEB + "(" + DBConstant.STATUTE_LAW_ID
                + " integer primary key autoincrement," + DBConstant.STATUTE_LAW_TITLE + " text," + DBConstant.STATUTE_LAW_CONTENT
                + " text," + DBConstant.STATUTE_OPENDATE + " text," + DBConstant.STATUTE_LAW_TYPE + " text)";

        db.execSQL(tb_posttie);
        db.execSQL(tb_lawer);
        db.execSQL(tb_cases);
        db.execSQL(tb_statute);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
