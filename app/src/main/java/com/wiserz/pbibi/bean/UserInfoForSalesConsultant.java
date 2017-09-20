package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/20 10:18.
 * QQ : 971060378
 * Used as : 销售顾问的bean
 */
public class UserInfoForSalesConsultant extends BaseBean {

    /**
     * avatar : http://img.bibicar.cn/FlWyA1DDi61SjRIfj1t3_5t0fOhw
     * intro :
     * nickname : 水果很忙
     * signature :
     * sort : 625
     * tag : 1
     * type : 1
     * user_id : 4784
     */

    private String avatar;
    private String intro;
    private String nickname;
    private String signature;
    private int sort;
    private int tag;
    private int type;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
