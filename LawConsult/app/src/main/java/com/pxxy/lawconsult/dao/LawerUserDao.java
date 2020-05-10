package com.pxxy.lawconsult.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.LawerUser;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;


public class LawerUserDao {
    public static AppSqlite sqlite = AppSqlite.getInstance(MyApp.getContext());
    public static SQLiteDatabase db = null;
    static {
        db = sqlite.getWritableDatabase();
    }
    /**
     * 插入律师
     * @param LawerUserList
     */
    public static void SaveLawerUser(List<LawerUser> LawerUserList){

        for (LawerUser mlawerUser:LawerUserList){
            ContentValues values = new ContentValues();
            values.put(DBConstant.LAWERUSER_ID,mlawerUser.getLawerId());
            values.put(DBConstant.LAWERUSER_NAME,mlawerUser.getLawerName());
            values.put(DBConstant.LAWERUSER_AGE,mlawerUser.getLawerAge());
            values.put(DBConstant.LAWERUSER_SEX,mlawerUser.getLawerSex());
            values.put(DBConstant.LAWERUSER_EMAIL,mlawerUser.getEmail());
            values.put(DBConstant.LAWERUSER_NUMBER,mlawerUser.getNumber());
            values.put(DBConstant.LAWERUSER_OFFICE,mlawerUser.getOffice());
            values.put(DBConstant.LAWERUSER_SPECIALITY,mlawerUser.getSpeciality());
            values.put(DBConstant.LAWERUSER_INTRODUCTION,mlawerUser.getIntroduction());
            values.put(DBConstant.LAWERUSER_ADDRESS,mlawerUser.getAddress());
            values.put(DBConstant.LAWERUSER_TEL,mlawerUser.getTel());
            values.put(DBConstant.LAWERUSER_TYPE,mlawerUser.getType());
            values.put(DBConstant.LAWERUSER_PHOTO,mlawerUser.getImage());
            //插入数据
            db.insert(DBConstant.TB_LAWERUSER,null,values);
        }
    }
    /**
     * 获取所有的律师数据
     */
    public static List<LawerUser> getAllLawerUser(){
        List<LawerUser> lawerUsers=new ArrayList<>();
        //查询律师表
        Cursor cursor=db.query(DBConstant.TB_LAWERUSER,null,null,
                null,null,null,null);
        //便利律师表里的数据
        while(cursor.moveToNext()){
            LawerUser mlawerUser=new LawerUser();
            //设置数据
            mlawerUser.setLawerId(cursor.getInt(cursor.getColumnIndex(DBConstant.LAWERUSER_ID)));
            mlawerUser.setLawerName(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_NAME)));
            mlawerUser.setLawerAge(cursor.getInt(cursor.getColumnIndex(DBConstant.LAWERUSER_AGE)));
            mlawerUser.setLawerSex(cursor.getInt(cursor.getColumnIndex(DBConstant.LAWERUSER_SEX)));
            mlawerUser.setEmail(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_EMAIL)));
            mlawerUser.setOffice(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_OFFICE)));
            mlawerUser.setSpeciality(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_SPECIALITY)));
            mlawerUser.setIntroduction(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_INTRODUCTION)));
            mlawerUser.setAddress(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_ADDRESS)));
            mlawerUser.setNumber(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_NUMBER)));
            mlawerUser.setTel(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_TEL)));
            mlawerUser.setType(cursor.getInt(cursor.getColumnIndex(DBConstant.LAWERUSER_TYPE)));
            mlawerUser.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.LAWERUSER_PHOTO)));
            //添加数据
            lawerUsers.add(mlawerUser);
        }
        return lawerUsers;
    }
    /**
     * 关闭数据库
     */
    public static void closeDB(){
        db.close();
    }
    /**
     * 清空律师表
     */
    public static void clearDB(){
        db.delete(DBConstant.TB_LAWERUSER,null,null);
    }
}
