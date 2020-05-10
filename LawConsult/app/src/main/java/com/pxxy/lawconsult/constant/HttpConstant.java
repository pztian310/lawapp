package com.pxxy.lawconsult.constant;


public class HttpConstant {
    //服务器端servlet
    public static final String SERVER_LOGINSERVLET = "http://120.77.205.137/Law/LoginServlet";
    public static final String SERVER_REGISTERSERVLET = "http://120.77.205.137/Law/RegisterServlet";
    public static final String SERVER_FINDPASSWORD = "http://120.77.205.137/Law/FindPasswordServlet";
    public static final String SERVER_ISUSEREXIST = "http://120.77.205.137/Law/IsUserExistServlet";
    public static final String SERVER_GETUSERPHOTO = "http://120.77.205.137/Law/GetUserPhotoServlet";
    public static final String SERVER_GETCASE = "http://120.77.205.137/Law/GetCaseServlet";
    public static final String SERVER_SAVECONSULT = "http://120.77.205.137/Law/SaveConsultServlet";
    public static final String SERVER_GET_USER_COLLECTSERVLET = "http://120.77.205.137/Law/GetUserCollectServlet";
    public static final String SERVER_ADD_USER_COLLECTSERVLET = "http://120.77.205.137/Law/AddUserCollectServlet";
    public static final String SERVER_DELETE_USER_COLLECTSERVLET = "http://120.77.205.137/Law/DeleteUserCollectServlet";
    public static final String SERVER_GETCONSULTCONTENT = "http://120.77.205.137/Law/GetConsultServlet";
    public static final String SERVER_GETLAWERUSER = "http://120.77.205.137/Law/GetLawyerServlet";
    public static final String SERVER_GET_NOTICE_SERVLET = "http://120.77.205.137/Law/GetNoticeServlet";
    public static final String SERVER_ISLAWYER="http://120.77.205.137/Law/IsLawyerServlet";
    public static final String SERVER_GETREPLYLAWYER="http://120.77.205.137/Law/GetReplyLawyerServlet";
    public static final String SERVER_SAVELAWYERREPLY="http://120.77.205.137/Law/SaveLawyerReplyServlet";
    public static final String SERVER_LAWYERBANNER="http://120.77.205.137/Law/LawyerBannerServlet";
    public static  final String SERVER_SETUSERPHOTO="http://120.77.205.137/Law/UploadUserPhotoServlet";
    public static  final String SERVER_SETNICKNAME="http://120.77.205.137/Law/SetNickNameServlet";
    public static  final String SERVER_GETNICKNAME="http://120.77.205.137/Law/GetNickNameServlet";
    public static final String SERVER_GETSTATUTE="http://120.77.205.137/Law/GetStatuteServlet";
    /*public static final String SERVER_GETMYPUBLISH="http://120.77.205.137/Law/GetMyPublishServlet";*/
    public static final String SERVER_GETFuzzySTATUTE="http://120.77.205.137/Law/GetStartsContentServlet";
    public static final String SERVER_GET_USER_CASESERVLET = "http://120.77.205.137/Law/GetUserCaseServlet";
    public static final String SERVER_APPLY_LAWYER = "http://120.77.205.137/Law/ApplyLawerServlet";
    public static final String SERVER_USERTOAPPWER="http://120.77.205.137/Law/GetPrivateConsultServlet";
    public static final String SERVER_BYCONSULTING = "http://120.77.205.137/Law/Byconsulting";


    //服务端adminServlet
    public static final String SERVER_ADMIN_GETCLIENT_USER_SERVLET = "http://120.77.205.137/Law/AdminGetAllClientUserServlet";
    public static final String SERVER_ADMIN_GETLAWYER_USER_SERVLET = "http://120.77.205.137/Law/AdminGetAllLawyerUserServlet";
    public static final String SERVER_ADMIN_DELETE_USER_SERVLET = "http://120.77.205.137/Law/AdminDeleteUserServlet";
    public static final String SERVER_ADMIN_GETCASE_SERVLET = "http://120.77.205.137/Law/AdminGetCaseServlet";
    public static final String SERVER_ADMIN_UPDATE_CASE = "http://120.77.205.137/Law/AdminUpdateCaseServlet";
    public static final String SERVER_ADMIN_DELETE_CASE = "http://120.77.205.137/Law/AdminDeleteCaseServlet";
    public static final String SERVER_ADMIN_ADD_CASE = "http://120.77.205.137/Law/AdminAddCaseServlet";
    public static final String SERVER_ADMIN_GET_STATUTE_SERVLET = "http://120.77.205.137/Law/AdminGetStatureServlet";
    public static final String SERVER_ADMIN_UPDATE_STATUTE = "http://120.77.205.137/Law/AdminUpdateStatuteServlet";
    public static final String SERVER_ADMIN_DELETE_STATUTE = "http://120.77.205.137/Law/AdminDeleteStatuteServlet";



    //    updateCaseActivity
    public static final String ADMIN_UPDATE_CASE = "updateCase";
    public static final String ADMIN_UPDATE_CASE_SUCCESS = "success";
    public static final String ADMIN_UPDATE_CASE_FAILURE = "failure";

    //deleteCase
    public static final String ADMIN_DELETECASE_RESULT = "result";
    public static final String ADMIN_DELETECASE_RESULT_SUCCESS = "success";
    public static final String ADMIN_DELETECASE_RESULT_FAILURE = "failure";

    //deleteStatue
    public static final String ADMIN_DELETESTATUTE_RESULT = "result";
    public static final String ADMIN_DELETESTATUTE_RESULT_SUCCESS = "success";
    public static final String ADMIN_DELETESTATUTE_RESULT_FAILURE = "failure";

    //adminAddCase
    public static final String ADMIN_ADD_CASE_RESULT = "addCaseResult";
    public static final String ADMIN_ADD_CASE_RESULT_SUCCESS = "success";
    public static final String ADMIN_ADD_CASE_RESULT_FAILURE = "failure";

    //admin获取case
    public static final String ADMIN_GETCASE_CASEINDEX = "caseIndex";

    //admin获取法条
    public static final String ADMIN_GETSTATURE_STATUREINDEX = "statureIndex";



    //LoginActivity网络常量
    public static final String LOGIN = "login";//json登录状态:key
    public static final String LOGIN_SUCCESS = "success";// json登录状态:value
    public static final String LOGIN_FAILURE = "failure";// json登录状态:value
    public static final String LOGIN_FAILURE_SERVER_ERROR = "serverError";// json登录状态:value
    public static final String LOGIN_DATA = "data";   //json数据:key


    //RegisterActivity网络常量
    public static final String REGISTER = "register";// json注册标签:key
    public static final String REGISTER_SUCCESS = "success";// json:注册标签:value
    public static final String REGISTER_FAILURE = "failure";//  json:注册标签:value
    public static final String REGISTER_FAILURE_SERVER_ERROR = "serverError";// json:注册标签:value

    public static final String REGISTER_IS_REGISTER = "isRegister";// json账号是否存在:key
    public static final boolean EXIST = true;// json账号是否存在:value
    public static final boolean NOT_EXIST = false;// json账号是否存在:value

    public static final String REGISTER_DATA = "data";// json数据:key

    //FindPasswordActivity网络常量
    public static final String FIND_PASSWORD = "findPassword";// json找回密码标签:key
    public static final String FINDPASSWORD_SUCCESS = "success";// json找回密码标签:value
    public static final String FINDPASSWORD_FAILURE = "failure";// json找回密码标签:value
    public static final String FINDPASSWORD_NOTFIND = "notFind";// json找回密码标签:value
    public static final String FINDPASSWORD_FAILURE_SERVER_ERROR = "serverError";// json找回密码标签:value

    //获取头像
    public static final String GETUSERPHOTO = "getUserPhoto";//Json获取头像:key
    public static final String GETUSERPHOTO_SUCCESS = "success";//json获取头像:value,成功
    public static final String GETUSERPHOTO_FAILURE = "failure";//json获取头像:value,失败
    public static final String GETUSERPHOTO_SERVER = "serverError";//json获取头像:value,服务器错误

    public static final String GETUSERPHOTO_DATA = "data";//json数据:key


    //判断用户是否存在
    public static final String IS_USEREXIST = "isUserExist";// json账号是否存在:key
    public static final String IS_USEREXIST_EXIST = "exist";// json账号是否存在:value
    public static final String IS_USEREXIST_NOT_EXIST = "notExist";// json账号是否存在:value
    public static final String IS_USEREXIST_SERVERERROR = "serverError";//json账号是否存在:value

    //获取案例
    public static final String GETCASE_NODATA = "noData";
    //发布咨询
    public static final String CONSULT_IS_SAVESUCCESS = "isSaveSuccess";//json 保存发布:key
    public static final String CONSULT_SAVESUCCESS = "saveSuccess"; //json 保存成功:value
    public static final String CONSULT_SAVEFAIL = "saveFail"; //json 保存失败:value
    public static final String CONSULT_SERVERERRROR = "serverError"; //json 保存发布:服务器出错

    //添加收藏
    public static final String ADD_COLLECT_USER_ID = "userId";
    public static final String ADD_COLLECT_CASE_ID = "caseId";
    public static final String ADD_COLLECT_RESULT = "addCollect";
    public static final String ADD_COLLECT_RESULT_SUCCESS = "success";
    public static final String ADD_COLLECT_RESULT_FAILURE = "failure";
    public static final String ADD_COLLECT_RESULT_SERVER_ERROR = "serverError";

    //删除收藏
    public static final String DELETE_COLLECT_USER_ID = "userId";
    public static final String DELETE_COLLECT_CASE_ID = "caseId";
    public static final String DELETE_COLLECT_RESULT = "deleteCollect";
    public static final String DELETE_COLLECT_RESULT_SUCCESS = "success";
    public static final String DELETE_COLLECT_RESULT_FAILURE = "failure";
    public static final String DELETE_COLLECT_RESULT_SERVER_ERROR = "serverError";

    //获取收藏
    public static final String GETCOLLECT_USER_ID = "userId";

    //获取发布咨询内容
    public static final String GETCONSULT = "getConsult"; //json 获取发布内容:key
    public static final String GETCONSULT_SUCCESS = "getConsultSuccess";//json 获取成功:value
    public static final String GETCONSULT_FAIL = "getConsultFail"; //json 获取数据失败:
    public static final String GETCONSULT_SERVERERROR = "serverError"; //json 服务器出错

    //获取律师
    public static final String GETLAWERDATA = "data";
    //律师
    public static final String GETLAWER = "getLawer";//Json获取律师数据:key
    public static final String GETLAWER_SUCCESS = "getLawerSuccess";//json获取律师数据:value,成功
    public static final String GETLAWER_FAILURE = "getLawerFailure";//json获取律师数据：value,失败
    public static final String GETLAWER_SERVERERROR = "servererror";//json获取律师数据；value,服务器错误

    //adminGetUser
    public static final String ADMIN_USER_INDEX = "userIndex";

    //adminDeleteUser
    public static final String ADMIN_DELETEUSER_RESULT = "result";
    public static final String ADMIN_DELETEUSER_RESULT_SUCCESS = "success";
    public static final String ADMIN_DELETEUSER_RESULT_FAILURE = "failure";

    //updateStatute
    public static final String ADMIN_UPDATE_STATUTE = "updateStatute";
    public static final String ADMIN_UPDATE_STATUTE_SUCCESS = "success";
    public static final String ADMIN_UPDATE_STATUTE_FAILURE = "failure";


    //上传头像
    public static final String SETUSERPHOTO = "setUserPhoto";//Json获取头像:key
    public static final String SETUSERPHOTO_SUCCESS = "success";//json获取头像:value,成功
    public static final String SETUSERPHOTO_FAILURE = "failure";//json获取头像:value,失败
    public static final String SETUSERPHOTO_SERVER = "serverError";//json获取头像:value,服务器错误

    //修改昵称和个性签名signature
    public static final String SETPICKNAME="setPickName";
    public static final String SETNAME="name";
    public static final String SETSIGNATURE="signature";
    public static final String SETPICKNAME_SUCCESS = "success";//json获取头像:value,成功
    public static final String SETPICKNAME__FAILURE = "failure";//json获取头像:value,失败
    public static final String SETPICKNAME__SERVER = "serverError";//json获取头像:value,服务器错误


    //获取昵称和个性签名
    public static final String GETNICKNAME="getNickName";
    public static final String GETNICKNAME_NAME="name";
    public static final String GETNICKNAME_SIGNATURE="signature";

    public static final String GETPICKNAME_SUCCESS = "success";// json上传数据:value,成功
    public static final String GETPICKNAME__FAILURE = "failure";// json上传数据:value,失败
    public static final String GETPICKNAME__SERVER = "serverError";// json上传数据:value,服务器错误

    public static final String GETLAWERNODATA="nodata";

    //获取法条
    public static final String GETSTATUTE_NODATA="nodata";
    //法条的内容
    public static final String GETSTATUTE="getstatute";//Json获取法条数据：key
    public static final String GETSTATUTE_SUCCESS="getStatuteSuccess";//Json获取法条数据:value,成功
    public static final String GETSTATUTE_FAILURE="getStatuteFailure";//Json获取法条数据：value,失败
    public static final String GETSTATUTE_SERVERERROR="servererror";//Json获取法条数据；value,服务器错误
}
