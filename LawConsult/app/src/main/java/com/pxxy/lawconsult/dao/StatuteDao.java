package com.pxxy.lawconsult.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.Statute;

import java.util.ArrayList;
import java.util.List;

public class StatuteDao {
    public static AppSqlite sqlite = AppSqlite.getInstance(MyApp.getContext());
    public static SQLiteDatabase db = null;
    static {
        db = sqlite.getWritableDatabase();
    }
    /**
     * 插入律师
     * @param StatuteList
     */
     public static void SaveStatute(List<Statute> StatuteList){
         for(Statute mstatute:StatuteList){
             ContentValues values = new ContentValues();
             values.put(DBConstant.STATUTE_LAW_ID,mstatute.getLaw_id());
             values.put(DBConstant.STATUTE_LAW_TITLE,mstatute.getLaw_title());
             values.put(DBConstant.STATUTE_LAW_CONTENT,mstatute.getLaw_content());
             values.put(DBConstant.STATUTE_OPENDATE,mstatute.getOpendate());
             values.put(DBConstant.STATUTE_LAW_TYPE,mstatute.getLaw_type());
             //插入数据
             db.insert(DBConstant.TB_STATUTEB,null,values);
         }
     }
    /**
     * 获取所有的法条数据
     */
    public static List<Statute> getAllStatute(){
        List<Statute> statuteList=new ArrayList<>();
        //查询法条表
        Cursor cursor=db.query(DBConstant.TB_STATUTEB,null,null,null,
                null,null,null);
        while (cursor.moveToNext()){
            Statute mstatute=new Statute();
            mstatute.setLaw_id(cursor.getInt(cursor.getColumnIndex(DBConstant.STATUTE_LAW_ID)));
            mstatute.setLaw_title(cursor.getString(cursor.getColumnIndex(DBConstant.STATUTE_LAW_TITLE)));
            mstatute.setLaw_content(cursor.getString(cursor.getColumnIndex(DBConstant.STATUTE_LAW_CONTENT)));
            mstatute.setLaw_type(cursor.getString(cursor.getColumnIndex(DBConstant.STATUTE_LAW_TYPE)));
            mstatute.setOpendate(cursor.getString(cursor.getColumnIndex(DBConstant.STATUTE_OPENDATE)));
            statuteList.add(mstatute);
        }
        return statuteList;
    }
    /**
     * 关闭数据库
     */
    public static void closeDB(){
        db.close();
    }
    /**
     * 清空法条表
     */
    public static void clearDB(){
        db.delete(DBConstant.TB_STATUTEB,null,null);
    }
}
