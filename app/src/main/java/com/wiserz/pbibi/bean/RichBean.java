package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/4 16:04.
 * QQ : 971060378
 * Used as : 财富bean
 */
public class RichBean extends BaseBean {

    /**
     * avatar : http://wx.qlogo.cn/mmopen/4LPl8aTWWCLDIDuT8vgwpicPzXlgHOkfUsOWI9Iwdp56HJ0XxSxpClaOa57UbqmKWhWUDESiaFFVjBUvhn2L3c32icuNohibGueI/0
     * is_like : 2
     * nickname : 子锋
     * sort : 620
     * total_money : 911.55
     * type : 1
     * user_id : 310
     */

    private String avatar;
    private int is_like;
    private String nickname;
    private int sort;
    private double total_money;
    private int type;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
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
