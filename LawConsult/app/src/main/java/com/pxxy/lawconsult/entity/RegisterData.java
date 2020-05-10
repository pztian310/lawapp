package com.pxxy.lawconsult.entity;

public class RegisterData {
    public static final String REGISTERSTATE_SUCCESS = "success";// json:注册标签:value,成功
    public static final String REGISTERSTATE_FAILURE = "failure";// json:注册标签:value,失败
    public static final String REGISTERSTATE_FAILURE_USEREXIST = "userExist";
    public static final String REGISTERSTATE_FAILURE_SERVER_ERROR = "serverError";// json:注册标签:value,服务器错误

    private String registerState;
    private ClientUser data;

    public String getRegisterState() {
        return registerState;
    }

    public ClientUser getData() {
        return data;
    }

    public void setData(ClientUser data) {
        this.data = data;
    }

    public void setRegisterState(String registerState) {
        this.registerState = registerState;
    }

}
