package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/23 17:45.
 * QQ : 971060378
 * Used as : 车辆系列的bean
 */
public class CarSeriesBean extends BaseBean {

    /**
     * brand_id : 97
     * makename : 进口阿斯顿·马丁
     * series_id : 2621
     * series_name : DB9 进口阿斯顿·马丁
     */

    private int brand_id;
    private String makename;
    private int series_id;
    private String series_name;

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getMakename() {
        return makename;
    }

    public void setMakename(String makename) {
        this.makename = makename;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }
}
