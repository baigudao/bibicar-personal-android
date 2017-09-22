package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/9/5 10:35.
 * QQ : 971060378
 * Used as : 个人朋友圈中的feed_list
 */
public class FeedBean extends BaseBean {

    /**
     * car_info : {}
     * comment_list : [{"comment_id":843,"content":"嗯","created":1497591207,"feed_id":536,"feed_type":1,"from_user_info":{"profile":{"avatar":"http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT","nickname":"松鼠"},"user_id":451},"post_files":[{"file_id":"FnOtQY_26Q9aKT3_-ufsxuwxPS7N","file_type":1,"file_url":"http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N"}],"reply_id":0,"to_user_info":{}},{"comment_id":847,"content":"很好","created":1502203004,"feed_id":536,"feed_type":1,"from_user_info":{"profile":{"avatar":"http://img.bibicar.cn/Fomgs5nsei9RzTEQSAncDKoFx5uI","nickname":"MR·胡"},"user_id":455},"post_files":[{"file_id":"FnOtQY_26Q9aKT3_-ufsxuwxPS7N","file_type":1,"file_url":"http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N"}],"reply_id":0,"to_user_info":{}}]
     * comment_num : 2
     * created : 1471487348
     * feed_id : 536
     * feed_type : 1
     * feeds : [{"avatar":"http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT","feed_id":"536","nickname":"松鼠","user_id":"451"},{"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","feed_id":"536","nickname":"馨子","user_id":"454"},{"avatar":"http://img.bibicar.cn/Fomgs5nsei9RzTEQSAncDKoFx5uI","feed_id":"536","nickname":"MR·胡","user_id":"455"}]
     * forward_id : 0
     * forward_num : 0
     * is_like : 1
     * like_list : [{"profile":{"avatar":"http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT","nickname":"松鼠"},"user_id":451},{"profile":{"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","nickname":"馨子"},"user_id":454},{"profile":{"avatar":"http://img.bibicar.cn/Fomgs5nsei9RzTEQSAncDKoFx5uI","nickname":"MR·胡"},"user_id":455}]
     * like_num : 2
     * post_content : DD~
     * post_files : [{"file_id":"FnOtQY_26Q9aKT3_-ufsxuwxPS7N","file_type":1,"file_url":"http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N"}]
     * post_user_info : {"profile":{"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","nickname":"馨子","type":1},"user_id":454}
     * source_id : 536
     * user_favourite_car : {"brand_info":{"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"}}
     */

    private CarInfoBean car_info;
    private int comment_num;
    private int created;
    private int feed_id;
    private int feed_type;
    private int forward_id;
    private int forward_num;
    private String is_like;
    private int like_num;
    private String post_content;
    private PostUserInfoBean post_user_info;
    private int source_id;
    private UserFavouriteCarBean user_favourite_car;
    private List<CommentListBean> comment_list;
    private List<FeedsBean> feeds;
    private List<LikeListBean> like_list;
    private List<PostFilesBeanX> post_files;

    public CarInfoBean getCar_info() {
        return car_info;
    }

    public void setCar_info(CarInfoBean car_info) {
        this.car_info = car_info;
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

    public int getForward_id() {
        return forward_id;
    }

    public void setForward_id(int forward_id) {
        this.forward_id = forward_id;
    }

    public int getForward_num() {
        return forward_num;
    }

    public void setForward_num(int forward_num) {
        this.forward_num = forward_num;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
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

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public UserFavouriteCarBean getUser_favourite_car() {
        return user_favourite_car;
    }

    public void setUser_favourite_car(UserFavouriteCarBean user_favourite_car) {
        this.user_favourite_car = user_favourite_car;
    }

    public List<CommentListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public List<FeedsBean> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedsBean> feeds) {
        this.feeds = feeds;
    }

    public List<LikeListBean> getLike_list() {
        return like_list;
    }

    public void setLike_list(List<LikeListBean> like_list) {
        this.like_list = like_list;
    }

    public List<PostFilesBeanX> getPost_files() {
        return post_files;
    }

    public void setPost_files(List<PostFilesBeanX> post_files) {
        this.post_files = post_files;
    }

    public static class CarInfoBean {
    }

    public static class PostUserInfoBean {
        /**
         * profile : {"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","nickname":"馨子","type":1}
         * user_id : 454
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
             * avatar : http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k
             * nickname : 馨子
             * type : 1
             */

            private String avatar;
            private String nickname;
            private int type;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }

    public static class UserFavouriteCarBean {
        /**
         * brand_info : {"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"}
         */

        private BrandInfoBean brand_info;

        public BrandInfoBean getBrand_info() {
            return brand_info;
        }

        public void setBrand_info(BrandInfoBean brand_info) {
            this.brand_info = brand_info;
        }

        public static class BrandInfoBean {
            /**
             * abbre : B
             * brand_id : 3
             * brand_name : 宝马
             * brand_url : http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png
             */

            private String abbre;
            private int brand_id;
            private String brand_name;
            private String brand_url;

            public String getAbbre() {
                return abbre;
            }

            public void setAbbre(String abbre) {
                this.abbre = abbre;
            }

            public int getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(int brand_id) {
                this.brand_id = brand_id;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getBrand_url() {
                return brand_url;
            }

            public void setBrand_url(String brand_url) {
                this.brand_url = brand_url;
            }
        }
    }

    public static class CommentListBean {
        /**
         * comment_id : 843
         * content : 嗯
         * created : 1497591207
         * feed_id : 536
         * feed_type : 1
         * from_user_info : {"profile":{"avatar":"http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT","nickname":"松鼠"},"user_id":451}
         * post_files : [{"file_id":"FnOtQY_26Q9aKT3_-ufsxuwxPS7N","file_type":1,"file_url":"http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N"}]
         * reply_id : 0
         * to_user_info : {}
         */

        private int comment_id;
        private String content;
        private int created;
        private int feed_id;
        private int feed_type;
        private FromUserInfoBean from_user_info;
        private int reply_id;
        private ToUserInfoBean to_user_info;
        private List<PostFilesBean> post_files;

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public FromUserInfoBean getFrom_user_info() {
            return from_user_info;
        }

        public void setFrom_user_info(FromUserInfoBean from_user_info) {
            this.from_user_info = from_user_info;
        }

        public int getReply_id() {
            return reply_id;
        }

        public void setReply_id(int reply_id) {
            this.reply_id = reply_id;
        }

        public ToUserInfoBean getTo_user_info() {
            return to_user_info;
        }

        public void setTo_user_info(ToUserInfoBean to_user_info) {
            this.to_user_info = to_user_info;
        }

        public List<PostFilesBean> getPost_files() {
            return post_files;
        }

        public void setPost_files(List<PostFilesBean> post_files) {
            this.post_files = post_files;
        }

        public static class FromUserInfoBean {
            public static class ProfileBeanX {
            }
        }

        public static class ToUserInfoBean {
        }

        public static class PostFilesBean {
            /**
             * file_id : FnOtQY_26Q9aKT3_-ufsxuwxPS7N
             * file_type : 1
             * file_url : http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N
             */

            private String file_id;
            private int file_type;
            private String file_url;

            public String getFile_id() {
                return file_id;
            }

            public void setFile_id(String file_id) {
                this.file_id = file_id;
            }

            public int getFile_type() {
                return file_type;
            }

            public void setFile_type(int file_type) {
                this.file_type = file_type;
            }

            public String getFile_url() {
                return file_url;
            }

            public void setFile_url(String file_url) {
                this.file_url = file_url;
            }
        }
    }

    public static class FeedsBean {
        /**
         * avatar : http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT
         * feed_id : 536
         * nickname : 松鼠
         * user_id : 451
         */

        private String avatar;
        private String feed_id;
        private String nickname;
        private String user_id;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(String feed_id) {
            this.feed_id = feed_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static class LikeListBean {
        /**
         * profile : {"avatar":"http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT","nickname":"松鼠"}
         * user_id : 451
         */

        private ProfileBeanXX profile;
        private int user_id;

        public ProfileBeanXX getProfile() {
            return profile;
        }

        public void setProfile(ProfileBeanXX profile) {
            this.profile = profile;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public static class ProfileBeanXX {
            /**
             * avatar : http://img.bibicar.cn/FkVVSJRd17tt2YR8lUQh19Z7-RlT
             * nickname : 松鼠
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

    public static class PostFilesBeanX {
        /**
         * file_id : FnOtQY_26Q9aKT3_-ufsxuwxPS7N
         * file_type : 1
         * file_url : http://img.bibicar.cn/FnOtQY_26Q9aKT3_-ufsxuwxPS7N
         */

        private String file_id;
        private int file_type;
        private String file_url;

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }
}
