package com.pxxy.lawconsult.constant;


public class AppConstant {
    public static final String USER = "user";
    public static final String USER_COLLECT = "userCollect";
    //loginActivity
    public static final String LOGIN_TERMSTEXT = "登录即代表阅读并同意服务条款";
    public static final String LOGIN_USERNAME_EMPTY = "请输入账号";
    public static final String LOGIN_PASSWORD_EMPTY = "请输入密码";
    public static final String LOGIN_LOADING_DIALOG = "登录中...";
    public static final String LOGIN_FAILURE_TITLE = "登录失败";
    public static final String LOGIN_FAILURE_MESSAGE1 = "用户名或密码错误!";
    public static final String LOGIN_FAILURE_MESSAGE2 = "网络繁忙!";
    public static final String LOGIN_FAILURE_MESSAGE3 = "网络连接失败，请检查网络!";
    public static final String LOGIN_USER_ECHO = "echoUser";
    public static final String LOGIN_USER_TYPE = "type";  //用户类型
    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    // HomeActivity
    public static  final String HOME_ACTIVITY_BACKTOAST = "再按一次退出程序";

    //fragmentHome
    public static final String FRAGMENT_HOME_REFRESH_SUCCESS = "已更新数据";
    public static final String FRAGMENT_HOME_REFRESH_FAILURE = "网络异常，请稍后重试";
    public static final String FRAGMENT_HOME_REFRESH_NODATA = "无新数据可刷新";
    public static final String FRAGMENT_HOME_LOAD_SUCCESS_START = "为你加载";
    public static final String FRAGMENT_HOME_LOAD_SUCCESS_END = "条数据";
    public static final String FRAGMENT_HOME_LOAD_FAILURE = "网络异常，请稍后重试";
    public static final String FRAGMENT_HOME_LOAD_NODATA = "已加载全部数据!";
    public static final String FRAGMENT_HOME_INTENT_CASEDATA = "case";
    public static final String FRAGMENT_HOME_INTENT_CASEPOSITION = "position";
    public static final String FRAGMENT_HOME_SERVER_ERROR = "服务器繁忙!";
    //sp的Key
    public static final String SP_AUTOLOGIN_USER = "user";
    public static final String SP_AUTOLOGIN_TYPE= "type";
    public static final String SP_AUTOLOGIN_ISAUTOLOGIN = "isLogin";
    public static final String SP_CONFIG_CLEARPASSWORD = "clearPassword";
    public static final String SP_AUTOLOGIN_TEL = "tel";
    public static final String SP_AUTOLOGIN_PASSWORD = "password";
    public static final String SP_AUTOLOGIN_PHOTO = "photo";

    //广播action
    public static final String EXIT_APP="com.pxxy.lawconsult.exit_app";

    //showCaseActivity
    public static final String SHOW_CASE_INTERNET_ERROR = "网络连接失败，请检查网络!";
    public static final String SHOW_CASE_ADD_COLLECT_FAILURE = "添加收藏失败";
    public static final String SHOW_CASE_ADD_COLLECT_SUCCESS = "添加收藏成功";
    public static final String SHOW_CASE_DELETE_COLLECT_SUCCESS = "取消收藏成功";
    public static final String SHOW_CASE_DELETE_COLLECT_FAILURE = "取消收藏失败";
    public static final String SHOW_CASE_SERVER_ERROR = "网络繁忙";
    public static final String SHOW_CASE_GET_COLLECT_FAILURE = "获取用户案例失败";

    //adminActivity
    public static final String ADMIN_USER_CLIENTUSER = "客户管理";
    public static final String ADMIN_USER_LAWYERUSER = "律师管理";
    public static final String ADMIN_USER_TYPE = "type";
    public static final String ADMIN_USER_LOAD_CLIENTUSER_END = "客户加载完毕!";
    public static final String ADMIN_USER_LOAD_LAWYERUSER_END = "律师加载完毕!";
    public static final String ADMIN_USER_SHOW_USER_MESSAGE_USEROBJECT = "user";
    public static final String ADMIN_USER_CLIENTUSER_MESSAGE = "客户详情";
    public static final String ADMIN_USER_LAWYERUSER_MESSAGE = "律师详情";
    public static final String ADMIN_USER_CLIENTUSER_MESSAGE_BUTTON = "客户详情";
    public static final String ADMIN_USER_LAWYERUSER_MESSAGE_BUTTON = "律师详情";
    public static final String ADMIN_USER_CLIENTUSER_DELETE_BUTTON = "删除用户";
    public static final String ADMIN_USER_LAWYERUSER_DELETE_BUTTON = "删除律师";
    public static final String ADMIN_USER_DELETE_CANCLE_BUTTON = "取消";
    public static final String ADMIN_USER_DELETE_CONFIRM_BUTTON = "确定";
    public static final String ADMIN_USER_LAWYERUSER_DELETE_BUTTON_HINT_TITLE = "删除律师";
    public static final String ADMIN_USER_CLIENTUSER_DELETE_BUTTON_HINT_TITLE = "删除用户";
    public static final String ADMIN_USER_CLIENTUSER_DELETE_BUTTON_HINT_MESSAGE = "删除用户将会清除用户数据，且无法恢复，确定删除该用户？";
    public static final String ADMIN_USER_LAWYERUSER_DELETE_BUTTON_HINT_MESSAGE = "删除律师将会清除律师数据，且无法恢复，确定删除该律师？";
    public static final String ADMIN_USER_CLIENTUSER_DELETE_FAILURE = "用户删除失败!";
    public static final String ADMIN_USER_CLIENTUSER_DELETE_SUCCESS = "用户删除成功!";
    public static final String ADMIN_USER_LAWYERUSER_DELETE_SUCCESS = "律师删除成功!";
    public static final String ADMIN_USER_LAWYERUSER_DELETE_FAILURE = "律师删除失败!";


    //adminAddUserActivity
    public static final String ADMIN_USER_ADDUSER_TOAST_TEL = "手机号不能为空";
    public static final String ADMIN_USER_ADDUSER_TOAST_PASSWORD = "密码不能为空";
    public static final String ADMIN_USER_ADDUSER_TOAST_NOT_INTERNET = "网络连接失败，请检查网络连接!";
    public static final String ADMIN_USER_ADDUSER_TOAST_SERVER_ERROR = "网络繁忙!";
    public static final String ADMIN_USER_ADDUSER_DIALOG_FAILURE_TITLE = "添加失败";
    public static final String ADMIN_USER_ADDUSER_DIALOG_SUCCESS_TITLE = "添加成功";
    public static final String ADMIN_USER_ADDUSER_DIALOG_FAILURE_MESSAGE = "用户已存在，请检查账号!";
    public static final String ADMIN_USER_ADDUSER_SUCCESS = "用户添加成功!";
    public static final String ADMIN_USER_ADDUSER_FAILURE = "用户添加失败，请稍后重试!";
    public static final String ADMIN_USER_ADDUSER_CANCLE_BUTTON = "取消";
    public static final String ADMIN_USER_ADDUSER_CONFIRM_BUTTON = "确定";

    //adminCaseRecyclerAdapter
    public static final String ADMIN_CASE_SHOW_CASE_BT_UPDATE = "修改案例";
    public static final String ADMIN_CASE_SHOW_CASE_BT_DELETE = "删除案例";

    //adminCaseActivity
    public static final String ADMIN_CASE_ACTIVITY_SERVER_ERROR = "服务器繁忙!";
    public static final String ADMIN_CASE_ACTIVITY_NOT_INTERNET = "网络连接失败，请检查网络连接!";
    public static final String ADMIN_CASE_ACTIVITY_NO_CASE_DATA = "已加载全部案例!";
    public static final String ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_ITEM = "案例详情";
    public static final String ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_TYPE = "type";
    public static final String ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_CASE = "case";
    public static final String ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_POSITION = "position";
    public static final String ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_UPDATE = "修改案例";
    public static final String ADMIN_CASE_ACTIVITY_UPDATE_CASE_IS_EMPTY= "修改后的数据不能为空!";
    public static final String ADMIN_CASE_ACTIVITY_UPDATE_CASE_SUCCESS ="案例数据修改成功!";
    public static final String ADMIN_CASE_ACTIVITY_UPDATE_CASE_FAILURE = "案例数据修改失败，请稍后重试!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_CASE_SUCCESS = "案例删除成功!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_CASE_FAILURE = "案例删除失败，请稍后重试!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_CASE_NOT_INTERNET = "网络连接失败，请检查网络连接!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_DIALOG_TITLE = "删除案例";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_DIALOG_MESSAGE = "删除案例将会清除该案例的所有数据，且无法恢复，确定删除该案例？";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_DIALOG_CANCLEBUTTON = "取消";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_DIALOG_CONFIRMBUTTON = "确定";



    //adminShowCaseActivity
    public static final String ADMIN_SHOW_CASE_ACTIVITY_TITLE = "标题";
    public static final String ADMIN_SHOW_CASE_ACTIVITY_TYPE = "类型";
    public static final String ADMIN_SHOW_CASE_ACTIVITY_IMAGE = "图片(URL)";
    public static final String ADMIN_SHOW_CASE_ACTIVITY_CONTENT = "内容(HTML)";
    //adminAddCaseActivity
    public static final String ADMIN_ADD_CASE_ACTIVITY_HINT_TITLE = "请输入案例标题";
    public static final String ADMIN_ADD_CASE_ACTIVITY_HINT_TYPE = "请输入案例类型";
    public static final String ADMIN_ADD_CASE_ACTIVITY_HINT_IMAGE = "请输入案例图片(图片URL)";
    public static final String ADMIN_ADD_CASE_ACTIVITY_HINT_CONTENT = "请输入案例内容(HTML地址)";

    public static final String ADMIN_ADD_CASE_ACTIVITY_DATA_CAN_NOT_BE_EMPTY = "输入框中内容不能为空!";
    public static final String ADMIN_ADD_CASE_ACTIVITY_NOT_INTERNET = "网络连接失败，请检查网络连接!";

    public static final String ADMIN_ADD_CASE_ACTIVITY_DIALOG_SERVER_ERROR_MESSAGE = "案例添加失败，请稍后重试!";
    public static final String ADMIN_ADD_CASE_ACTIVITY_DIALOG_FAILURE_MESSAGE = "服务器繁忙，请稍后重试!";
    public static final String ADMIN_ADD_CASE_ACTIVITY_DIALOG_TITLE = "添加案例";
    public static final String ADMIN_ADD_CASE_ACTIVITY_DIALOG_SUCCESS_MESSAGE = "案例添加成功";
    public static final String ADMIN_ADD_CASE_ACTIVITY_DIALOG_BUTTON = "确定";

    //adminShowUserActivity
    public static final String ADMIN_SHOW_USER_BASIC_USER_ID = "用户id";
    public static final String ADMIN_SHOW_USER_BASIC_USER_TEL = "手机号";
    public static final String ADMIN_SHOW_USER_BASIC_USER_PASSWORD = "用户密码";
    public static final String ADMIN_SHOW_USER_BASIC_USER_PHOTO = "头像地址";
    public static final String ADMIN_SHOW_USER_BASIC_USER_TYPE = "用户类型";


    //adminShowUserActivity客户对象
    public static final String ADMIN_SHOW_USER_CLIENT_USER_NICKNAME = "昵称";
    public static final String ADMIN_SHOW_USER_CLIENT_USER_SIGNATURE = "个性签名";
    public static final String ADMIN_SHOW_USER_CLIENT_USER_NAME = "姓名";
    public static final String ADMIN_SHOW_USER_CLIENT_USER_SEX = "性别";
    public static final String ADMIN_SHOW_USER_CLIENT_USER_EMAIL = "邮箱";

    //adminShowUserActivity律师对象
    public static final String ADMIN_SHOW_USER_LAWER_USER_NAME = "姓名";
    public static final String ADMIN_SHOW_USER_LAWER_USER_AGE = "年龄";
    public static final String ADMIN_SHOW_USER_LAWER_USER_SEX = "性别";
    public static final String ADMIN_SHOW_USER_LAWER_USER_EMAIL = "邮箱";
    public static final String ADMIN_SHOW_USER_LAWER_USER_NUMBER = "律师证编号";
    public static final String ADMIN_SHOW_USER_LAWER_USER_SPECIALITY = "擅长类别";
    public static final String ADMIN_SHOW_USER_LAWER_USER_OFFICE = "单位";
    public static final String ADMIN_SHOW_USER_LAWER_USER_INTRODUCTION = "介绍";
    public static final String ADMIN_SHOW_USER_LAWER_USER_ADDRESS = "地址";

    //adminStatuteActivity
    public static final String ADMIN_SHOW_CHILD_STATUTE_UPDATE_BT = "修改法条";
    public static final String ADMIN_SHOW_CHILD_STATUTE_DELETE_BT = "删除法条";
    public static final String ADMIN_STATUTE_ACTIVITY_SERVER_ERROR = "服务器繁忙!";
    public static final String ADMIN_STATUTE_ACTIVITY_NO_STATUTE_DATA = "已加载全部法条!";
    public static final String ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_TITLE = "删除法条";
    public static final String ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_MESSAGE = "删除法条将会清除该法条的所有数据，且无法恢复，确定删除该法条？";
    public static final String ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_CANCLEBUTTON = "取消";
    public static final String ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_CONFIRMBUTTON = "确定";
    public static final String ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_TYPE = "type";
    public static final String ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_STATUTE = "statute";
    public static final String ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_POSITION = "position";
    public static final String ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_ITEM = "法条详情";
    public static final String ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_UPDATE = "修改法条";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_STATUTE_SUCCESS = "案例删除成功!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_STATUTE_FAILURE = "案例删除失败，请稍后重试!";
    public static final String ADMIN_CASE_ACTIVITY_DELETE_STATUTE_NOT_INTERNET = "网络连接失败，请检查网络连接!";

    //adminShowStatuteActivity
    public static final String ADMIN_SHOW_STATUTE_ACTIVITY_TITLE = "标题";
    public static final String ADMIN_SHOW_STATUTE_ACTIVITY_TYPE = "类型";
    public static final String ADMIN_SHOW_STATUTE_ACTIVITY_ID = "法条id";
    public static final String ADMIN_SHOW_STATUTE_ACTIVITY_CONTENT = "内容";
    public static final String ADMIN_SHOW_STATUTE_ACTIVITY_OPENDATE = "发布时间";
    public static final String ADMIN_STATUTE_ACTIVITY_UPDATE_STATUTE_FAILURE = "法条数据修改失败，请稍后重试!";
    public static final String ADMIN_STATUTE_ACTIVITY_UPDATE_SERVER_ERROR = "网络繁忙，请稍后重试!";
    public static final String ADMIN_STATUTE_ACTIVITY_UPDATE_STATUTE_SUCCESS ="法条数据修改成功!";


    //admin退出
    public static final String ADMIN_EXIT_DIALOG_TITLE = "管理员退出";
    public static final String ADMIN_EXIT_DIALOG_MESSAGE = "是否退出当前管理员账号？";
    public static final String ADMIN_EXIT_DIALOG_CANCLEBUTTON = "取消";
    public static final String ADMIN_EXIT_DIALOG_CONFIGBUTTON = "确定";
    //FragmentLawyer
    public static final String FRAGMENT_LAWYER_REFRESH_SUCCESS = "已更新数据";
    public static final String FRAGMENT_LAWYER_REFRESH_FAILURE = "网络异常，请稍后重试";
    public static final String FRAGMENT_LAWYER_REFRESH_NODATA = "无新数据可刷新";
    public static final String FRAGMENT_LAWYER_LOAD_SUCCESS_START = "为你加载";
    public static final String FRAGMENT_LAWYERR_LOAD_SUCCESS_END = "条数据";
    public static final String FRAGMENT_LAWYER_LOAD_FAILURE = "网络异常，请稍后重试";
    public static final String FRAGMENT_LAWYER_LOAD_NODATA = "已加载全部数据!";
    public static final String FRAGMENT_LAWYER_INTENT_LAWERUSERDATA = "lawerUser";
    public static final String FRAGMENT_LAWYER_SERVER_ERROR = "服务器繁忙!";

    //SearchActivity
    public static final String SEARCH_ACTIVITY_REFRESH_SUCCESS="已更新数据";
    public static final String SEARCH_ACTIVITY_REFRESH_FAILURE="网络异常，请稍后重试";
    public static final String SEARCH_ACTIVITY_REFRESH_NODATA="无新数据可刷新";
    public static final String SEARCH_ACTIVITY_LOAD_SUCCESS_START="为你加载";
    public static final String SEARCH_ACTIVITY_LOAD_SUCCESS_END="条数据";
    public static final String SEARCH_ACTIVITY_LOAD_FAILURE="网络异常，请稍后重试";
    public static final String SEARCH_ACTIVITY_LOAD_NODATA="已加载全部数据";
    public static final String SEARCH_ACTIVITY_INTENT_STATUTEDATA="statute";
    public static final String SEARCH_ACTIVITY_SERVER_ERROR="服务器繁忙";

    //申请律师需要用到的key
    public static final  String APPLYLAWYER_NAME="lawerName";
    public static final  String APPLYLAWYER_USERID="lawerId";
    public static final  String APPLYLAWYER_AGE="lawerAge";
    public static final  String APPLYLAWYER_Sex="lawerSex";
    public static final  String APPLYLAWYER_EMAIL="email";
    public static final  String APPLYLAWYER_NUMBER="number";
    public static final  String APPLYLAWYER_SPECIALITY="speciality";
    public static final  String APPLYLAWYER_OFFICE="office";
    public static final  String APPLYLAWYER_INTRODUCTION="introduction";
    public static final  String APPLYLAWYER_ADDRESS="address";

    public static  final String APPLYLAWYER_SUCCESS="success";
    public static  final String APPLYLAWYER_RESULT="applyLawerState";
    public static  final String APPLYLAWYER_FAILURE="failure";
    public static  final String APPLYLAWYER_SERVER_ERROR="servererror";


}
