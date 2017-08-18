package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/18 13:15.
 * QQ : 971060378
 * Used as : 用户的bean
 */
public class UserBean extends BaseBean {

    /**
     * avatar : http://tp2.sinaimg.cn/1742241445/50/5701534834/1
     * fans_num : 0
     * friend_num : 0
     * is_friend : 2
     * nickname : Vincent_薛雪峰
     * user_id : 343
     */

    private String avatar;
    private int fans_num;
    private int friend_num;
    private int is_friend;
    private String nickname;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public int getFriend_num() {
        return friend_num;
    }

    public void setFriend_num(int friend_num) {
        this.friend_num = friend_num;
    }

    public int getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
