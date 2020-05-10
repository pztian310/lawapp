package com.pxxy.lawconsult.entity;

import java.io.Serializable;

public class Case implements Serializable {
    public static final String ID = "id";
    public static final String IMAGE = "image";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    private int id; //案例id
    private String type;    //案例类型
    private String image;   //案例图片
    private String title;   //案例标题
    private String content; //案例内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
