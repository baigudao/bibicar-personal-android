package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/8/24 17:14.
 * QQ : 971060378
 * Used as : 文章评论的bean
 */
public class ArticleCommentBean extends BaseBean {

    /**
     * brand_info : {"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"}
     * comment_content : 😎
     * comment_created : 1479522464
     * comment_id : 739
     * comment_reply_id : 0
     * feed_id : 1567
     * from_user : {"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}
     * hot_list : {"list":[{"comment_content":"👿","comment_created":1479809423,"comment_id":740,"comment_reply_id":739,"feed_id":1567,"from_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","brand_info":{"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"},"nickname":"youth","user_id":342},"is_like":2,"like_num":1,"to_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}},{"comment_content":"哈哈","comment_created":1480072185,"comment_id":800,"comment_reply_id":739,"feed_id":1567,"from_user":{"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","brand_info":{"abbre":"B","brand_id":82,"brand_name":"保时捷","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_82_100.png"},"nickname":"吴简","user_id":456},"is_like":2,"like_num":0,"to_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}}],"total":9}
     * is_like : 2
     * like_num : 2
     * to_user : {"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","nickname":"BiBi Car","user_id":389}
     */

    private BrandInfoBean brand_info;
    private String comment_content;
    private int comment_created;
    private int comment_id;
    private int comment_reply_id;
    private int feed_id;
    private FromUserBean from_user;
    private HotListBean hot_list;
    private int is_like;
    private int like_num;
    private ToUserBeanX to_user;

    public BrandInfoBean getBrand_info() {
        return brand_info;
    }

    public void setBrand_info(BrandInfoBean brand_info) {
        this.brand_info = brand_info;
    }

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

    public HotListBean getHot_list() {
        return hot_list;
    }

    public void setHot_list(HotListBean hot_list) {
        this.hot_list = hot_list;
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

    public ToUserBeanX getTo_user() {
        return to_user;
    }

    public void setTo_user(ToUserBeanX to_user) {
        this.to_user = to_user;
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

    public static class HotListBean {
        /**
         * list : [{"comment_content":"👿","comment_created":1479809423,"comment_id":740,"comment_reply_id":739,"feed_id":1567,"from_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","brand_info":{"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"},"nickname":"youth","user_id":342},"is_like":2,"like_num":1,"to_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}},{"comment_content":"哈哈","comment_created":1480072185,"comment_id":800,"comment_reply_id":739,"feed_id":1567,"from_user":{"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","brand_info":{"abbre":"B","brand_id":82,"brand_name":"保时捷","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_82_100.png"},"nickname":"吴简","user_id":456},"is_like":2,"like_num":0,"to_user":{"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}}]
         * total : 9
         */

        private int total;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * comment_content : 👿
             * comment_created : 1479809423
             * comment_id : 740
             * comment_reply_id : 739
             * feed_id : 1567
             * from_user : {"avatar":"http://img.bibicar.cn/bibilogo.png","brand_info":{"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"},"nickname":"youth","user_id":342}
             * is_like : 2
             * like_num : 1
             * to_user : {"avatar":"http://img.bibicar.cn/bibilogo.png","nickname":"youth","user_id":342}
             */

            private String comment_content;
            private int comment_created;
            private int comment_id;
            private int comment_reply_id;
            private int feed_id;
            private FromUserBeanX from_user;
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

            public FromUserBeanX getFrom_user() {
                return from_user;
            }

            public void setFrom_user(FromUserBeanX from_user) {
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

            public static class FromUserBeanX {
                /**
                 * avatar : http://img.bibicar.cn/bibilogo.png
                 * brand_info : {"abbre":"B","brand_id":3,"brand_name":"宝马","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_3_100.png"}
                 * nickname : youth
                 * user_id : 342
                 */

                private String avatar;
                private BrandInfoBeanX brand_info;
                private String nickname;
                private int user_id;

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public BrandInfoBeanX getBrand_info() {
                    return brand_info;
                }

                public void setBrand_info(BrandInfoBeanX brand_info) {
                    this.brand_info = brand_info;
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

                public static class BrandInfoBeanX {
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
    }

    public static class ToUserBeanX {
        /**
         * avatar : http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF
         * nickname : BiBi Car
         * user_id : 389
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
