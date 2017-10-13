package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/10/13 14:02.
 * QQ : 971060378
 * Used as : xxx
 */
public class ContactsBean extends BaseBean {

    private String name;
    private String phone;
    private String letter;//字母
    private UserInfoBean userinfo;

    public ContactsBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public UserInfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfoBean userinfo) {
        this.userinfo = userinfo;
    }

}