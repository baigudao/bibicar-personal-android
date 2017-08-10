package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/10 15:38.
 * QQ : 971060378
 * Used as : 视频bean
 */
public class VideoBean extends BaseBean {

    /**
     * collect_num : 0
     * comment_num : 0
     * created : 1484545742
     * feed_id : 1632
     * feed_type : 5
     * html_url : http://ooowovlud.bkt.clouddn.com/13-04-50-13%E5%89%AF%E6%9C%AC.mp4
     * image_url : http://7xopqk.com1.z0.glb.clouddn.com/Fgmd6hlXnaerv5flRHWE8ARGD50e
     * is_collect : 2
     * is_like : 2
     * like_num : 0
     * post_content : 绝密视频
     * post_user_info : {"profile":{"avatar":"","nickname":"youth"},"user_id":342}
     * visit_num : 1
     */
    private int collect_num;
    private int comment_num;
    private int created;
    private int feed_id;
    private int feed_type;
    private String html_url;
    private String image_url;
    private int is_collect;
    private int is_like;
    private int like_num;
    private String post_content;
    private PostUserInfoBean post_user_info;
    private int visit_num;

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public int getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(int feed_type) {
        this.feed_type = feed_type;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
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

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public PostUserInfoBean getPost_user_info() {
        return post_user_info;
    }

    public void setPost_user_info(PostUserInfoBean post_user_info) {
        this.post_user_info = post_user_info;
    }

    public int getVisit_num() {
        return visit_num;
    }

    public void setVisit_num(int visit_num) {
        this.visit_num = visit_num;
    }

    public static class PostUserInfoBean {
        /**
         * profile : {"avatar":"","nickname":"youth"}
         * user_id : 342
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
             * avatar :
             * nickname : youth
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
}
