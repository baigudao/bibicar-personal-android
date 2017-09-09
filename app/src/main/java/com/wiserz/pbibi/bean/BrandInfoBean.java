package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/9 11:04.
 * QQ : 971060378
 * Used as : 车辆品牌信息
 */
public class BrandInfoBean extends BaseBean {

    private int brand_id;  //品牌id
    private String brand_name;  //品牌名字
    private String brand_url;  //品牌url

    private String abbre;  //首字母
    private int type;//0：表示车牌名；1：表示头部字母

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

    public String getAbbre() {
        return abbre;
    }

    public void setAbbre(String abbre) {
        this.abbre = abbre;
    }

    public String getBrand_url() {
        return brand_url;
    }

    public void setBrand_url(String brand_url) {
        this.brand_url = brand_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
