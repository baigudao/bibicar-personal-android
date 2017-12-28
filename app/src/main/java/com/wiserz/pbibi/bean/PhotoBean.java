package com.wiserz.pbibi.bean;

import java.io.File;

/**
 * Created by jackie on 2017/12/21 11:48.
 * QQ : 971060378
 * Used as : 图片
 */
public class PhotoBean extends BaseBean {

    private File file;//图片对应的文件
    private int pos;//对应于12种类型中的位置

    public PhotoBean(){}

    public PhotoBean(File file,int pos){
        this.file = file;
        this.pos = pos;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
