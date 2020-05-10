package com.pxxy.lawconsult.entity;


import java.io.Serializable;

public class Statute implements Serializable {

    public static final String LAW_ID = "law_id";
    public static final String LAW_TYPE = "law_type";
    public static final String LAW_TITLE = "law_title";
    public static final String LAW_CONTENT = "law_content";
    public static final String OPENDATE = "opendate";

    private int law_id;//法条id
    private String law_type;//法条类型
    private String law_title;//法条标题
    private String law_content;//法条内容
    private String opendate;//法条发布时间

    public String getOpendate() {
        return opendate;
    }

    public void setOpendate(String opendate) {
        this.opendate = opendate;
    }

    public int getLaw_id() {
        return law_id;
    }

    public void setLaw_id(int law_id) {
        this.law_id = law_id;
    }

    public String getLaw_type() {
        return law_type;
    }

    public void setLaw_type(String law_type) {
        this.law_type = law_type;
    }

    public String getLaw_title() {
        return law_title;
    }

    public void setLaw_title(String law_title) {
        this.law_title = law_title;
    }

    public String getLaw_content() {
        return law_content;
    }

    public void setLaw_content(String law_content) {
        this.law_content = law_content;
    }


}
