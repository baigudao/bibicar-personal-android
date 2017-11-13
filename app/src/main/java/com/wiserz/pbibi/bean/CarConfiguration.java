package com.wiserz.pbibi.bean;

import java.util.ArrayList;

/**
 * Created by huangzhifeng on 2017/11/13.
 */

public class CarConfiguration extends BaseBean {
    private String type_name;
    private int type_id;
    private ArrayList<Configuration> list;
    public static class Configuration{
        private int id;
        private String name;
        private int type;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public ArrayList<Configuration> getList() {
        return list;
    }

    public void setList(ArrayList<Configuration> list) {
        this.list = list;
    }
}
