package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/21 10:57.
 * QQ : 971060378
 * Used as : 话题信息的bean
 */
public class TopicInfoBean extends BaseBean {

    /**
     * created : 1472627369
     * feed_num : 0
     * id : 18
     * is_skip : 2
     * post_file : http://img.bibicar.cn/vrcar.jpg
     * sort : 2
     * theme : #保时捷#
     * title : Drive活动带你释放最初的驾驶激情。还等什么？
     * user_id : 389
     */

    private String created;
    private int feed_num;
    private int id;
    private int is_skip;
    private String post_file;
    private int sort;
    private String theme;
    private String title;
    private int user_id;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getFeed_num() {
        return feed_num;
    }

    public void setFeed_num(int feed_num) {
        this.feed_num = feed_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(int is_skip) {
        this.is_skip = is_skip;
    }

    public String getPost_file() {
        return post_file;
    }

    public void setPost_file(String post_file) {
        this.post_file = post_file;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
