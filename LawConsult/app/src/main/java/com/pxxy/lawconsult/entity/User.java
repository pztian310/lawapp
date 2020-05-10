package com.pxxy.lawconsult.entity;

import java.io.Serializable;

public class User implements Serializable {
    /**
     *
     */
    public static final String ID = "id";
    public static final String TEL = "tel";
    public static final String PASSWORD = "password";
    public static final String IMAGE = "image";
    public static final String TYPE = "type";

    private int id;
    private String tel;
    private String password;
    private String image;

    public User(int id, String tel, String password, String image, int type) {
        super();
        this.id = id;
        this.tel = tel;
        this.password = password;
        this.image = image;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public User() {
        super();
    }

    private int type;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }
}
