package cn.sharesdk.login;

import java.io.Serializable;

import cn.sharesdk.framework.Platform;

/**
 * Created by jackie on 2017/8/26 13:38.
 * QQ : 971060378
 * Used as : 第三方登录授权的用户信息
 */
public class UserInfo implements Serializable {

    private String userIcon;
    private String userName;
    private String userGender;
    private String userNote;
    private String userId;
    private Platform platform;

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }
}
