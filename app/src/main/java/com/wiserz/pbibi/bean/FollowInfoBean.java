package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/12 16:40.
 * QQ : 971060378
 * Used as : 我的关注的bean
 */
public class FollowInfoBean extends BaseBean {

    /**
     * avatar : http://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEJEZywakwtSMKYTRaTS53g74C2niaWBf1qOgK2w12iapmcvPMvVYIkgwHiabG7hyX6VTCDsicuB5nf0dw/0
     * created_at : 1506044513
     * id : 38
     * nickname : youth
     * type : 1
     * type_id : 59c46a6156cb2
     * type_info : {"car_intro":"","car_name":"兰博基尼 Aventador 2011 LP700-4","car_type":1,"file_img":"http://img.bibicar.cn/FoZ3PkBM3IgRb6wQoBqezDM3RP6Q?imageMogr2/auto-orient/thumbnail/1000x/strip","hash":"59c46a6156cb2","price":350}
     * user_id : 342
     */

    private String avatar;
    private String created_at;
    private int id;
    private String nickname;
    private int type;
    private String type_id;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
