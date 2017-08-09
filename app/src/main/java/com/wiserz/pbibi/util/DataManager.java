package com.wiserz.pbibi.util;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.wiserz.pbibi.bean.LoginBean;

/**
 * Created by jackie on 2017/8/9 16:36.
 * QQ : 971060378
 * Used as : 数据管理者
 */
public class DataManager {

    private static DataManager mDataManager;

    private static final Object mObject = new Object();

    private Object mData1, mData2, mData3, mData4, mData5;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (mDataManager == null) {
            synchronized (mObject) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }

    /**
     * 保存当前用户信息
     *
     * @param userInfoBean
     */
    public void setUserInfo(LoginBean.UserInfoBean userInfoBean) {
        SPUtils.getInstance().put(Constant.MY_USER_INFO, userInfoBean == null ? "" : CommonUtil.toJson(userInfoBean));
    }

    /**
     * 获取当前用户信息
     */
    public LoginBean.UserInfoBean getUserInfo() {
        String userInfo = SPUtils.getInstance().getString(Constant.MY_USER_INFO);
        if (EmptyUtils.isEmpty(userInfo)) {
            return null;
        }
        LoginBean.UserInfoBean userInfoBean = CommonUtil.fromJson(LoginBean.UserInfoBean.class, userInfo);
        return userInfoBean;
    }
}
