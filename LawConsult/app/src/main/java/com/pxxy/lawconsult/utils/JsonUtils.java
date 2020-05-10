package com.pxxy.lawconsult.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.Notice;
import com.pxxy.lawconsult.entity.RegisterData;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.entity.PosttieData;
import com.pxxy.lawconsult.entity.ReplyLawyerData;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static Gson gson = new Gson();

    /**
     * json数据转Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMap(String json) {
        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    /**
     * json对象转caseList
     *
     * @param json
     * @return
     */
    public static List<Case> jsonToCaseList(String json) {
        List<Case> caseList = gson.fromJson(json, new TypeToken<List<Case>>() {
        }.getType());
        return caseList;
    }

    /**
     * json对象转statuteList
     *
     * @param json
     * @return
     */
    public static List<Statute> jsonToStatuteList(String json) {
        List<Statute> statutes = gson.fromJson(json, new TypeToken<List<Statute>>() {
        }.getType());
        return statutes;
    }

    /**
     * json转IntegerList
     *
     * @param json
     * @return
     */
    public static List<Integer> jsonToIntegerList(String json) {
        List<Integer> caseList = gson.fromJson(json, new TypeToken<List<Integer>>() {
        }.getType());
        return caseList;
    }

    /**
     * map对象转json数据
     *
     * @param map
     * @return
     */
    public static String MapToJson(Map<String, String> map) {
        String json = gson.toJson(map);
        return json;
    }

    /**
     * clientUser对象转json数据
     *
     * @param user
     * @return
     */
    public static String clientUserToJson(ClientUser user) {
        String clientUserJson = gson.toJson(user);
        return clientUserJson;
    }

    /**
     * json数据转ClientUser对象
     *
     * @param userJson
     * @return
     */
    public static ClientUser jsonToClientUser(String userJson) {
        ClientUser user = gson.fromJson(userJson, ClientUser.class);
        return user;
    }

    /**
     * LawerUser对象转json数据
     *
     * @param user
     * @return
     */
    public static String lawerUserToJson(LawerUser user) {
        String lawerUserJson = gson.toJson(user);
        return lawerUserJson;
    }

    /**
     * 用户转json数据
     *
     * @param user
     * @return
     */
    public static String userToJson(User user) {
        String userJson = gson.toJson(user);
        return userJson;
    }


    /**
     * json数据转User对象
     *
     * @param json
     * @return
     */
    public static User jsonToUser(String json) {
        User user = gson.fromJson(json, User.class);
        return user;
    }

    /**
     * json数据转LawerUser对象
     *
     * @param userJson
     * @return
     */
    public static LawerUser jsonToLawerUser(String userJson) {
        LawerUser lawerUser = gson.fromJson(userJson, LawerUser.class);
        return lawerUser;
    }

    /**
     * json数据转CLientUser集合
     *
     * @param userjson
     * @return
     */
    public static List<ClientUser> jsonToClientUserList(String userjson) {
        List<ClientUser> clientUsers = gson.fromJson(userjson, new TypeToken<List<ClientUser>>() {
        }.getType());
        return clientUsers;
    }

    /**
     * json数据转LawyerUser集合
     *
     * @param userJson
     * @return
     */
    public static List<LawerUser> jsonToLawerUserList(String userJson) {
        List<LawerUser> lawerUsers = gson.fromJson(userJson, new TypeToken<List<LawerUser>>() {
        }.getType());
        return lawerUsers;
    }

    /**
     * json转通知集合
     *
     * @param userJson
     * @return
     */
    public static List<Notice> jsonToLawerNoticeList(String userJson) {
        List<Notice> notices = gson.fromJson(userJson, new TypeToken<List<Notice>>() {
        }.getType());
        return notices;
    }

    /**
     * json对象转化成registerData对象
     *
     * @param userJson
     * @return
     */
    public static RegisterData jsonToRegisterData(String userJson) {
        RegisterData registerData = gson.fromJson(userJson, RegisterData.class);
        return registerData;

    }

    /**
     * json数据转consultList
     *
     * @param consultJson
     * @return List<PosttieData>
     */
    public static List<PosttieData> jsonToConsultList(String consultJson) {
        List<PosttieData> consultList = gson.fromJson(consultJson, new TypeToken<List<PosttieData>>() {
        }.getType());
        return consultList;
    }

    /**
     * json数据转replyLawyerData
     *
     * @param replyLawyerJson
     * @return List<ReplyLawyerData>
     */
    public static List<ReplyLawyerData> jsonToReplyLawyerList(String replyLawyerJson) {
        List<ReplyLawyerData> replyLawyerList = gson.fromJson(replyLawyerJson, new TypeToken<List<ReplyLawyerData>>() {
        }.getType());
        return replyLawyerList;
    }

}
