package com.wiserz.pbibi.bean;

/**
 * Created by 97106 on 2017/9/23.
 */

public class CarModelBean extends BaseBean {

    /**
     * model_id : 114811
     * model_name : 2015 4.4L V8
     * series_id : 3995
     */

    private int model_id;
    private String model_name;
    private String series_id;

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }
}
