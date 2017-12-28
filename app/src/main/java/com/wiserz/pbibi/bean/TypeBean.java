package com.wiserz.pbibi.bean;

/**
 * Created by skylar on 2017/12/16 14:16.
 * Used as : 图片对象
 */
public class TypeBean extends BaseBean{
        /**
         * file_id : Fq4aiXZUGbSnLs1iX8QCfHs549Ah
         * file_type : 1
         * file_url : http://thirtimg.bibicar.cn/13639276_624e2734917_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
         */

        private String file_id;//图片的hash值
        private int file_type;
        private String file_url;

        public TypeBean(){

        }

        public TypeBean(String file_id,int file_type,String file_url){
            this.file_id = file_id;
            this.file_type = file_type;
            this.file_url = file_url;
        }

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
