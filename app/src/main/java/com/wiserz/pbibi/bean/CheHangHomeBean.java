package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/10 17:19.
 * QQ : 971060378
 * Used as : 车行bean，用于推荐页面中的车行列表
 */
public class CheHangHomeBean extends BaseBean {

    private String avatar;
    private String nickname;
    private String signature;
    private int saling_num;
    private int sold_num;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public int getSaling_num() {
        return saling_num;
    }

    public void setSaling_num(int saling_num) {
        this.saling_num = saling_num;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
