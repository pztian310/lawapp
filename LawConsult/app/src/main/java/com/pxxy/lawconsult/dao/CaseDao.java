package com.pxxy.lawconsult.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.Case;

import java.util.ArrayList;
import java.util.List;

public class CaseDao {
    public static AppSqlite sqlite = AppSqlite.getInstance(MyApp.getContext());
    public static SQLiteDatabase db = null;

    static {
        db = sqlite.getWritableDatabase();
    }

    /**
     * 插入案例
     * @param caseList
     */
    public static void saveCase(List<Case> caseList){

        for (Case mCase:caseList){
            ContentValues values = new ContentValues();
            values.put(DBConstant.CASES_ID,mCase.getId());
            values.put(DBConstant.CASES_TITLE,mCase.getTitle());
            values.put(DBConstant.CASES_TYPE,mCase.getType());
            values.put(DBConstant.CASES_CONTENT,mCase.getContent());
            values.put(DBConstant.CASES_IMAGE,mCase.getImage());
            //插入数据
            db.insert(DBConstant.TB_CASES,null,values);
        }

    }

    public static List<Case> getAllCase(){
        List<Case> cases = new ArrayList<>();
        //查询案例表
        Cursor cursor = db.query(DBConstant.TB_CASES,null,
                null,null,
                null,null,null);
        //获取数据
        while (cursor.moveToNext()){
            Case mCase = new Case();
            //设置数据
            mCase.setId(cursor.getInt(cursor.getColumnIndex(DBConstant.CASES_ID)));
            mCase.setType(cursor.getString(cursor.getColumnIndex(DBConstant.CASES_TYPE)));
            mCase.setTitle(cursor.getString(cursor.getColumnIndex(DBConstant.CASES_TITLE)));
            mCase.setContent(cursor.getString(cursor.getColumnIndex(DBConstant.CASES_CONTENT)));
            mCase.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.CASES_IMAGE)));
            //往cases中添加数据
            cases.add(mCase);
        }
        return cases;
    }

    /**
     * 关闭数据库
     */
    public static void closeDB(){
        db.close();
    }

    /**
     * 清空案例表
     */
    public static void clearDB(){
        db.delete(DBConstant.TB_CASES,null,null);
    }
}
