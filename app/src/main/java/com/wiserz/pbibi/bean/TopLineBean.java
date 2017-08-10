package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/10 13:35.
 * QQ : 971060378
 * Used as : 头条数据的bean
 */
public class TopLineBean extends BaseBean {

    /**
     * content : :出卖灵魂并不可悲，可悲的是不能卖个好价钱~！
     * img_url : http://img.bibicar.cn/bibilogo.png
     */
    private String content;
    private String img_url;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}