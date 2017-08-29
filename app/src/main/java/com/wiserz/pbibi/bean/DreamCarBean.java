package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/8/29 10:27.
 * QQ : 971060378
 * Used as : 梦想车bean
 */
public class DreamCarBean extends BaseBean {

    /**
     * brand_info : {"abbre":"L","brand_id":80,"brand_name":"劳斯莱斯","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_80_100.png"}
     * dc_id : 78
     * series_info : {"brand_id":80,"series_id":3884,"series_name":"魅影"}
     */

    private BrandInfoBean brand_info;
    private int dc_id;
    private SeriesInfoBean series_info;

    public BrandInfoBean getBrand_info() {
        return brand_info;
    }

    public void setBrand_info(BrandInfoBean brand_info) {
        this.brand_info = brand_info;
    }

    public int getDc_id() {
        return dc_id;
    }

    public void setDc_id(int dc_id) {
        this.dc_id = dc_id;
    }

    public SeriesInfoBean getSeries_info() {
        return series_info;
    }

    public void setSeries_info(SeriesInfoBean series_info) {
        this.series_info = series_info;
    }

    public static class BrandInfoBean {
        /**
         * abbre : L
         * brand_id : 80
         * brand_name : 劳斯莱斯
         * brand_url : http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_80_100.png
         */

        private String abbre;
        private int brand_id;
        private String brand_name;
        private String brand_url;

        public String getAbbre() {
            return abbre;
        }

        public void setAbbre(String abbre) {
            this.abbre = abbre;
        }

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

        public String getBrand_url() {
            return brand_url;
        }

        public void setBrand_url(String brand_url) {
            this.brand_url = brand_url;
        }
    }

    public static class SeriesInfoBean {
        /**
         * brand_id : 80
         * series_id : 3884
         * series_name : 魅影
         */

        private int brand_id;
        private int series_id;
        private String series_name;

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
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
}
