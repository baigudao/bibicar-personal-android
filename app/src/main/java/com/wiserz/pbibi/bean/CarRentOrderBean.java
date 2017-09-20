package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/15 17:23.
 * QQ : 971060378
 * Used as : 汽车租赁订单的bean
 */
public class CarRentOrderBean extends BaseBean {

    /**
     * car_id : 5969d55b09904
     * created_at : 1505888544
     * created_by : 454
     * id : 63
     * order_sn : 2017092048525255
     * pay_fee : 0
     * pay_type : 0
     * rental_time_end : 07-1318:00
     * rental_time_start : 07-1318:00
     * status : 1
     * total_price : 0.01
     * user_id : 454
     */

    private String car_id;
    private String created_at;
    private String created_by;
    private int id;
    private String order_sn;
    private int pay_fee;
    private int pay_type;
    private String rental_time_end;
    private String rental_time_start;
    private int status;
    private double total_price;
    private int user_id;

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(int pay_fee) {
        this.pay_fee = pay_fee;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getRental_time_end() {
        return rental_time_end;
    }

    public void setRental_time_end(String rental_time_end) {
        this.rental_time_end = rental_time_end;
    }

    public String getRental_time_start() {
        return rental_time_start;
    }

    public void setRental_time_start(String rental_time_start) {
        this.rental_time_start = rental_time_start;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
