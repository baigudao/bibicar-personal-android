package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/18 13:12.
 * QQ : 971060378
 * Used as : 维保的拼接bean
 */
public class GuaranteeHistoryBean extends BaseBean {

    /**
     * apply_id : 0
     * brand_id : 4
     * brand_logo : http://octigmfzv.bkt.clouddn.com/brand/4/logo.jpg?v=1473220336
     * brand_name : 东风日产
     * created_at : 1505444298
     * id : 24
     * pay_fee : 0.01
     * pay_type : 1
     * plate :
     * report_sn : 2017091597511015
     * status : 5
     * type : 1
     * user_id : 454
     * vin : LGBP12E21DY196239
     */

    private int apply_id;
    private String brand_id;
    private String brand_logo;
    private String brand_name;
    private String created_at;
    private int id;
    private double pay_fee;
    private int pay_type;
    private String plate;
    private String report_sn;
    private int status;
    private int type;
    private int user_id;
    private String vin;

    public int getApply_id() {
        return apply_id;
    }

    public void setApply_id(int apply_id) {
        this.apply_id = apply_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(double pay_fee) {
        this.pay_fee = pay_fee;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getReport_sn() {
        return report_sn;
    }

    public void setReport_sn(String report_sn) {
        this.report_sn = report_sn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
