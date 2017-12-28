package com.wiserz.pbibi.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skylar on 2017/12/16 17:31.
 * Used as : 生成报价单后的报价单对象
 */
public class ReportBean extends BaseBean {

    private Info info;
    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public void setShare_txt(String share_txt) {
        this.share_txt = share_txt;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Info getInfo() {
        return info;
    }

    public String getShare_img() {
        return share_img;
    }

    public String getShare_title() {
        return share_title;
    }

    public String getShare_txt() {
        return share_txt;
    }

    public String getShare_url() {
        return share_url;
    }

    public class Info {

        private String bank_account;
        private String bank_name;
        private String bank_no;
        private int board_fee;
        private int brand_id;
        private Brand_info brand_info;
        private String brand_name;
        private int car_color;
        private int car_id;
        private String car_intro;
        private String car_name;
        private String contact_address;
        private String contact_name;
        private String contact_phone;
        private int created;
        private List<Extra_info> extra_info;
        private ArrayList<TypeBean> files;
        private int guide_price;
        private String hash;
        private int id;
        private int insurance_fee;
        private int model_id;
        private String model_name;
        private int other_fee;
        private String other_fee_intro;
        private String promise;
        private int purch_fee;
        private int report_time;
        private int series_id;
        private String series_name;
        private int status;
        private int total_price;
        private int updated;
        private int user_id;
        public void setBank_account(String bank_account) {
            this.bank_account = bank_account;
        }
        public String getBank_account() {
            return bank_account;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }
        public String getBank_name() {
            return bank_name;
        }

        public void setBank_no(String bank_no) {
            this.bank_no = bank_no;
        }
        public String getBank_no() {
            return bank_no;
        }

        public void setBoard_fee(int board_fee) {
            this.board_fee = board_fee;
        }
        public int getBoard_fee() {
            return board_fee;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }
        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_info(Brand_info brand_info) {
            this.brand_info = brand_info;
        }
        public Brand_info getBrand_info() {
            return brand_info;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }
        public String getBrand_name() {
            return brand_name;
        }

        public void setCar_color(int car_color) {
            this.car_color = car_color;
        }
        public int getCar_color() {
            return car_color;
        }

        public void setCar_id(int car_id) {
            this.car_id = car_id;
        }
        public int getCar_id() {
            return car_id;
        }

        public void setCar_intro(String car_intro) {
            this.car_intro = car_intro;
        }
        public String getCar_intro() {
            return car_intro;
        }

        public void setCar_name(String car_name) {
            this.car_name = car_name;
        }
        public String getCar_name() {
            return car_name;
        }

        public void setContact_address(String contact_address) {
            this.contact_address = contact_address;
        }
        public String getContact_address() {
            return contact_address;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }
        public String getContact_name() {
            return contact_name;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }
        public String getContact_phone() {
            return contact_phone;
        }

        public void setCreated(int created) {
            this.created = created;
        }
        public int getCreated() {
            return created;
        }

        public void setExtra_info(List<Extra_info> extra_info) {
            this.extra_info = extra_info;
        }
        public List<Extra_info> getExtra_info() {
            return extra_info;
        }

        public void setFiles(ArrayList<TypeBean> files) {
            this.files = files;
        }
        public ArrayList<TypeBean> getFiles() {
            return files;
        }

        public void setGuide_price(int guide_price) {
            this.guide_price = guide_price;
        }
        public int getGuide_price() {
            return guide_price;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
        public String getHash() {
            return hash;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setInsurance_fee(int insurance_fee) {
            this.insurance_fee = insurance_fee;
        }
        public int getInsurance_fee() {
            return insurance_fee;
        }

        public void setModel_id(int model_id) {
            this.model_id = model_id;
        }
        public int getModel_id() {
            return model_id;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }
        public String getModel_name() {
            return model_name;
        }

        public void setOther_fee(int other_fee) {
            this.other_fee = other_fee;
        }
        public int getOther_fee() {
            return other_fee;
        }

        public void setOther_fee_intro(String other_fee_intro) {
            this.other_fee_intro = other_fee_intro;
        }
        public String getOther_fee_intro() {
            return other_fee_intro;
        }

        public void setPromise(String promise) {
            this.promise = promise;
        }
        public String getPromise() {
            return promise;
        }

        public void setPurch_fee(int purch_fee) {
            this.purch_fee = purch_fee;
        }
        public int getPurch_fee() {
            return purch_fee;
        }

        public void setReport_time(int report_time) {
            this.report_time = report_time;
        }
        public int getReport_time() {
            return report_time;
        }

        public void setSeries_id(int series_id) {
            this.series_id = series_id;
        }
        public int getSeries_id() {
            return series_id;
        }

        public void setSeries_name(String series_name) {
            this.series_name = series_name;
        }
        public String getSeries_name() {
            return series_name;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }
        public int getTotal_price() {
            return total_price;
        }

        public void setUpdated(int updated) {
            this.updated = updated;
        }
        public int getUpdated() {
            return updated;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
        public int getUser_id() {
            return user_id;
        }
    }

    public class Brand_info {

        private String abbre;
        private int brand_id;
        private String brand_name;
        private String brand_url;
        public void setAbbre(String abbre) {
            this.abbre = abbre;
        }
        public String getAbbre() {
            return abbre;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }
        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }
        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_url(String brand_url) {
            this.brand_url = brand_url;
        }
        public String getBrand_url() {
            return brand_url;
        }

    }

    public class Extra_info{
        private String alias;
        private int id;
        private String name;
        private String type;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


//    public class Files {
//
//        private List<TypeBean> type1;
//        private List<TypeBean> type2;
//        private List<TypeBean> type3;
//        private List<TypeBean> type4;
//        public void setType1(List<TypeBean> type1) {
//            this.type1 = type1;
//        }
//        public List<TypeBean> getType1() {
//            return type1;
//        }
//
//        public void setType2(List<TypeBean> type2) {
//            this.type2 = type2;
//        }
//        public List<TypeBean> getType2() {
//            return type2;
//        }
//
//        public void setType3(List<TypeBean> type3) {
//            this.type3 = type3;
//        }
//        public List<TypeBean> getType3() {
//            return type3;
//        }
//
//        public void setType4(List<TypeBean> type4) {
//            this.type4 = type4;
//        }
//        public List<TypeBean> getType4() {
//            return type4;
//        }
//
//    }
}
