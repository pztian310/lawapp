package com.pxxy.lawconsult.entity;

import java.io.Serializable;

public class LawerUser extends User implements Serializable {
	/**
	 *
	 */
	public static final String LAWERID="lawerId";
	public static final String LAWERNAME="lawerName";
	public static final String LAWERAGE="lawerAge";
	public static final String LAWERSEX="lawerSex";
	public static final String EMAIL="email";
	public static final String NUMBER="number";
	public static final String SPECIALITY="speciality";
	public static final String OFFICE="office";
	public static final String INTRODUCTION="introduction";
	public static final String ADDRESS="address";
	public static final String USERID = "userId";
	public static final String BIRTHDAY = "birthday";

	private int lawerId; // 律师id
	private String lawerName; // 律师姓名
	private int lawerAge; // 律师年龄
	private int lawerSex; // 性别，男（1），女（0）
	private String email; // 邮箱
	private String number; // 律师编号
	private String speciality; // 擅长类别
	private String office; // 单位
	private String introduction; // 律师介绍
	private String address;//地址
	private String birthday;//生日

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getLawerId() {
		return lawerId;
	}

	public void setLawerId(int lawerId) {
		this.lawerId = lawerId;
	}

	public String getLawerName() {
		return lawerName;
	}

	public void setLawerName(String lawerName) {
		this.lawerName = lawerName;
	}

	public int getLawerAge() {
		return lawerAge;
	}

	public void setLawerAge(int lawerAge) {
		this.lawerAge = lawerAge;
	}

	public int getLawerSex() {
		return lawerSex;
	}

	public void setLawerSex(int lawerSex) {
		this.lawerSex = lawerSex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getBithday()
	public void  setBirthday(String  birthday) {this.birthday = birthday;}




}
