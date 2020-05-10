package com.pxxy.lawconsult.entity;

public class LoginData {
	// 登录状态
	public static final String LOGINSTATE_SUCCESS = "success";
	public static final String LOGINSTATE_FAILURE = "failure";
	public static final String LOGINSTATE_FAILURE_SERVER_ERROR = "serverError";

	private String loginState;
	private ClientUser clientUser;
	private LawerUser lawerUser;
	private Integer type;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	public ClientUser getClientUser() {
		return clientUser;
	}

	public void setClientUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	public LawerUser getLawerUser() {
		return lawerUser;
	}

	public void setLawerUser(LawerUser lawerUser) {
		this.lawerUser = lawerUser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
