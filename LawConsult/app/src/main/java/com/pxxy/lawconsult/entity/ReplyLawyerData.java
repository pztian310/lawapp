package com.pxxy.lawconsult.entity;

public class ReplyLawyerData {
    private String LAWYERAVATAR = "image";
    private String LAWYERNAME = "lawer_name";
    private String LAWYEROFFICE = "office";
    private String REPLYDATE = "H_date";
    private String REPLYCONTENT = "H_content";

    public ReplyLawyerData(String image, String name, String office, String date, String content) {
        this.LAWYERAVATAR = image;
        this.LAWYERNAME = name;
        this.LAWYEROFFICE = office;
        this.REPLYDATE = date;
        this.REPLYCONTENT = content;
    }

    public String getLAWYERAVATAR() {
        return LAWYERAVATAR;
    }

    public String getLAWYERNAME() {
        return LAWYERNAME;
    }

    public String getLAWYEROFFICE() {
        return LAWYEROFFICE;
    }

    public String getREPLYDATE() {
        return REPLYDATE;
    }

    public String getREPLYCONTENT() {
        return REPLYCONTENT;
    }
}
