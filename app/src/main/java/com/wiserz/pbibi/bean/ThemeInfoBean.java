package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/7 13:17.
 * QQ : 971060378
 * Used as : 话题信息
 */
public class ThemeInfoBean extends BaseBean{

    /**
     * id : 6
     * is_skip : 2
     * post_file : http://img.bibicar.cn/FidOTHM5hL_cZwWetjMdobNCtJhH
     * sort : 21
     * theme : #秋风十里#
     * title : 舱手握方向盘的那一刻
     * user_info : {"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","fans_num":6,"friend_num":1,"nickname":"BiBi Car","user_id":389}
     */

    private int id;
    private int is_skip;
    private String post_file;
    private int sort;
    private String theme;
    private String title;
    private UserInfoBean user_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(int is_skip) {
        this.is_skip = is_skip;
    }

    public String getPost_file() {
        return post_file;
    }

    public void setPost_file(String post_file) {
        this.post_file = post_file;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {
        /**
         * avatar : http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF
         * fans_num : 6
         * friend_num : 1
         * nickname : BiBi Car
         * user_id : 389
         */

        private String avatar;
        private int fans_num;
        private int friend_num;
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
}
