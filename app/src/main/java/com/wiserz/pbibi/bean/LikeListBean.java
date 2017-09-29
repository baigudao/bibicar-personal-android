package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/29 13:28.
 * QQ : 971060378
 * Used as :
 */
public class LikeListBean extends BaseBean {

    /**
     * profile : {"avatar":"http://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEJjmqhibiaUsibic0TXzBcFrggdTOzu8kia65OFayx4xPU1DCUDgAfPTCIVSTVDmnK5j0EQx420obzcSUw/0","nickname":"王与坐骑"}
     * user_id : 434
     */

    private ProfileBean profile;
    private int user_id;

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static class ProfileBean {
        /**
         * avatar : http://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEJjmqhibiaUsibic0TXzBcFrggdTOzu8kia65OFayx4xPU1DCUDgAfPTCIVSTVDmnK5j0EQx420obzcSUw/0
         * nickname : 王与坐骑
         */

        private String avatar;
        private String nickname;

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
    }
}
