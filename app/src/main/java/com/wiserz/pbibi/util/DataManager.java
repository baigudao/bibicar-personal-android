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

    private Object mData, mData1, mData2, mData3, mData4, mData5, mData6, mData7, mData8, mData9;

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

    public void setObject(Object data) {
        mData = data;
    }

    public Object getObject() {
        return mData;
    }

    public void setData1(Object data1) {
        mData1 = data1;
    }

    public Object getData1() {
        return mData1;
    }

    public void setData2(Object data2) {
        mData2 = data2;
    }

    public Object getData2() {
        return mData2;
    }

    public void setData3(Object data3) {
        mData3 = data3;
    }

    public Object getData3() {
        return mData3;
    }

    public void setData4(Object data4) {
        mData4 = data4;
    }

    public Object getData4() {
        return mData4;
    }

    public void setData5(Object data5) {
        mData5 = data5;
    }

    public Object getData5() {
        return mData5;
    }

    public Object getData6() {
        return mData6;
    }

    public void setData6(Object mData6) {
        this.mData6 = mData6;
    }

    public Object getData7() {
        return mData7;
    }

    public void setData7(Object mData7) {
        this.mData7 = mData7;
    }

    public Object getData8() {
        return mData8;
    }

    public void setData8(Object mData8) {
        this.mData8 = mData8;
    }

    public Object getData9() {
        return mData9;
    }

    public void setData9(Object mData9) {
        this.mData9 = mData9;
    }
}
