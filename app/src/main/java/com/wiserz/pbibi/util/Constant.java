package com.wiserz.pbibi.util;

/**
 * Created by jackie on 2017/8/9 10:32.
 * QQ : 971060378
 * Used as : 常量类
 */
public class Constant {

    //一般常量
    public static final String DEVICE_IDENTIFIER = "device_identifier";
    public static final String DEVICE_ID = "device_id";
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final String DEVICE_RESOLUTION = "device_resolution";
    public static final String DEVICE_SYS_VERSION = "device_sys_version";
    public static final String DEVICE_TYPE = "device_type";
    public static final int DEVICE_TYPE_ANDROID = 2;
    public static final String DEVICE_ANDROID = "android_";
    public static final String TYPE = "type";
    public static final String MOBILE = "mobile";
    public static final String COMPANY = "company";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String PASSWORD = "password";
    public static final String CARD_FILE = "card_file";
    public static final String TELENUMBER = "telenumber";
    public static final String ADDRESS = "address";
    public static final String CAR_FILE = "car_file";
    public static final String TOKEN = "token";
    public static final String[] TAB_TITLE = new String[]{"账号密码登录", "手机免密登录"};
    public static final String SESSION_ID = "session_id";
    public static final String PASSWORD1 = "password1";
    public static final String PASSWORD2 = "password2";
    public static final String NICKNAME = "nickname";
    public static final String POSITION = "position";
    public static final int PAGE_NUM = 10;//刷新一页的数据
    public static final String IS_ENTER_GUIDE_VIEW = "is_enter_guide_view";
    public static final String IS_USER_LOGIN = "is_user_login";
    public static final String PAGE = "page";
    public static final String ACCOUNT = "account";
    public static final String CHAT_TOKEN = "chat_token";
    public static final String USER_ID = "user_id";
    public static final String MY_USER_INFO = "my_user_info";

    //URL常量
    //    private final static String hostUrl = "https://api.bibicar.cn/";//正式环境
    private static final String hostUrl = "https://testapi.bibicar.cn/";//测试环境

    public static final String registerApp = hostUrl + "app/register";//App注册
    public static final String verificationCodeUrl = hostUrl + "v3/user/sendcode";//得到验证码
    public static final String userRegisterUrl = hostUrl + "v3/user/register";//用户注册
    public static final String userProtocolUrl = "https://api.bibicar.cn/protocol.html";//用户协议

    private static final String userLoginUrl = hostUrl + "v3/user/login";//用户登录
    private static final String newHomeUrl = hostUrl + "v3/theme/homepage";//首页

    public static String getUserLoginUrl() {
        return userLoginUrl;
    }

    public static String getNewHomeUrl() {
        return newHomeUrl;
    }
}