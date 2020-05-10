package com.pxxy.lawconsult.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.PosttieData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsultDao {
    //获取数据库对象
    public static AppSqlite sqlite = AppSqlite.getInstance(MyApp.getContext());
    public static SQLiteDatabase db = null;
    static {
        db = sqlite.getWritableDatabase();
    }
    //保存consult内容
    public static void saveConsultContent(List<PosttieData> list) {
        for (PosttieData posttieData : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.POSTTIE_T_ID,posttieData.getT_ID());
            cv.put(DBConstant.POSTTIE_USERAVATAR,posttieData.getUserAvatar());
            cv.put(DBConstant.POSTTIE_NICKNAME,posttieData.getNickName());
            cv.put(DBConstant.POSTTIE_CONTENT,posttieData.getContent());
            cv.put(DBConstant.POSTTIE_DATE,posttieData.getDate());
            db.insert(DBConstant.TB_POSTTIE,null,cv);
        }
    }
    //获取consult内容
    public static List<PosttieData> getAllConsultContent(){
        //定义list来接收数据
        ArrayList<PosttieData> list = new ArrayList<>();
        Cursor cursor = db.query(DBConstant.TB_POSTTIE,
                null, null, null, null, null, null);
        if(cursor==null) {
            while (cursor.moveToNext()) {
                PosttieData posttieData = new PosttieData(
                        cursor.getInt(cursor.getColumnIndex(DBConstant.POSTTIE_T_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstant.POSTTIE_USERAVATAR)),
                        cursor.getString(cursor.getColumnIndex(DBConstant.POSTTIE_NICKNAME)),
                        cursor.getString(cursor.getColumnIndex(DBConstant.CASES_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(DBConstant.POSTTIE_DATE)));
                list.add(posttieData);
            }
        }
        return list;
    }
    //清空表中的数据
    public static void deleteData(){
        db.delete(DBConstant.TB_POSTTIE,null,null);
    }
    //关闭连接
    public void close(){
        db.close();
    }
}
