package com.wiserz.pbibi.bean;

import java.io.File;

/**
 * Created by jackie on 2017/9/8 13:53.
 * QQ : 971060378
 * Used as : 上传图片的信息bean
 */
public class UploadCarPhotoInfo extends BaseBean {

    private int file_type;//车辆照片类型 (1:外观 2:中控内饰 3:发动机及结构 4:更多细节)
    private File file;

    public int getFile_type() {
        return file_type;
    }

    public void setFile_type(int file_type) {
        this.file_type = file_type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
