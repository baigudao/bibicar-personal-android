package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/9/14 14:21.
 * QQ : 971060378
 * Used as : 违章记录的bean
 */
public class PeccancyRecordBean extends BaseBean {

    /**
     * city : GD_GZ
     * hphm : 粤AN324Y
     * hpzl : 02
     * lists : [{"act":"驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的机动车行驶超过规定时速50%以上的","area":"白云大道南新体育馆路段","code":"","date":"2016-08-11 13:41:00","fen":"12","handled":"0","money":"1000"}]
     * province : GD
     */

    private String city;
    private String hphm;
    private String hpzl;
    private String province;
    private List<ListsBean> lists;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * act : 驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的机动车行驶超过规定时速50%以上的
         * area : 白云大道南新体育馆路段
         * code :
         * date : 2016-08-11 13:41:00
         * fen : 12
         * handled : 0
         * money : 1000
         */

        private String act;
        private String area;
        private String code;
        private String date;
        private String fen;
        private String handled;
        private String money;

        public String getAct() {
            return act;
        }

        public void setAct(String act) {
            this.act = act;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getFen() {
            return fen;
        }

        public void setFen(String fen) {
            this.fen = fen;
        }

        public String getHandled() {
            return handled;
        }

        public void setHandled(String handled) {
            this.handled = handled;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
