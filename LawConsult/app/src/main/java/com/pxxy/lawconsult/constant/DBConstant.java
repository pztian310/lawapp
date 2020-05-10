package com.pxxy.lawconsult.constant;

public class DBConstant {
    public  static String DB_NAME="lawConsult.db";      //本地数据库名称
    public  static int DB_VERSION=1;                    //本地数据库版本

    //咨询内容表
    public static String TB_POSTTIE="posttie";             //表名
    public static String POSTTIE_T_ID="T_ID";            //主帖编号
    public static String POSTTIE_USERAVATAR="userAvatar";     //主贴标题
    public static String POSTTIE_T_TEL="T_tel";             //用户手机号码
    public static String POSTTIE_CONTENT="T_content";   //内容
    public static String POSTTIE_NICKNAME="nickName";       //用户名
    public static String POSTTIE_DATE="T_date";       //发帖时间
    public static String POSTTIE_T_TYPE="T_type";         //发帖类型
    public static String REQUEST_INDEX="index";        //请求帖的位置
    public static String POSTTIE_T_OBJECT="T_object";       //律师id

    //回帖信息表
    public static String TB_RESTORETIE="restoretie";   //表名
    public static String RESTORETIE_H_ID="H_id";        //回帖编号
    public static String RESTORETIE_T_ID="T_id";        //与posttie中的T_ID关联
    public  static String RESTORETIE_H_CONTNET="H_content";//回帖的内容
    public static String RESTORETIE_H_TEL="H_tel";  //回帖人的手机号码
    public static String RESTORETIE_H_DATE="H_date";    //回帖的时间
    public static String RESRORETIE_H_FROMUID="H_fromuid";  //回帖的目的id

    //案例表
    public static String TB_CASES = "cases";
    public static String CASES_ID = "case_id";
    public static String CASES_TYPE = "case_type";
    public static String CASES_IMAGE = "case_image";
    public static String CASES_CONTENT = "case_content";
    public static String CASES_TITLE = "case_title";

    //律师表
    public static String TB_LAWERUSER="laweruser";
    public static String LAWERUSER_ID="laweruser_id";
    public static String LAWERUSER_NAME="laweruser_name";
    public static String LAWERUSER_AGE="laweruser_age";
    public static String LAWERUSER_SEX="laweruser_sex";
    public static String LAWERUSER_EMAIL="email";
    public static String LAWERUSER_NUMBER="number";
    public static String LAWERUSER_SPECIALITY="speciality";
    public static String LAWERUSER_OFFICE="office";
    public static String LAWERUSER_INTRODUCTION="introduction";
    public static String LAWERUSER_ADDRESS="adderss";
    public static String LAWERUSER_TEL="tel";
    public static String LAWERUSER_PHOTO="photo";
    public static String LAWERUSER_TYPE="type";

    //法条表
    public static String TB_STATUTEB="statuteb";
    public static String STATUTE_LAW_ID="law_id";
    public static String STATUTE_LAW_TITLE="law_title";
    public static String STATUTE_LAW_CONTENT="law_content";
    public static String STATUTE_LAW_TYPE="law_type";
    public static String STATUTE_OPENDATE="opendate";
}
