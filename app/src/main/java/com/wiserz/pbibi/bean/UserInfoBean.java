package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/4 13:49.
 * QQ : 971060378
 * Used as : 用户信息
 */
public class UserInfoBean extends BaseBean {

    /**
     * created : 1467128731
     * mobile : bibi06
     * profile : {"age":0,"avatar":"http://img.bibicar.cn/FqKNZJ3khFC9SHdJWTwQ7QYsVW3k","balance":0,"bibi_no":10454,"company":0,"constellation":"","gender":2,"nickname":"馨子","signature":"古月","sort":953,"type":1}
     * total_car : 1
     * total_money : 55.06
     * user_id : 454
     * username : bibi_osvy0w
     */

    private int created;
    private String mobile;
    private ProfileBean profile;
    private int total_car;
    private double total_money;
    private int user_id;
    private String username;

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

    public int getTotal_car() {
        return total_car;
    }

    public void setTotal_car(int total_car) {
        this.total_car = total_car;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
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
         * sort : 953
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
        private int sort;
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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
