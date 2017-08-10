package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/8/10 16:28.
 * QQ : 971060378
 * Used as : 文章bean
 */
public class ArticleBean extends BaseBean {

    /**
     * collect_num : 19
     * comment_num : 48
     * created : 1479449731
     * feed_id : 1567
     * feed_type : 4
     * image_url : ["http://image.bitautoimg.com/appimage/media/20161117/w1024_h897_0e8a761027e047c588ef51a392afaf73.jpg"]
     * is_collect : 2
     * is_like : 2
     * like_num : 3
     * post_content : 这豪车40万买来 现在却值2亿涨得比楼价疯多了
     * post_user_info : {"profile":{"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","nickname":"BiBi Car"},"user_id":389}
     * title : 这车叫布加迪Type 57SC Atalantic，在几年前的拍卖中，成交价是3000万美金，折合成人民币2亿元，此前一直由法拉利250 TestaRossa保持的1220万美元的最高拍卖价被毫不留情地打破，需要知道的是，这车是1971年买下的，当时的售价为5.9万美元，以现时的汇率折算是40万左右，一买一卖升值了500多倍，如果加上拍卖会上买家需要支付的佣金，Type 57SC Atalantic的新主人需要准备差不多4000万美元，相当于2.8亿人民币。
     */

    private int collect_num;
    private int comment_num;
    private int created;
    private int feed_id;
    private int feed_type;
    private int is_collect;
    private int is_like;
    private int like_num;
    private String post_content;
    private PostUserInfoBean post_user_info;
    private String title;
    private List<String> image_url;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(List<String> image_url) {
        this.image_url = image_url;
    }

    public static class PostUserInfoBean {
        /**
         * profile : {"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","nickname":"BiBi Car"}
         * user_id : 389
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
