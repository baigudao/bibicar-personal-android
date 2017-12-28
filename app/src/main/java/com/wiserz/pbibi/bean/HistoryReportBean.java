package com.wiserz.pbibi.bean;

/**
 * Created by skylar on 2017/12/18 17:11.
 * Used as : 历史报价单列表对象
 */

import java.util.List;

public class HistoryReportBean {

    private int has_more;
    private List<HistoryBean> list;
    private int number;
    private int total;

    public void setHas_more(int has_more) {
        this.has_more = has_more;
    }
    public int getHas_more() {
        return has_more;
    }

    public void setList(List<HistoryBean> list) {
        this.list = list;
    }
    public List<HistoryBean> getList() {
        return list;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public class HistoryBean {

        private String brand_name;
        private String car_name;
        private String file_img;
        private int report_id;
        private int report_time;
        private String series_name;
        private String share_img;
        private String share_title;
        private String share_txt;
        private String share_url;
        private int total_price;

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setCar_name(String car_name) {
            this.car_name = car_name;
        }

        public String getCar_name() {
            return car_name;
        }

        public void setFile_img(String file_img) {
            this.file_img = file_img;
        }

        public String getFile_img() {
            return file_img;
        }

        public void setReport_id(int report_id) {
            this.report_id = report_id;
        }

        public int getReport_id() {
            return report_id;
        }

        public void setReport_time(int report_time) {
            this.report_time = report_time;
        }

        public int getReport_time() {
            return report_time;
        }

        public void setSeries_name(String series_name) {
            this.series_name = series_name;
        }

        public String getSeries_name() {
            return series_name;
        }

        public void setShare_img(String share_img) {
            this.share_img = share_img;
        }

        public String getShare_img() {
            return share_img;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_txt(String share_txt) {
            this.share_txt = share_txt;
        }

        public String getShare_txt() {
            return share_txt;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getTotal_price() {
            return total_price;
        }

    }

    }
