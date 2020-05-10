package com.pxxy.lawconsult.entity;

public class ClientUser extends User{
	/**
	 * 
	 */
	private int clientId; // 客户ID
	private String nickName; // 昵称
	private String signature; // 个性签名
	private int clientSex; // 性别
	private String email; // 电子邮箱

	public ClientUser(int clientId, String nickName, String signature, int clientSex, String email) {
		super();
		this.clientId = clientId;
		this.nickName = nickName;
		this.signature = signature;
		this.clientSex = clientSex;
		this.email = email;
	}

	public ClientUser() {
		super();
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getClientSex() {
		return clientSex;
	}

	public void setClientSex(int clientSex) {
		this.clientSex = clientSex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
