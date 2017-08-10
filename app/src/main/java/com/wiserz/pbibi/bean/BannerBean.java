package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/10 11:27.
 * QQ : 971060378
 * Used as : 轮播图的bean
 */
public class BannerBean extends BaseBean {

    /**
     * appUrl : #岁月神偷#
     * imgUrl : http://img.bibicar.cn/theme-sui.jpg
     * link : /topic/8
     * title : 岁月是一场有去无回的旅行，走过的路，错过的人，遇见的事，好的坏的都是风景。
     * type : 8
     */

    private String appUrl;
    private String imgUrl;
    private String link;
    private String title;
    private String type;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
