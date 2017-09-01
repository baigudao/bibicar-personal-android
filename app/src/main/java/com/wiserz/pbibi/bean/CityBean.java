package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/1 14:29.
 * QQ : 971060378
 * Used as : 城市的bean
 */
public class CityBean extends BaseBean {

    private String city_name;
    private String city_code;
    private String abbr;
    private int engineno;
    private int classno;
    private String pinyin;

    public CityBean() {
    }

    public CityBean(String city_name, String pinyin) {
        this.city_name = city_name;
        this.pinyin = pinyin;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public int getEngineno() {
        return engineno;
    }

    public void setEngineno(int engineno) {
        this.engineno = engineno;
    }

    public int getClassno() {
        return classno;
    }

    public void setClassno(int classno) {
        this.classno = classno;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}

