package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/8/10 14:00.
 * QQ : 971060378
 * Used as : 汽车信息的bean
 */
public class CarInfoBean extends BaseBean {

    /**
     * car_color : 1
     * car_id : 58841af3a0e27
     * car_name : 奔驰 E级 2012款 1.8T 自动 E200L优雅型CGI
     * car_status : 1
     * car_type : 2
     * created : 1484928000
     * fav_num : 0
     * files : [{"file_id":"FqPStp8h6igdGJTVP1MRzD4L16v0","file_type":2,"file_url":"http://thirtimg.bibicar.cn/14021374_4ee78825291_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip"}]
     * is_pacted : 2
     * price : 25.8
     * sales_volume : 0
     * updated : 1484928000
     * visit_num : 1
     */

    private int car_color;
    private String car_id;
    private String car_name;
    private int car_status;
    private int car_type;
    private int created;
    private int fav_num;
    private int is_pacted;
    private double price;
    private int sales_volume;
    private int updated;
    private int visit_num;
    private List<FilesBean> files;

    public int getCar_color() {
        return car_color;
    }

    public void setCar_color(int car_color) {
        this.car_color = car_color;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public int getCar_status() {
        return car_status;
    }

    public void setCar_status(int car_status) {
        this.car_status = car_status;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getFav_num() {
        return fav_num;
    }

    public void setFav_num(int fav_num) {
        this.fav_num = fav_num;
    }

    public int getIs_pacted() {
        return is_pacted;
    }

    public void setIs_pacted(int is_pacted) {
        this.is_pacted = is_pacted;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(int sales_volume) {
        this.sales_volume = sales_volume;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getVisit_num() {
        return visit_num;
    }

    public void setVisit_num(int visit_num) {
        this.visit_num = visit_num;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        /**
         * file_id : FqPStp8h6igdGJTVP1MRzD4L16v0
         * file_type : 2
         * file_url : http://thirtimg.bibicar.cn/14021374_4ee78825291_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
         */

        private String file_id;
        private int file_type;
        private String file_url;

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }
}
