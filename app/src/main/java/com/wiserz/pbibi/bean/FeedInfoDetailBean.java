package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/9/19 17:45.
 * QQ : 971060378
 * Used as : 朋友圈中feed_info
 */
public class FeedInfoDetailBean extends BaseBean {

    /**
     * collect_num : 0
     * comment_num : 0
     * created : 1505783815
     * feed_id : 6154
     * feed_type : 1
     * image_url : []
     * is_collect : 2
     * is_like : 2
     * like_num : 1
     * post_content : 随便发一下
     * post_files : [{"file_id":"3c6c7070-c147-4855-9c5b-a2677a19a007","file_type":1,"file_url":"http://img.bibicar.cn/3c6c7070-c147-4855-9c5b-a2677a19a007"}]
     * post_user_info : {"profile":{"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","nickname":"馨子"},"user_id":454}
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
    private List<?> image_url;
    private List<PostFilesBean> post_files;

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

    public List<?> getImage_url() {
        return image_url;
    }

    public void setImage_url(List<?> image_url) {
        this.image_url = image_url;
    }

    public List<PostFilesBean> getPost_files() {
        return post_files;
    }

    public void setPost_files(List<PostFilesBean> post_files) {
        this.post_files = post_files;
    }

    public static class PostUserInfoBean {
        /**
         * profile : {"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","nickname":"馨子"}
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

    public static class PostFilesBean {
        /**
         * file_id : 3c6c7070-c147-4855-9c5b-a2677a19a007
         * file_type : 1
         * file_url : http://img.bibicar.cn/3c6c7070-c147-4855-9c5b-a2677a19a007
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
