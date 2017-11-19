package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/9 16:18.
 * QQ : 971060378
 * Used as : 登录返回的数据bean
 */
public class LoginBean extends BaseBean {

    /**
     * session_id : session598ac2ba475d3
     * user_info : {"chat_token":"LN+OYZtcEtKv5hUnTlTOuf+JRJS5sthGyHSacCSG/XWMRMERMm0lCMNP+bq2l8TRmAShFuKUJDs=","created":1467128731,"mobile":"bibi06","profile":{"age":0,"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","balance":0,"bibi_no":10454,"company":0,"constellation":"","gender":2,"nickname":"馨子","signature":"古月","type":1},"user_id":454,"username":"bibi_osvy0w"}
     */

    private int is_bind_mobile;
    private String session_id;
    private UserInfoBean user_info;

    public int getIs_bind_mobile() {
        return is_bind_mobile;
    }

    public void setIs_bind_mobile(int is_bind_mobile) {
        this.is_bind_mobile = is_bind_mobile;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {
        /**
         * chat_token : LN+OYZtcEtKv5hUnTlTOuf+JRJS5sthGyHSacCSG/XWMRMERMm0lCMNP+bq2l8TRmAShFuKUJDs=
         * created : 1467128731
         * mobile : bibi06
         * profile : {"age":0,"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","balance":0,"bibi_no":10454,"company":0,"constellation":"","gender":2,"nickname":"馨子","signature":"古月","type":1}
         * user_id : 454
         * username : bibi_osvy0w
         */

        private String chat_token;
        private int created;
        private String mobile;
        private ProfileBean profile;
        private int user_id;
        private String username;

        public String getChat_token() {
            return chat_token;
        }

        public void setChat_token(String chat_token) {
            this.chat_token = chat_token;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class ProfileBean {
            /**
             * age : 0
             * avatar : http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k
             * balance : 0
             * bibi_no : 10454
             * company : 0
             * constellation :
             * gender : 2
             * nickname : 馨子
             * signature : 古月
             * type : 1
             */

            private int age;
            private String avatar;
            private int balance;
            private int bibi_no;
            private int company;
            private String constellation;
            private int gender;
            private String nickname;
            private String signature;
            private int type;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }

            public int getBibi_no() {
                return bibi_no;
            }

            public void setBibi_no(int bibi_no) {
                this.bibi_no = bibi_no;
            }

            public int getCompany() {
                return company;
            }

            public void setCompany(int company) {
                this.company = company;
            }

            public String getConstellation() {
                return constellation;
            }

            public void setConstellation(String constellation) {
                this.constellation = constellation;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
