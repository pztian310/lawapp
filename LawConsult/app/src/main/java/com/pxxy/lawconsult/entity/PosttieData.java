package com.pxxy.lawconsult.entity;

import java.io.Serializable;

public class PosttieData implements Serializable {
    private int T_ID;
    private String userAvatar;
    private String nickName;
    private String date;
    private String content;
    public PosttieData(int t_ID,String userAvatar,String nickName,String content,String date) {
        this.T_ID = t_ID;
        this.userAvatar = userAvatar;
        this.nickName = nickName;
        this.content = content;
        this.date = date;
    }
    public String getUserAvatar() {
        return userAvatar;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDate() {
        return date;
    }
    public String getContent() {
        return content;
    }
    public int getT_ID() {
        return T_ID;
    }
}
