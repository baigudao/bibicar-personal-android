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
    public static final String APP_URL = "app_url";
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
    public static final String S_TYPE = "s_type";
    public static final String PAGE = "page";
    public static final String VIN = "vin";
    public static final String CITY = "city";
    public static final String HPHM = "hphm";
    public static final String TAG = "tag";
    public static final String ENGINENO = "engineno";
    public static final String CLASSNO = "classno";
    public static final String ACCOUNT = "account";
    public static final String CHAT_TOKEN = "chat_token";
    public static final String INSURANCE_DUE_TIME = "insurance_due_time";
    public static final String CHECK_EXPIRATION_TIME = "check_expiration_time";
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
    public static final String CAR_NO = "car_no";
    public static final String ACTION = "action";
    public static final String IS_TRANSFER = "is_transfer";
    public static final String CAR_STATUS = "car_status";
    public static final String VIN_NO = "vin_no";
    public static final String MODEL_ID = "model_id";
    public static final String EXCHANGE_TIME = "exchange_time";
    public static final String SERIES_ID = "series_id";
    public static final String BOARD_TIME = "board_time";
    public static final String MILEAGE = "mileage";
    public static final String MAINTAIN = "maintain";
    public static final String VIN_FILE = "vin_file";
    public static final String ENGINE_NO = "engine_no";
    public static final String MIN_PRICE = "min_price";
    public static final String MAX_PRICE = "max_price";
    public static final String MIN_MILEAGE = "min_mileage";
    public static final String MAX_MILEAGE = "max_mileage";
    public static final String MIN_BOARD_TIME = "min_board_time";
    public static final String MAX_BOARD_TIME = "max_board_time";
    public static final String HAS_VR = "has_vr";
    public static final String OLD = "old";
    public static final String PHONE = "phone";
    public static final String AGE = "age";
    public static final String BRAND_NAME = "brand_name";
    public static final String DESC = "desc";
    public static final String SOURCE = "source";
    public static final String FEED_ID = "feed_id";
    public static final String IS_AUTH = "is_auth";//是否身份验证 1:是 2:否
    public static final String THEME = "theme";
    public static final String POST_FILE = "post_file";
    public static final String TITLE = "title";
    public static final String MONEY = "money";
    public static final String CAR_COLOR = "car_color";
    public static final String AVATAR = "avatar";
    public static final String COMPANY_ID = "company_id";
    public static final String THEME_ID = "theme_id";
    public static final String PROVINCE_ID = "province_id";
    public static final String CONTENT = "content";
    public static final String REPLY_ID = "reply_id";
    public static final String FATHER_ID = "father_id";
    public static final String COMMENT_ID = "comment_id";
    public static final String FILES_TYPE = "files_type";
    public static final String FILES_ID = "files_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CAR_INTRO = "car_intro";
    public static final String PRICE = "price";
    public static final String CITY_ID = "city_id";
    public static final String REPORT_SN = "report_sn";
    public static final String CAR_TYPE = "car_type";
    public static final String POST_CONTENT = "post_content";
    public static final String CONTACT_PHONE = "contact_phone";
    public static final String CONTACT_ADDRESS = "contact_address";
    public static final String ADVERTISEMENT_IMAGE_SUCCESS = "advertisement_image_success";
    public static final String ADVERTISEMENT_IMAGE = "advertisement_image";
    public static final String ADVERTISEMENT_IMAGE_NUM = "advertisement_image_num";
    public static final String HOST_IMAGE_URL = "http://img.bibicar.cn/";//http://img.bibicar.cn/Fja6zKmICAz6l6QCd7-Z0pC-bjt8

    //URL常量
    //        private final static String hostUrl = "https://api.bibicar.cn/";//正式环境
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

    private static final String userInfoUpdateUrl = hostUrl + "v3/User/updateAll";//用户资料更新

    private final static String carRentListUrl = hostUrl + "v4/rentalcar/list";//可租车辆列表
    private final static String createCarRentOrderUrl = hostUrl + "v4/rentalcar/createrentalorder";//创建租车订单
    private final static String createCarRentPayUrl = hostUrl + "v4/rentalcar/rentalorderpay";//创建租车支付
    private static final String rentCarDetailUrl = hostUrl + "v4/rentalcar/index";//租车详情
    private static final String myCarRentUrl = hostUrl + "v4/rentalcar/rentalorderlist";//我的租车

    private final static String uploadTokenUrl = hostUrl + "app/uploadtoken";//上传文件的Token

    private final static String carListUrl = hostUrl + "v4/car/list";//车辆列表
    private final static String carDetailUrl = hostUrl + "v4/car/index";//车辆详情  v4接口

    private final static String videoListUrl = hostUrl + "v3/Video/list";//视频列表
    private static final String videoDetailUrl = hostUrl + "v3/Video/index";//视频详情

    private final static String articleListUrl = hostUrl + "v3/article/list";//文章列表
    private final static String articleIndexUrl = hostUrl + "v3/article/index";
    private final static String articleSearchUrl = hostUrl + "v3/Article/searcharticlelist";//搜索文章
    private static final String articleCommentListUrl = hostUrl + "v3/article/commentlist";//文章评论列表
    private static final String createCommentUrl = hostUrl + "v3/article/commentcreate";//添加评论
    private final static String allSecondComment = hostUrl + "v3/article/getcomment";//二级评论
    private final static String commentLikeCreate = hostUrl + "v3/article/commentlikecreate";//点赞
    private final static String commentLikeDelete = hostUrl + "v3/article/commentlikedelete";//取消点赞
    private final static String articleCollectURl = hostUrl + "v3/article/collectcreate";//收藏
    private final static String articleDeleteURl = hostUrl + "v3/article/collectdelete";//取消收藏
    private final static String carDeleteURl = hostUrl + "v3/Favoritecar/delete";//取消车辆收藏
    private final static String carCollectURl = hostUrl + "v3/Favoritecar/create";//车辆收藏
    private final static String articleCollectListURl = hostUrl + "v3/Article/collectlist";//文章收藏列表
    private final static String videoCollectListURl = hostUrl + "v3/Video/collectlist";//视频收藏列表

    private final static String likeCreateURl = hostUrl + "v3/Like/create";//朋友圈点赞
    private final static String likeDeleteURl = hostUrl + "v3/Like/delete";//朋友圈取消点赞
    private final static String deleteCommentUrl = hostUrl + "v3/comment/delete";//删除朋友圈
    private final static String postDetailUrl = hostUrl + "v4/Post/index";//朋友圈详情

    private final static String userSearchUrl = hostUrl + "v3/user/search";//用户搜索

    private static final String cheHangListUrl = hostUrl + "v4/publishcar/getcompanylist";//车行列表

    private static final String createTopicUrl = hostUrl + "v3/theme/createtheme";//创建话题
    private static final String hallUrl = hostUrl + "v3/theme/themehome";//大厅
    private static final String topicListUrl = hostUrl + "v3/theme/list";//话题列表
    private static final String topicDetailUrl = hostUrl + "v3/theme/themeindex";//话题详情
    private static final String myFocusUrl = hostUrl + "v3/theme/myfocus";//我的关注
    private static final String joinTopicUrl = hostUrl + "v3/theme/followtheme";//加入话题
    private static final String topicMemberUrl = hostUrl + "v3/theme/themeofuser";//话题成员
    private static final String allTopicUrl = hostUrl + "v3/theme/alltheme";//全部话题
    private final static String topicSearchUrl = hostUrl + "v3/theme/searchtheme";//话题搜索

    private static final String balanceUrl = hostUrl + "v3/UserCarPact/getusermoney";//获取余额
    private static final String takeCashUrl = hostUrl + "v3/UserCarPact/sellerrefund";//卖家提现
    private static final String sellerRechargeUrl = hostUrl + "v3/UserCarPact/withdraw";//卖家充值

    private static final String inviteFriendUrl = hostUrl + "v3/user/getmessage";

    private static final String bindingPhoneUrl = hostUrl + "v3/user/bindmobile";

    private static final String userHomeUrl = hostUrl + "v3/user/homepage";//用户信息
    private static final String sellingCarUrl = hostUrl + "v3/publishcar/list";//在售车辆
    private static final String salesConsultantUrl = hostUrl + "v3/user/getsaleslist";//销售顾问

    private static final String userLoveCarUrl = hostUrl + "v4/car/userFavCars";//用户爱车
    private static final String dreamCarUrl = hostUrl + "v3/DreamCar/list";//梦想车

    private static final String cityListUrl = hostUrl + "car/city/province_id/";//城市列表
    private static final String provinceListUrl = hostUrl + "car/province";//省份列表

    private static final String articleSearchHistoryUrl = hostUrl + "v3/Article/visitlist";//文章浏览历史
    private static final String carSearchHistoryUrl = hostUrl + "v4/car/carvisithistory";//车辆浏览历史

    private static final String customMadeCarUrl = hostUrl + "v3/car/applycar";//订制车辆

    private static final String myHomePageUrl = hostUrl + "v4/User/homepage";//个人中心
    private static final String myUserPageUrl = hostUrl + "v4/user/userpage";//个人主页
    private static final String myRichListUrl = hostUrl + "v4/User/getrichlist";//财富排行

    private static final String myFriendsUrl = hostUrl + "v3/post/publish";//个人的朋友圈
    //    private static final String friendsUrl = hostUrl + "v3/post/publish";//朋友圈
    private static final String commentListUrl = hostUrl + "v3/Comment/list";//评论列表

    private static final String brandUrl = hostUrl + "v4/PollingCar/getPollingCarBrand";

    private static final String carBrandUrl = hostUrl + "car/brand";//车辆品牌列表
    private static final String carBrandSeriesUrl = hostUrl + "car/series/brand_id/";//通过品牌id得到车辆系列列表
    private static final String carModelUrl = hostUrl + "car/model/series_id/";//通过系列id得到车型列表

    private static final String likeCarListUrl = hostUrl + "v3/Favoritecar/list";//喜欢的车辆

    private static final String publishNewCarUrl = hostUrl + "v3/publishcar/newCar";//上传新车
    private static final String publishSecondCarUrl = hostUrl + "v3/publishcar/create";//上传二手车

    private static final String createFollowUrl = hostUrl + "v3/friendship/create";//关注
    private static final String deleteFollowUrl = hostUrl + "v3/friendship/delete";//取消关注

    private static final String createPostUrl = hostUrl + "v3/post/create";//发布动态

    private static final String checkPeccancyUrl = hostUrl + "v4/pollingcar/checkcarrule";//查违章
    private static final String checkHistoryUrl = hostUrl + "v4/pollingcar/history";//查询历史
    private static final String checkGuaranteeUrl = hostUrl + "v4/PollingCar/PollingCarPay";//查维保支付费用
    private static final String checkSumUrl = hostUrl + "v4/PollingCar/CheckIns";//查出险支付费用
    private static final String peccancyListUrl = hostUrl + "v4/pollingcar/checkcarrulelist";//违章列表
    private static final String checkDetailUrl = hostUrl + "v4/pollingcar/getreport";//维保，出险详情
    private static final String sumListUrl = hostUrl + "v4/pollingcar/Inslist";//出险列表
    private static final String guaranteeListUrl = hostUrl + "v4/pollingcar/PollingCarList";//维保列表

    private static final String orderListUrl = hostUrl + "v3/shop/orderlist";//订单列表

    private static final String richLikeUrl = hostUrl + "v4/user/createrichboardlike";//排行点赞
    private static final String richDeleteLikeUrl = hostUrl + "v4/user/cancelrichboardlike";//排行点赞取消

    private static final String mySellingCarUrl = hostUrl + "v4/Publishcar/list";//我的售车

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

    public static String getCustomMadeCarUrl() {
        return customMadeCarUrl;
    }

    public static String getMyHomePageUrl() {
        return myHomePageUrl;
    }

    public static String getMyRichListUrl() {
        return myRichListUrl;
    }

    public static String getMyUserPageUrl() {
        return myUserPageUrl;
    }

    public static String getVideoDetailUrl() {
        return videoDetailUrl;
    }

    public static String getMyFriendsUrl() {
        return myFriendsUrl;
    }

    public static String getCommentListUrl() {
        return commentListUrl;
    }

    public static String getCreateCommentUrl() {
        return createCommentUrl;
    }

    public static String getAllSecondComment() {
        return allSecondComment;
    }

    public static String getCommentLikeCreate() {
        return commentLikeCreate;
    }

    public static String getCommentLikeDelete() {
        return commentLikeDelete;
    }

    public static String getArticleCollectURl() {
        return articleCollectURl;
    }

    public static String getArticleDeleteURl() {
        return articleDeleteURl;
    }

    public static String getJoinTopicUrl() {
        return joinTopicUrl;
    }

    public static String getTopicMemberUrl() {
        return topicMemberUrl;
    }

    public static String getBrandUrl() {
        return brandUrl;
    }

    public static String getCarBrandUrl() {
        return carBrandUrl;
    }

    public static String getLikeCarListUrl() {
        return likeCarListUrl;
    }

    public static String getPublishNewCarUrl() {
        return publishNewCarUrl;
    }

    public static String getCreateFollowUrl() {
        return createFollowUrl;
    }

    public static String getDeleteFollowUrl() {
        return deleteFollowUrl;
    }

    public static String getCreatePostUrl() {
        return createPostUrl;
    }

    public static String getCheckPeccancyUrl() {
        return checkPeccancyUrl;
    }

    public static String getCheckHistoryUrl() {
        return checkHistoryUrl;
    }

    public static String getCheckGuaranteeUrl() {
        return checkGuaranteeUrl;
    }

    public static String getCheckSumUrl() {
        return checkSumUrl;
    }

    public static String getPeccancyListUrl() {
        return peccancyListUrl;
    }

    public static String getCheckDetailUrl() {
        return checkDetailUrl;
    }

    public static String getSumListUrl() {
        return sumListUrl;
    }

    public static String getGuaranteeListUrl() {
        return guaranteeListUrl;
    }

    public static String getAllTopicUrl() {
        return allTopicUrl;
    }

    public static String getCarDeleteURl() {
        return carDeleteURl;
    }

    public static String getCarCollectURl() {
        return carCollectURl;
    }

    public static String getArticleCollectListURl() {
        return articleCollectListURl;
    }

    public static String getVideoCollectListURl() {
        return videoCollectListURl;
    }

    public static String getOrderListUrl() {
        return orderListUrl;
    }

    public static String getLikeCreateURl() {
        return likeCreateURl;
    }

    public static String getLikeDeleteURl() {
        return likeDeleteURl;
    }

    public static String getDeleteCommentUrl() {
        return deleteCommentUrl;
    }

    public static String getPostDetailUrl() {
        return postDetailUrl;
    }

    public static String getPublishSecondCarUrl() {
        return publishSecondCarUrl;
    }

    public static String getCarBrandSeriesUrl() {
        return carBrandSeriesUrl;
    }

    public static String getCarModelUrl() {
        return carModelUrl;
    }

    public static String getUserInfoUpdateUrl() {
        return userInfoUpdateUrl;
    }

    public static String getRichLikeUrl() {
        return richLikeUrl;
    }

    public static String getRichDeleteLikeUrl() {
        return richDeleteLikeUrl;
    }

    public static String getMySellingCarUrl() {
        return mySellingCarUrl;
    }
}