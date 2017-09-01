package com.wiserz.pbibi.util;

/**
 * Created by jackie on 2017/8/9 10:32.
 * QQ : 971060378
 * Used as : 常量类
 */
public class Constant {

    //一般常量
    public static final String APP_ID = "wx8bac6dd603d47d15";
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
    public static final String CAR_ID = "car_id";
    public static final String RENTAL_TIME_START = "rental_time_start";
    public static final String RENTAL_TIME_END = "rental_time_end";
    public static final String PAY_TYPE = "pay_type";
    public static final String ORDER_SN = "order_sn";
    public static final String KEY_WORD = "keyword";
    public static final String ORDER_ID = "order_id";
    public static final String BRAND_ID = "brand_id";
    public static final String SERIES_ID = "series_id";
    public static final String MIN_PRICE = "min_price";
    public static final String MAX_PRICE = "max_price";
    public static final String MIN_MILEAGE = "min_mileage";
    public static final String MAX_MILEAGE = "max_mileage";
    public static final String MIN_BOARD_TIME = "min_board_time";
    public static final String MAX_BOARD_TIME = "max_board_time";
    public static final String HAS_VR = "has_vr";
    public static final String OLD = "old";
    public static final String SOURCE = "source";
    public static final String FEED_ID = "feed_id";
    public static final String IS_AUTH = "is_auth";//是否身份验证 1:是 2:否
    public static final String THEME = "theme";
    public static final String POST_FILE = "post_file";
    public static final String TITLE = "title";
    public static final String MONEY = "money";
    public static final String AVATAR = "avatar";
    public static final String COMPANY_ID = "company_id";
    public static final String THEME_ID = "theme_id";
    public static final String PROVINCE_ID = "province_id";
    public static final String ADVERTISEMENT_IMAGE_SUCCESS = "advertisement_image_success";
    public static final String ADVERTISEMENT_IMAGE = "advertisement_image";
    public static final String ADVERTISEMENT_IMAGE_NUM = "advertisement_image_num";
    public static final String HOST_IMAGE_URL = "http://img.bibicar.cn/";//http://img.bibicar.cn/Fja6zKmICAz6l6QCd7-Z0pC-bjt8

    //URL常量
    //    private final static String hostUrl = "https://api.bibicar.cn/";//正式环境
    private static final String hostUrl = "https://testapi.bibicar.cn/";//测试环境

    private static final String registerApp = hostUrl + "app/register";//App注册
    private static final String verificationCodeUrl = hostUrl + "v3/user/sendcode";//得到验证码
    private static final String forgetPasswordUrl = hostUrl + "v3/user/forgetpassword";//忘记密码
    private static final String userRegisterUrl = hostUrl + "v3/user/register";//用户注册
    private static final String userProtocolUrl = "https://api.bibicar.cn/protocol.html";//用户协议
    private final static String oauthRegisterUrl = hostUrl + "v3/user/oauthregister";//第三方注册
    private static final String oauthLoginUrl = hostUrl + "v3/user/oauthlogin";//第三方登录

    private static final String splashUrl = hostUrl + "app/getimg";//启动页
    private static final String userLoginUrl = hostUrl + "v3/user/login";//用户登录
    private static final String newHomeUrl = hostUrl + "v3/theme/homepage";//首页

    private final static String carRentListUrl = hostUrl + "v3/rentalcar/list";//可租车辆列表
    private final static String createCarRentOrderUrl = hostUrl + "v3/rentalcar/createrentalorder";//创建租车订单
    private final static String createCarRentPayUrl = hostUrl + "v3/rentalcar/rentalorderpay";//创建租车支付
    private static final String rentCarDetailUrl = hostUrl + "v3/rentalcar/index";//租车详情
    private static final String myCarRentUrl = hostUrl + "v3/rentalcar/rentalorderlist";//我的租车

    private final static String uploadTokenUrl = hostUrl + "app/uploadtoken";//上传文件的Token

    private final static String carListUrl = hostUrl + "v4/car/list";//车辆列表
    private final static String carDetailUrl = hostUrl + "v4/car/index";//车辆详情  v4接口

    private final static String videoListUrl = hostUrl + "v3/Video/list";//视频列表

    private final static String articleListUrl = hostUrl + "v3/article/list";//文章列表
    private final static String articleIndexUrl = hostUrl + "v3/article/index";
    private final static String articleSearchUrl = hostUrl + "v3/Article/searcharticlelist";//搜索文章
    private static final String articleCommentListUrl = hostUrl + "v3/article/commentlist";//文章评论列表

    private final static String userSearchUrl = hostUrl + "v3/user/search";//用户搜索

    private final static String topicSearchUrl = hostUrl + "v3/theme/searchtheme";//话题搜索

    private static final String cheHangListUrl = hostUrl + "v3/publishcar/getcompanylist";//车行列表

    private static final String createTopicUrl = hostUrl + "v3/theme/createtheme";//创建话题
    private static final String hallUrl = hostUrl + "v3/theme/themehome";//大厅
    private static final String topicListUrl = hostUrl + "v3/theme/list";//话题列表
    private static final String topicDetailUrl = hostUrl + "v3/theme/themeindex";//话题详情
    private static final String myFocusUrl = hostUrl + "v3/theme/myfocus";//我的关注

    private static final String balanceUrl = hostUrl + "v3/UserCarPact/getusermoney";//获取余额
    private static final String takeCashUrl = hostUrl + "v3/UserCarPact/sellerrefund";//卖家提现
    private static final String sellerRechargeUrl = hostUrl + "v3/UserCarPact/withdraw";//卖家充值

    private static final String inviteFriendUrl = hostUrl + "v3/user/getmessage";

    private static final String bindingPhoneUrl = hostUrl + "v3/user/bindmobile";

    private static final String userHomeUrl = hostUrl + "v3/user/homepage";//用户信息
    private static final String sellingCarUrl = hostUrl + "v3/publishcar/list";//在售车辆
    private static final String salesConsultantUrl = hostUrl + "v3/user/getsaleslist";//销售顾问

    private static final String userLoveCarUrl = hostUrl + "v3/car/userFavCars";//用户爱车
    private static final String dreamCarUrl = hostUrl + "v3/DreamCar/list";//梦想车

    private static final String cityListUrl = hostUrl + "car/city/province_id/";//城市列表
    private static final String provinceListUrl = hostUrl + "car/province";//省份列表

    private static final String articleSearchHistoryUrl = hostUrl + "v3/Article/visitlist";//文章浏览历史
    private static final String carSearchHistoryUrl = hostUrl + "v4/car/carvisithistory";//车辆浏览历史

    public static String getRegisterApp() {
        return registerApp;
    }

    public static String getUserRegisterUrl() {
        return userRegisterUrl;
    }

    public static String getVerificationCodeUrl() {
        return verificationCodeUrl;
    }

    public static String getForgetPasswordUrl() {
        return forgetPasswordUrl;
    }

    public static String getUserProtocolUrl() {
        return userProtocolUrl;
    }

    public static String getSplashUrl() {
        return splashUrl;
    }

    public static String getUserLoginUrl() {
        return userLoginUrl;
    }

    public static String getOauthRegisterUrl() {
        return oauthRegisterUrl;
    }

    public static String getOauthLoginUrl() {
        return oauthLoginUrl;
    }

    public static String getNewHomeUrl() {
        return newHomeUrl;
    }

    public static String getCarRentListUrl() {
        return carRentListUrl;
    }

    public static String getCreateCarRentOrderUrl() {
        return createCarRentOrderUrl;
    }

    public static String getCreateCarRentPayUrl() {
        return createCarRentPayUrl;
    }

    public static String getUploadTokenUrl() {
        return uploadTokenUrl;
    }

    public static String getCarListUrl() {
        return carListUrl;
    }

    public static String getCarDetailUrl() {
        return carDetailUrl;
    }

    public static String getVideoListUrl() {
        return videoListUrl;
    }

    public static String getArticleListUrl() {
        return articleListUrl;
    }

    public static String getArticleIndexUrl() {
        return articleIndexUrl;
    }

    public static String getArticleSearchUrl() {
        return articleSearchUrl;
    }

    public static String getUserSearchUrl() {
        return userSearchUrl;
    }

    public static String getTopicSearchUrl() {
        return topicSearchUrl;
    }

    public static String getRentCarDetailUrl() {
        return rentCarDetailUrl;
    }

    public static String getMyCarRentUrl() {
        return myCarRentUrl;
    }

    public static String getCheHangListUrl() {
        return cheHangListUrl;
    }

    public static String getCreateTopicUrl() {
        return createTopicUrl;
    }

    public static String getHallUrl() {
        return hallUrl;
    }

    public static String getTopicListUrl() {
        return topicListUrl;
    }

    public static String getBalanceUrl() {
        return balanceUrl;
    }

    public static String getTakeCashUrl() {
        return takeCashUrl;
    }

    public static String getSellerRechargeUrl() {
        return sellerRechargeUrl;
    }

    public static String getInviteFriendUrl() {
        return inviteFriendUrl;
    }

    public static String getArticleCommentListUrl() {
        return articleCommentListUrl;
    }

    public static String getUserHomeUrl() {
        return userHomeUrl;
    }

    public static String getSellingCarUrl() {
        return sellingCarUrl;
    }

    public static String getSalesConsultantUrl() {
        return salesConsultantUrl;
    }

    public static String getTopicDetailUrl() {
        return topicDetailUrl;
    }

    public static String getMyFocusUrl() {
        return myFocusUrl;
    }

    public static String getBindingPhoneUrl() {
        return bindingPhoneUrl;
    }

    public static String getUserLoveCarUrl() {
        return userLoveCarUrl;
    }

    public static String getDreamCarUrl() {
        return dreamCarUrl;
    }

    public static String getCityListUrl() {
        return cityListUrl;
    }

    public static String getProvinceListUrl() {
        return provinceListUrl;
    }

    public static String getArticleSearchHistoryUrl() {
        return articleSearchHistoryUrl;
    }

    public static String getCarSearchHistoryUrl() {
        return carSearchHistoryUrl;
    }
}