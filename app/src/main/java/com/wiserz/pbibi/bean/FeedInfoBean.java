package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/4 18:19.
 * QQ : 971060378
 * Used as : 视频详情中的feed_info
 */
public class FeedInfoBean extends BaseBean {

    /**
     * collect_num : 0
     * comment_num : 1
     * created : 1503764995
     * feed_from :
     * feed_id : 6133
     * feed_type : 5
     * html_url : http://v.youku.com/v_show/id_XMjk4NDgwMjIwOA==.html?spm=a2h0k.8191407.0.0&from=s1.8-1-1.2
     * image_url : http://img.bibicar.cn/bibichepaidangdiyiqi.jpeg
     * is_collect : 2
     * is_like : 2
     * like_num : 0
     * post_user_info : {"profile":{"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","nickname":"BiBi Car"},"user_id":389}
     * title : BiBi车拍档第一期
     * visit_num : 4
     */

    private int collect_num;
    private int comment_num;
    private int created;
    private String feed_from;
    private int feed_id;
    private int feed_type;
    private String html_url;
    private String image_url;
    private int is_collect;
    private int is_like;
    private int like_num;
    private PostUserInfoBean post_user_info;
    private String title;
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

    public String getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(String feed_from) {
        this.feed_from = feed_from;
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

    public PostUserInfoBean getPost_user_info() {
        return post_user_info;
    }

    public void setPost_user_info(PostUserInfoBean post_user_info) {
        this.post_user_info = post_user_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVisit_num() {
        return visit_num;
    }

    public void setVisit_num(int visit_num) {
        this.visit_num = visit_num;
    }

    public static class PostUserInfoBean {
        /**
         * profile : {"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","nickname":"BiBi Car"}
         * user_id : 389
         */

        private ProfileBean profile;
        private int user_id;
        private int is_friend;

        public int getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(int is_friend) {
            this.is_friend = is_friend;
        }

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
             * avatar : http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF
             * nickname : BiBi Car
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
