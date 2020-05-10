package com.pxxy.lawconsult.okhttp3;


import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.utils.MD5;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    public OkHttpClient okHttpClient = null;

    /**
     * 登录功能
     *
     * @param tel      登录电话号码
     * @param password 登录密码
     * @param callback 登录回调方法
     */
    public void login(String tel, String password, Callback callback) {

        //MD5加密密码
        String md5Password = MD5.getMD5x100(password);
        //发送请求
        request(tel, md5Password, HttpConstant.SERVER_LOGINSERVLET, callback);

    }

    /**
     * 注册功能
     *
     * @param tel      //注册电话号码
     * @param password //注册的密码
     * @param callback //注册的接口回调
     */
    public void register(String tel, String password, Callback callback) {
        //MD5加密密码
        String md5Password = MD5.getMD5x100(password);
        //发送请求
        request(tel, md5Password, HttpConstant.SERVER_REGISTERSERVLET, callback);
    }

    /**
     * 找回密码
     *
     * @param tel         电话号码
     * @param newPassword 新密码
     * @param callback    接口回调
     */
    public void findPassword(String tel, String newPassword, Callback callback) {
        //MD5加密密码
        String md5Password = MD5.getMD5x100(newPassword);
        //发送请求
        request(tel, md5Password, HttpConstant.SERVER_FINDPASSWORD, callback);
    }

    /**
     * 发送请求
     *
     * @param tel           第一个值
     * @param password      第二个值
     * @param serverAddress 服务器地址
     * @param callback      回调方法
     */
    public void request(String tel, String password, String serverAddress, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(User.TEL, tel)
                .add(User.PASSWORD, password)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(serverAddress)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * @param tel           请求的值
     * @param serverAddress 服务器地址
     * @param callback      回调方法
     */
    public void request(String tel, String serverAddress, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(User.TEL, tel)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(serverAddress)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 判断tel是否存在
     *
     * @param tel
     * @param callback
     */
    public void isUserExist(String tel, Callback callback) {
        //请求是否
        request(tel, "", HttpConstant.SERVER_ISUSEREXIST, callback);
    }

    /**
     * 请求头像数据
     *
     * @param tel
     * @param callback
     */
    public void getUserPhoto(String tel, Callback callback) {
        //请求头像
        request(tel, "", HttpConstant.SERVER_GETUSERPHOTO, callback);
    }

    public void getCase(int start, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add("start", String.valueOf(start))
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETCASE)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 请求律师的数据
     *
     * @param tel          律师的电话
     * @param lawerName    律师姓名
     * @param lawerAge     律师年龄
     * @param lawerSex     律师性别
     * @param email        律师邮箱
     * @param speciality   律师特长
     * @param office       律师单位
     * @param introduction 律师介绍
     * @param address      律师地址
     * @param lawerId      律师ID
     * @param number       律师编号
     * @param birthday     律师生日
     * @param callback
     */
    public void getLawerUser(String tel, String lawerId, String lawerName, String lawerAge, String lawerSex,
                             String email, String speciality, String office, String introduction, String address,
                             String number,String birthday, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(User.TEL, tel)
                .add(LawerUser.LAWERID, lawerName)
                .add(LawerUser.LAWERNAME, lawerName)
                .add(LawerUser.LAWERAGE, lawerAge)
                .add(LawerUser.LAWERSEX, lawerSex)
                .add(LawerUser.EMAIL, email)
                .add(LawerUser.SPECIALITY, speciality)
                .add(LawerUser.OFFICE, office)
                .add(LawerUser.INTRODUCTION, introduction)
                .add(LawerUser.NUMBER, number)
                .add(LawerUser.BIRTHDAY,birthday)
                .build();

        //请求律师数据
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETLAWERUSER)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void getLawerUserList(int start, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add("start", String.valueOf(start))
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETLAWERUSER)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }
    //模糊查询获取法条List集合
    public void getFuzzyStatuteList(String law_content,Callback callback){
        if (okHttpClient==null){
            okHttpClient=new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody=new FormBody.Builder()
                .add("law_content",law_content)
                .build();
        //创建request对象
        Request request=new Request.Builder()
                .url(HttpConstant.SERVER_GETFuzzySTATUTE)
                .post(formBody)
                .build();
        //回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }
    //图文咨询
    public void Byconsulting(String lawer_id, String tel, String consultType,
                             String consultContent, String consultTime,
                             Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(DBConstant.POSTTIE_T_OBJECT,lawer_id)
                .add(DBConstant.POSTTIE_T_TEL, tel)
                .add(DBConstant.POSTTIE_CONTENT, consultContent)
                .add(DBConstant.POSTTIE_DATE, consultTime)
                .add(DBConstant.POSTTIE_T_TYPE, consultType)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_BYCONSULTING)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    //法条List集合
    public void getStatuteList(String law_type, Callback callback){
        if (okHttpClient==null){
            okHttpClient=new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody=new FormBody.Builder()
                .add("law_type",law_type)
                .build();
        //创建request对象
        Request request=new Request.Builder()
                .url(HttpConstant.SERVER_GETSTATUTE)
                .post(formBody)
                .build();
        //回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }
    /**
     * 发布咨询
     *
     * @param tel            发布人的电话
     * @param consultType    发布的类型
     * @param consultContent 发布的内容
     * @param consultTime    发布的时间
     * @param callback       回调方法
     */
    public void saveConsultContent(String tel, String consultType, String consultContent, String consultTime, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(DBConstant.POSTTIE_T_TEL, tel)
                .add(DBConstant.POSTTIE_CONTENT, consultContent)
                .add(DBConstant.POSTTIE_DATE, consultTime)
                .add(DBConstant.POSTTIE_T_TYPE, consultType)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_SAVECONSULT)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 获取用户的收藏案例
     *
     * @param userId   user的id
     * @param callback 回调方法
     */
    public void getUserCollect(int userId, Callback callback) {
        String id = String.valueOf(userId);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.ADD_COLLECT_USER_ID, id)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GET_USER_COLLECTSERVLET)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 添加用户收藏数据
     *
     * @param userId   用户id
     * @param caseId   案例id
     * @param callback 回调
     */
    public void addUserCollect(int userId, int caseId, Callback callback) {
        String id = String.valueOf(userId);
        String mCase = String.valueOf(caseId);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.ADD_COLLECT_USER_ID, id)
                .add(HttpConstant.ADD_COLLECT_CASE_ID, mCase)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADD_USER_COLLECTSERVLET)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 删除用户收藏
     *
     * @param userId   用户id
     * @param caseId   案例id
     * @param callback 回调方法
     */
    public void deleteUserCollect(int userId, int caseId, Callback callback) {
        String id = String.valueOf(userId);
        String mCase = String.valueOf(caseId);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.DELETE_COLLECT_USER_ID, id)
                .add(HttpConstant.DELETE_COLLECT_CASE_ID, mCase)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_DELETE_USER_COLLECTSERVLET)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 请求咨询内容
     *
     * @param consultType
     * @param callback
     */
    public void getConsultContent(String consultType, String index, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(DBConstant.POSTTIE_T_TYPE, consultType)
                .add(DBConstant.REQUEST_INDEX, index)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETCONSULTCONTENT)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 获取回帖律师信息
     *
     * @param T_ID     发帖的编号
     * @param callback
     */
    public void getLawyerData(int T_ID, Callback callback) {
        String posttieId = String.valueOf(T_ID);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(DBConstant.POSTTIE_T_ID, posttieId).build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETREPLYLAWYER)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 保存律师的回复内容
     *
     * @param T_ID         回主贴的ID
     * @param replyContent 回帖的内容
     * @param date         回帖时间
     * @param callback
     * @Param tel 回帖人的手机号码
     */
    public void saveLawyerReply(int T_ID, String replyContent, String date, String tel, Callback callback) {
        String posttieId = String.valueOf(T_ID);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(DBConstant.RESTORETIE_T_ID, posttieId)
                .add(DBConstant.RESTORETIE_H_CONTNET, replyContent)
                .add(DBConstant.RESTORETIE_H_DATE, date)
                .add(DBConstant.RESTORETIE_H_TEL, tel)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(HttpConstant.SERVER_SAVELAWYERREPLY)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 管理员界面删除用户
     *
     * @param userId
     * @param userType
     * @param callback
     */
    public void adminDeleteUser(int userId, int userType, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String id = String.valueOf(userId);
        String type = String.valueOf(userType);
        FormBody formBody = new FormBody.Builder()
                .add(User.ID, id)
                .add(User.TYPE, type)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_DELETE_USER_SERVLET)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 删除案例对象
     *
     * @param caseId
     * @param callback
     */
    public void adminDeleteCase(int caseId, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String id = String.valueOf(caseId);
        FormBody formBody = new FormBody.Builder()
                .add(Case.ID, id)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_DELETE_CASE)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 删除法条
     *
     * @param caseId
     * @param callback
     */
    public void adminDeleteStatute(int caseId, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String id = String.valueOf(caseId);
        FormBody formBody = new FormBody.Builder()
                .add(Statute.LAW_ID, id)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_DELETE_STATUTE)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 获取管理员界面的用户
     *
     * @param startIndex
     * @param server
     * @param callback
     */
    public void adminGetUser(int startIndex, int type, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String server = "";
        if (type == 0) {
            server = HttpConstant.SERVER_ADMIN_GETCLIENT_USER_SERVLET;
        } else if (type == 1) {
            server = HttpConstant.SERVER_ADMIN_GETLAWYER_USER_SERVLET;
        }
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.ADMIN_USER_INDEX, String.valueOf(startIndex))
                .build();
        Request request = new Request.Builder()
                .url(server)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * admin获取Case
     *
     * @param startIndex
     * @param callback
     */
    public void adminGetCase(int startIndex, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String index = String.valueOf(startIndex);
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.ADMIN_GETCASE_CASEINDEX, index)
                .build();

        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_GETCASE_SERVLET)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * admin修改case
     *
     * @param title
     * @param type
     * @param image
     * @param content
     * @param callback
     */
    public void adminUpdateCase(int id, String title, String type, String image, String content, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(Case.ID, String.valueOf(id))
                .add(Case.TITLE, title)
                .add(Case.TYPE, type)
                .add(Case.IMAGE, image)
                .add(Case.CONTENT, content)
                .build();

        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_UPDATE_CASE)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 更新法条
     *
     * @param id
     * @param title
     * @param type
     * @param opendate
     * @param content
     * @param callback
     */
    public void adminUpdateStatute(int id, String title, String type, String opendate, String content, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(Statute.LAW_ID, String.valueOf(id))
                .add(Statute.LAW_TITLE, title)
                .add(Statute.LAW_TYPE, type)
                .add(Statute.OPENDATE, opendate)
                .add(Statute.LAW_CONTENT, content)
                .build();

        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_UPDATE_STATUTE)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 网络请求添加案例
     *
     * @param title
     * @param type
     * @param image
     * @param content
     * @param callback
     */
    public void adminAddCase(String title, String type, String image, String content, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody formBody = new FormBody.Builder()
                .add(Case.TITLE, title)
                .add(Case.TYPE, type)
                .add(Case.IMAGE, image)
                .add(Case.CONTENT, content)
                .build();

        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_ADD_CASE)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 获取法条数据
     *
     * @param startIndex
     * @param callback
     */
    public void adminGetStatute(int startIndex, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        String index = String.valueOf(startIndex);
        FormBody formBody = new FormBody.Builder()
                .add(HttpConstant.ADMIN_GETSTATURE_STATUREINDEX, index)
                .build();

        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_ADMIN_GET_STATUTE_SERVLET)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 获取所有通知
     *
     * @param callback
     */
    public void getAllNotice(Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GET_NOTICE_SERVLET)
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    /**
     * 修改头像
     *
     * @param tel      手机号
     * @param file     上传的文件
     * @param fileName 文件名
     */
    public void setUserPhoto(String tel, File file, String fileName, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        RequestBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(User.TEL, tel)
                .addFormDataPart(HttpConstant.SETUSERPHOTO, fileName, RequestBody.create(MediaType.parse("image/png"), file))
                .build();

        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_SETUSERPHOTO)
                .post(build)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 上传昵称和个性签名
     *
     * @param tel       手机号
     * @param pickName  昵称
     * @param signature 个性签名
     */
    public void setPickName(String tel, String pickName, String signature, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(User.TEL, tel)
                .add(HttpConstant.SETNAME, pickName)
                .add(HttpConstant.SETSIGNATURE, signature)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_SETNICKNAME)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    /**
     * 获取昵称和个性签名
     *
     * @param tel 手机号
     */
    public void getNickName(String tel, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        //创建表单
        FormBody formBody = new FormBody.Builder()
                .add(User.TEL, tel)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GETNICKNAME)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     *
     * @param law_id
     * @param law_title
     * @param law_content
     * @param law_type
     * @param opendate
     * @param callback
     */
    public void getStatute(String law_id,String law_title,String law_content,String law_type,String opendate,Callback callback){
        if(okHttpClient==null){
            okHttpClient=new OkHttpClient();
        }
        //创建表单对象
        FormBody formBody=new FormBody.Builder()
                .add(DBConstant.STATUTE_LAW_ID,law_id)
                .add(DBConstant.STATUTE_LAW_TITLE,law_title)
                .add(DBConstant.STATUTE_LAW_CONTENT,law_content)
                .add(DBConstant.STATUTE_LAW_TYPE,law_type)
                .add(DBConstant.STATUTE_OPENDATE,opendate)
                .build();
        //创建request对象
        Request request=new Request.Builder()
                .url(HttpConstant.SERVER_GETSTATUTE)
                .post(formBody)
                .build();
        //接口回调
        okHttpClient.newCall(request).enqueue(callback);
    }
    /**
     * 获取收藏案例
     *
     * @param userId   user的id
     * @param callback 回调方法
     */
    public void getUserCase(int userId, Callback callback) {
        String id = String.valueOf(userId);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add("id", id)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_GET_USER_CASESERVLET)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }
    /**
     * 申请律师
     * @param userId 用户id
     * @param lawyerName 律师姓名
     * @param lawyerAge 姓名
     * @param  lawyerSex 性别
     * @param  lawyerEmail 电子邮件
     * @param  lawyerNum 律师编号
     * @param  lawyerSpeciality  擅长类别
     * @param lawyerOffice 所在单位
     * @param lawyerIntroduction 个人介绍
     * @param lawyerAddress 地址
     * 
     * */
    public void setLaywerUser(int userId,String lawyerName,String lawyerAge ,int lawyerSex ,String lawyerEmail,String lawyerNum,String lawyerSpeciality,String lawyerOffice,String lawyerIntroduction,String lawyerAddress,Callback callback) {
        String id = String.valueOf(userId);
        String sex=String.valueOf(lawyerSex);
        String age = String.valueOf(lawyerAge);

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        //创建表单对象
        FormBody formBody = new FormBody.Builder()
                .add(LawerUser.USERID, id)
                .add(LawerUser.LAWERNAME, lawyerName)
                .add(LawerUser.LAWERAGE, lawyerAge)
                .add(LawerUser.LAWERSEX, sex)
                .add(LawerUser.EMAIL, lawyerEmail)
                .add(LawerUser.NUMBER, lawyerNum)
                .add(LawerUser.SPECIALITY, lawyerSpeciality)
                .add(LawerUser.OFFICE, lawyerOffice)
                .add(LawerUser.INTRODUCTION, lawyerIntroduction)
                .add(LawerUser.ADDRESS, lawyerAddress)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_APPLY_LAWYER)
                .post(formBody)
                .build();
        //设置接口回调接口
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     *  获取用户给单个律师的发布
     * @param lawyer_id 律师id
     * */
    public void getUserToLawyer(String lawyer_id,String tel, Callback callback){
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        FormBody builder = new FormBody.Builder()
                .add("T_object", lawyer_id)
                .add(User.TEL,tel)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.SERVER_USERTOAPPWER)
                .post(builder)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
