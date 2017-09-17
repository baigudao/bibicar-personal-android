package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/14 11:22.
 * QQ : 971060378
 * Used as : 福利的bean
 */
public class FuLiBean extends BaseBean {

    /**
     * _id : 598a5478421aa90ca3bb6bfc
     * createdAt : 2017-08-09T08:16:56.373Z
     * desc : 8-9
     * publishedAt : 2017-08-09T13:49:27.260Z
     * source : chrome
     * type : 福利
     * url : https://ws1.sinaimg.cn/large/610dc034ly1fid5poqfznj20u011imzm.jpg
     * used : true
     * who : daimajia
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
