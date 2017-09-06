package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/6 13:00.
 * QQ : 971060378
 * Used as : 全部的二级评论
 */
public class AllSecondCommentBean extends BaseBean {

    /**
     * comment_content : 不
     * comment_created : 1504662274
     * comment_id : 888
     * comment_reply_id : 878
     * feed_id : 6147
     * from_user : {"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}
     * is_like : 2
     * like_num : 0
     * to_user : {"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}
     */

    private String comment_content;
    private int comment_created;
    private int comment_id;
    private int comment_reply_id;
    private int feed_id;
    private FromUserBean from_user;
    private int is_like;
    private int like_num;
    private ToUserBean to_user;

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public int getComment_created() {
        return comment_created;
    }

    public void setComment_created(int comment_created) {
        this.comment_created = comment_created;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_reply_id() {
        return comment_reply_id;
    }

    public void setComment_reply_id(int comment_reply_id) {
        this.comment_reply_id = comment_reply_id;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public FromUserBean getFrom_user() {
        return from_user;
    }

    public void setFrom_user(FromUserBean from_user) {
        this.from_user = from_user;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public ToUserBean getTo_user() {
        return to_user;
    }

    public void setTo_user(ToUserBean to_user) {
        this.to_user = to_user;
    }

    public static class FromUserBean {
        /**
         * avatar : http://img.bibicar.cn/bibilogo.png
         * nickname : youth
         * user_id : 342
         */

        private String avatar;
        private String nickname;
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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class ToUserBean {
        /**
         * avatar : http://img.bibicar.cn/bibilogo.png
         * nickname : youth
         * user_id : 342
         */

        private String avatar;
        private String nickname;
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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
