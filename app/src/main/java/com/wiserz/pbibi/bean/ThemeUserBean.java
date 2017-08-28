package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/28 16:02.
 * QQ : 971060378
 * Used as : 话题参与者的bean
 */
public class ThemeUserBean extends BaseBean {

    /**
     * avatar : http://wx.qlogo.cn/mmopen/sAITGpVCapN4Flru4Nzy6c6Ajzq1IRZg0PjFfu6xheBNDQSWXQ7qGMdZlDcjEyJibx6jdiaFkEjRNvqJMWHgAamxZvWHlLJA8N/0
     * created : 1473245745
     * id : 46
     * nickname : 小哈佛
     * theme_id : 8
     * user_id : 545
     */

    private String avatar;
    private String created;
    private int id;
    private String nickname;
    private int theme_id;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
