package com.wiserz.pbibi.bean;

import java.util.List;

/**
 * Created by jackie on 2017/8/10 17:19.
 * QQ : 971060378
 * Used as : 车行bean，用于推荐页面中的车行列表
 */
public class CheHangBean extends BaseBean {

    /**
     * avatar : http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF
     * car_list : {"car_list":[{"car_info":{"board_time":"2014","brand_info":{"abbre":"A","brand_id":97,"brand_name":"阿斯顿·马丁","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_97_100.png"},"car_color":2,"car_id":"583c0b1028d96","car_intro":"雨","car_name":"阿斯顿·马丁 DB9 2014 6.0L 敞篷百年纪念版","car_no":"000000","car_status":1,"car_time":"17天前","car_type":1,"check_expiration_time":"2014-11-01","city_info":{"city_id":93,"city_lat":360,"city_lng":360,"city_name":"深圳"},"contact_address":"78","contact_name":"吧","contact_phone":"8999","created":1480330000,"displacement":"","engine_no":"999","exchange_time":0,"fav_num":0,"files":[{"file_id":"0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3","file_type":"4","file_url":"http://img.bibicar.cn/0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3?imageMogr2/auto-orient/thumbnail/1000x/strip"}],"gearbox":"","guide_price":0,"insurance_due_time":"2013-11-01","is_fav":2,"is_like":2,"is_pacted":2,"is_transfer":2,"maintain":1,"mileage":"20","model_detail":{"CarReferPrice":"438.00万","Engine_EnvirStandard":"欧4","Engine_ExhaustForFloat":"6.0 L","Engine_InhaleType":"无","Engine_Location":"前置","Engine_MaxNJ":"620 N·m","Engine_Type":"AM11","Engine_horsepower":"517 Ps","Oil_FuelCapacity":"78 L","Oil_FuelTab":"","Oil_FuelType":"汽油","Oil_SupplyType":"多点电喷","OutSet_Height":"1282 mm","OutSet_Length":"4720 mm","OutSet_WheelBase":"2740 mm","OutSet_Width":"2061 mm","Perf_AccelerateTime":"4.60 s","Perf_DriveType":"后轮驱动","Perf_MaxSpeed":"295","Perf_SeatNum":"4 个","UnderPan_ForwardGearNum":"6 挡 手自一体","id":2501,"model_id":106019},"model_info":{"model_id":106019,"model_name":"2014 6.0L 敞篷百年纪念版","model_year":"2014","series_id":2621},"platform_info":{},"price":20,"sales_volume":0,"series_info":{"brand_id":97,"series_id":2621,"series_name":"DB9"},"style":0,"updated":1480330000,"user_info":{"created":0,"mobile":"","profile":{"age":0,"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","constellation":"","gender":0,"nickname":"吴简","signature":"","type":2},"user_id":456,"username":""},"verify_status":2,"vin_file":"","vin_no":"999","visit_num":3,"vr_url":""},"car_users":[]}],"has_more":2,"total":1,"user_list":[]}
     * intro :
     * nickname : 吴简
     * saling_num : 1
     * signature :
     * sold_num : 0
     * sort : 912
     * tag : 1
     * type : 2
     * user_id : 456
     */

    private String avatar;
    private CarListBeanX car_list;
    private String intro;
    private String nickname;
    private int saling_num;
    private String signature;
    private int sold_num;
    private int sort;
    private int tag;
    private int type;
    private int user_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public CarListBeanX getCar_list() {
        return car_list;
    }

    public void setCar_list(CarListBeanX car_list) {
        this.car_list = car_list;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSaling_num() {
        return saling_num;
    }

    public void setSaling_num(int saling_num) {
        this.saling_num = saling_num;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static class CarListBeanX {
        /**
         * car_list : [{"car_info":{"board_time":"2014","brand_info":{"abbre":"A","brand_id":97,"brand_name":"阿斯顿·马丁","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_97_100.png"},"car_color":2,"car_id":"583c0b1028d96","car_intro":"雨","car_name":"阿斯顿·马丁 DB9 2014 6.0L 敞篷百年纪念版","car_no":"000000","car_status":1,"car_time":"17天前","car_type":1,"check_expiration_time":"2014-11-01","city_info":{"city_id":93,"city_lat":360,"city_lng":360,"city_name":"深圳"},"contact_address":"78","contact_name":"吧","contact_phone":"8999","created":1480330000,"displacement":"","engine_no":"999","exchange_time":0,"fav_num":0,"files":[{"file_id":"0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3","file_type":"4","file_url":"http://img.bibicar.cn/0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3?imageMogr2/auto-orient/thumbnail/1000x/strip"}],"gearbox":"","guide_price":0,"insurance_due_time":"2013-11-01","is_fav":2,"is_like":2,"is_pacted":2,"is_transfer":2,"maintain":1,"mileage":"20","model_detail":{"CarReferPrice":"438.00万","Engine_EnvirStandard":"欧4","Engine_ExhaustForFloat":"6.0 L","Engine_InhaleType":"无","Engine_Location":"前置","Engine_MaxNJ":"620 N·m","Engine_Type":"AM11","Engine_horsepower":"517 Ps","Oil_FuelCapacity":"78 L","Oil_FuelTab":"","Oil_FuelType":"汽油","Oil_SupplyType":"多点电喷","OutSet_Height":"1282 mm","OutSet_Length":"4720 mm","OutSet_WheelBase":"2740 mm","OutSet_Width":"2061 mm","Perf_AccelerateTime":"4.60 s","Perf_DriveType":"后轮驱动","Perf_MaxSpeed":"295","Perf_SeatNum":"4 个","UnderPan_ForwardGearNum":"6 挡 手自一体","id":2501,"model_id":106019},"model_info":{"model_id":106019,"model_name":"2014 6.0L 敞篷百年纪念版","model_year":"2014","series_id":2621},"platform_info":{},"price":20,"sales_volume":0,"series_info":{"brand_id":97,"series_id":2621,"series_name":"DB9"},"style":0,"updated":1480330000,"user_info":{"created":0,"mobile":"","profile":{"age":0,"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","constellation":"","gender":0,"nickname":"吴简","signature":"","type":2},"user_id":456,"username":""},"verify_status":2,"vin_file":"","vin_no":"999","visit_num":3,"vr_url":""},"car_users":[]}]
         * has_more : 2
         * total : 1
         * user_list : []
         */

        private int has_more;
        private int total;
        private List<CarListBean> car_list;
        private List<?> user_list;

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<CarListBean> getCar_list() {
            return car_list;
        }

        public void setCar_list(List<CarListBean> car_list) {
            this.car_list = car_list;
        }

        public List<?> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<?> user_list) {
            this.user_list = user_list;
        }

        public static class CarListBean {
            /**
             * car_info : {"board_time":"2014","brand_info":{"abbre":"A","brand_id":97,"brand_name":"阿斯顿·马丁","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_97_100.png"},"car_color":2,"car_id":"583c0b1028d96","car_intro":"雨","car_name":"阿斯顿·马丁 DB9 2014 6.0L 敞篷百年纪念版","car_no":"000000","car_status":1,"car_time":"17天前","car_type":1,"check_expiration_time":"2014-11-01","city_info":{"city_id":93,"city_lat":360,"city_lng":360,"city_name":"深圳"},"contact_address":"78","contact_name":"吧","contact_phone":"8999","created":1480330000,"displacement":"","engine_no":"999","exchange_time":0,"fav_num":0,"files":[{"file_id":"0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3","file_type":"4","file_url":"http://img.bibicar.cn/0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3?imageMogr2/auto-orient/thumbnail/1000x/strip"}],"gearbox":"","guide_price":0,"insurance_due_time":"2013-11-01","is_fav":2,"is_like":2,"is_pacted":2,"is_transfer":2,"maintain":1,"mileage":"20","model_detail":{"CarReferPrice":"438.00万","Engine_EnvirStandard":"欧4","Engine_ExhaustForFloat":"6.0 L","Engine_InhaleType":"无","Engine_Location":"前置","Engine_MaxNJ":"620 N·m","Engine_Type":"AM11","Engine_horsepower":"517 Ps","Oil_FuelCapacity":"78 L","Oil_FuelTab":"","Oil_FuelType":"汽油","Oil_SupplyType":"多点电喷","OutSet_Height":"1282 mm","OutSet_Length":"4720 mm","OutSet_WheelBase":"2740 mm","OutSet_Width":"2061 mm","Perf_AccelerateTime":"4.60 s","Perf_DriveType":"后轮驱动","Perf_MaxSpeed":"295","Perf_SeatNum":"4 个","UnderPan_ForwardGearNum":"6 挡 手自一体","id":2501,"model_id":106019},"model_info":{"model_id":106019,"model_name":"2014 6.0L 敞篷百年纪念版","model_year":"2014","series_id":2621},"platform_info":{},"price":20,"sales_volume":0,"series_info":{"brand_id":97,"series_id":2621,"series_name":"DB9"},"style":0,"updated":1480330000,"user_info":{"created":0,"mobile":"","profile":{"age":0,"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","constellation":"","gender":0,"nickname":"吴简","signature":"","type":2},"user_id":456,"username":""},"verify_status":2,"vin_file":"","vin_no":"999","visit_num":3,"vr_url":""}
             * car_users : []
             */

            private CarInfoBean car_info;
            private List<?> car_users;

            public CarInfoBean getCar_info() {
                return car_info;
            }

            public void setCar_info(CarInfoBean car_info) {
                this.car_info = car_info;
            }

            public List<?> getCar_users() {
                return car_users;
            }

            public void setCar_users(List<?> car_users) {
                this.car_users = car_users;
            }

            public static class CarInfoBean {
                /**
                 * board_time : 2014
                 * brand_info : {"abbre":"A","brand_id":97,"brand_name":"阿斯顿·马丁","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_97_100.png"}
                 * car_color : 2
                 * car_id : 583c0b1028d96
                 * car_intro : 雨
                 * car_name : 阿斯顿·马丁 DB9 2014 6.0L 敞篷百年纪念版
                 * car_no : 000000
                 * car_status : 1
                 * car_time : 17天前
                 * car_type : 1
                 * check_expiration_time : 2014-11-01
                 * city_info : {"city_id":93,"city_lat":360,"city_lng":360,"city_name":"深圳"}
                 * contact_address : 78
                 * contact_name : 吧
                 * contact_phone : 8999
                 * created : 1480330000
                 * displacement :
                 * engine_no : 999
                 * exchange_time : 0
                 * fav_num : 0
                 * files : [{"file_id":"0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3","file_type":"4","file_url":"http://img.bibicar.cn/0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3?imageMogr2/auto-orient/thumbnail/1000x/strip"}]
                 * gearbox :
                 * guide_price : 0
                 * insurance_due_time : 2013-11-01
                 * is_fav : 2
                 * is_like : 2
                 * is_pacted : 2
                 * is_transfer : 2
                 * maintain : 1
                 * mileage : 20
                 * model_detail : {"CarReferPrice":"438.00万","Engine_EnvirStandard":"欧4","Engine_ExhaustForFloat":"6.0 L","Engine_InhaleType":"无","Engine_Location":"前置","Engine_MaxNJ":"620 N·m","Engine_Type":"AM11","Engine_horsepower":"517 Ps","Oil_FuelCapacity":"78 L","Oil_FuelTab":"","Oil_FuelType":"汽油","Oil_SupplyType":"多点电喷","OutSet_Height":"1282 mm","OutSet_Length":"4720 mm","OutSet_WheelBase":"2740 mm","OutSet_Width":"2061 mm","Perf_AccelerateTime":"4.60 s","Perf_DriveType":"后轮驱动","Perf_MaxSpeed":"295","Perf_SeatNum":"4 个","UnderPan_ForwardGearNum":"6 挡 手自一体","id":2501,"model_id":106019}
                 * model_info : {"model_id":106019,"model_name":"2014 6.0L 敞篷百年纪念版","model_year":"2014","series_id":2621}
                 * platform_info : {}
                 * price : 20
                 * sales_volume : 0
                 * series_info : {"brand_id":97,"series_id":2621,"series_name":"DB9"}
                 * style : 0
                 * updated : 1480330000
                 * user_info : {"created":0,"mobile":"","profile":{"age":0,"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","constellation":"","gender":0,"nickname":"吴简","signature":"","type":2},"user_id":456,"username":""}
                 * verify_status : 2
                 * vin_file :
                 * vin_no : 999
                 * visit_num : 3
                 * vr_url :
                 */

                private String board_time;
                private BrandInfoBean brand_info;
                private int car_color;
                private String car_id;
                private String car_intro;
                private String car_name;
                private String car_no;
                private int car_status;
                private String car_time;
                private int car_type;
                private String check_expiration_time;
                private CityInfoBean city_info;
                private String contact_address;
                private String contact_name;
                private String contact_phone;
                private int created;
                private String displacement;
                private String engine_no;
                private int exchange_time;
                private int fav_num;
                private String gearbox;
                private int guide_price;
                private String insurance_due_time;
                private int is_fav;
                private int is_like;
                private int is_pacted;
                private int is_transfer;
                private int maintain;
                private String mileage;
                private ModelDetailBean model_detail;
                private ModelInfoBean model_info;
                private PlatformInfoBean platform_info;
                private int price;
                private int sales_volume;
                private SeriesInfoBean series_info;
                private int style;
                private int updated;
                private UserInfoBean user_info;
                private int verify_status;
                private String vin_file;
                private String vin_no;
                private int visit_num;
                private String vr_url;
                private List<FilesBean> files;

                public String getBoard_time() {
                    return board_time;
                }

                public void setBoard_time(String board_time) {
                    this.board_time = board_time;
                }

                public BrandInfoBean getBrand_info() {
                    return brand_info;
                }

                public void setBrand_info(BrandInfoBean brand_info) {
                    this.brand_info = brand_info;
                }

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

                public String getCar_intro() {
                    return car_intro;
                }

                public void setCar_intro(String car_intro) {
                    this.car_intro = car_intro;
                }

                public String getCar_name() {
                    return car_name;
                }

                public void setCar_name(String car_name) {
                    this.car_name = car_name;
                }

                public String getCar_no() {
                    return car_no;
                }

                public void setCar_no(String car_no) {
                    this.car_no = car_no;
                }

                public int getCar_status() {
                    return car_status;
                }

                public void setCar_status(int car_status) {
                    this.car_status = car_status;
                }

                public String getCar_time() {
                    return car_time;
                }

                public void setCar_time(String car_time) {
                    this.car_time = car_time;
                }

                public int getCar_type() {
                    return car_type;
                }

                public void setCar_type(int car_type) {
                    this.car_type = car_type;
                }

                public String getCheck_expiration_time() {
                    return check_expiration_time;
                }

                public void setCheck_expiration_time(String check_expiration_time) {
                    this.check_expiration_time = check_expiration_time;
                }

                public CityInfoBean getCity_info() {
                    return city_info;
                }

                public void setCity_info(CityInfoBean city_info) {
                    this.city_info = city_info;
                }

                public String getContact_address() {
                    return contact_address;
                }

                public void setContact_address(String contact_address) {
                    this.contact_address = contact_address;
                }

                public String getContact_name() {
                    return contact_name;
                }

                public void setContact_name(String contact_name) {
                    this.contact_name = contact_name;
                }

                public String getContact_phone() {
                    return contact_phone;
                }

                public void setContact_phone(String contact_phone) {
                    this.contact_phone = contact_phone;
                }

                public int getCreated() {
                    return created;
                }

                public void setCreated(int created) {
                    this.created = created;
                }

                public String getDisplacement() {
                    return displacement;
                }

                public void setDisplacement(String displacement) {
                    this.displacement = displacement;
                }

                public String getEngine_no() {
                    return engine_no;
                }

                public void setEngine_no(String engine_no) {
                    this.engine_no = engine_no;
                }

                public int getExchange_time() {
                    return exchange_time;
                }

                public void setExchange_time(int exchange_time) {
                    this.exchange_time = exchange_time;
                }

                public int getFav_num() {
                    return fav_num;
                }

                public void setFav_num(int fav_num) {
                    this.fav_num = fav_num;
                }

                public String getGearbox() {
                    return gearbox;
                }

                public void setGearbox(String gearbox) {
                    this.gearbox = gearbox;
                }

                public int getGuide_price() {
                    return guide_price;
                }

                public void setGuide_price(int guide_price) {
                    this.guide_price = guide_price;
                }

                public String getInsurance_due_time() {
                    return insurance_due_time;
                }

                public void setInsurance_due_time(String insurance_due_time) {
                    this.insurance_due_time = insurance_due_time;
                }

                public int getIs_fav() {
                    return is_fav;
                }

                public void setIs_fav(int is_fav) {
                    this.is_fav = is_fav;
                }

                public int getIs_like() {
                    return is_like;
                }

                public void setIs_like(int is_like) {
                    this.is_like = is_like;
                }

                public int getIs_pacted() {
                    return is_pacted;
                }

                public void setIs_pacted(int is_pacted) {
                    this.is_pacted = is_pacted;
                }

                public int getIs_transfer() {
                    return is_transfer;
                }

                public void setIs_transfer(int is_transfer) {
                    this.is_transfer = is_transfer;
                }

                public int getMaintain() {
                    return maintain;
                }

                public void setMaintain(int maintain) {
                    this.maintain = maintain;
                }

                public String getMileage() {
                    return mileage;
                }

                public void setMileage(String mileage) {
                    this.mileage = mileage;
                }

                public ModelDetailBean getModel_detail() {
                    return model_detail;
                }

                public void setModel_detail(ModelDetailBean model_detail) {
                    this.model_detail = model_detail;
                }

                public ModelInfoBean getModel_info() {
                    return model_info;
                }

                public void setModel_info(ModelInfoBean model_info) {
                    this.model_info = model_info;
                }

                public PlatformInfoBean getPlatform_info() {
                    return platform_info;
                }

                public void setPlatform_info(PlatformInfoBean platform_info) {
                    this.platform_info = platform_info;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public int getSales_volume() {
                    return sales_volume;
                }

                public void setSales_volume(int sales_volume) {
                    this.sales_volume = sales_volume;
                }

                public SeriesInfoBean getSeries_info() {
                    return series_info;
                }

                public void setSeries_info(SeriesInfoBean series_info) {
                    this.series_info = series_info;
                }

                public int getStyle() {
                    return style;
                }

                public void setStyle(int style) {
                    this.style = style;
                }

                public int getUpdated() {
                    return updated;
                }

                public void setUpdated(int updated) {
                    this.updated = updated;
                }

                public UserInfoBean getUser_info() {
                    return user_info;
                }

                public void setUser_info(UserInfoBean user_info) {
                    this.user_info = user_info;
                }

                public int getVerify_status() {
                    return verify_status;
                }

                public void setVerify_status(int verify_status) {
                    this.verify_status = verify_status;
                }

                public String getVin_file() {
                    return vin_file;
                }

                public void setVin_file(String vin_file) {
                    this.vin_file = vin_file;
                }

                public String getVin_no() {
                    return vin_no;
                }

                public void setVin_no(String vin_no) {
                    this.vin_no = vin_no;
                }

                public int getVisit_num() {
                    return visit_num;
                }

                public void setVisit_num(int visit_num) {
                    this.visit_num = visit_num;
                }

                public String getVr_url() {
                    return vr_url;
                }

                public void setVr_url(String vr_url) {
                    this.vr_url = vr_url;
                }

                public List<FilesBean> getFiles() {
                    return files;
                }

                public void setFiles(List<FilesBean> files) {
                    this.files = files;
                }

                public static class BrandInfoBean {
                    /**
                     * abbre : A
                     * brand_id : 97
                     * brand_name : 阿斯顿·马丁
                     * brand_url : http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_97_100.png
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

                public static class CityInfoBean {
                    /**
                     * city_id : 93
                     * city_lat : 360
                     * city_lng : 360
                     * city_name : 深圳
                     */

                    private int city_id;
                    private int city_lat;
                    private int city_lng;
                    private String city_name;

                    public int getCity_id() {
                        return city_id;
                    }

                    public void setCity_id(int city_id) {
                        this.city_id = city_id;
                    }

                    public int getCity_lat() {
                        return city_lat;
                    }

                    public void setCity_lat(int city_lat) {
                        this.city_lat = city_lat;
                    }

                    public int getCity_lng() {
                        return city_lng;
                    }

                    public void setCity_lng(int city_lng) {
                        this.city_lng = city_lng;
                    }

                    public String getCity_name() {
                        return city_name;
                    }

                    public void setCity_name(String city_name) {
                        this.city_name = city_name;
                    }
                }

                public static class ModelDetailBean {
                    /**
                     * CarReferPrice : 438.00万
                     * Engine_EnvirStandard : 欧4
                     * Engine_ExhaustForFloat : 6.0 L
                     * Engine_InhaleType : 无
                     * Engine_Location : 前置
                     * Engine_MaxNJ : 620 N·m
                     * Engine_Type : AM11
                     * Engine_horsepower : 517 Ps
                     * Oil_FuelCapacity : 78 L
                     * Oil_FuelTab :
                     * Oil_FuelType : 汽油
                     * Oil_SupplyType : 多点电喷
                     * OutSet_Height : 1282 mm
                     * OutSet_Length : 4720 mm
                     * OutSet_WheelBase : 2740 mm
                     * OutSet_Width : 2061 mm
                     * Perf_AccelerateTime : 4.60 s
                     * Perf_DriveType : 后轮驱动
                     * Perf_MaxSpeed : 295
                     * Perf_SeatNum : 4 个
                     * UnderPan_ForwardGearNum : 6 挡 手自一体
                     * id : 2501
                     * model_id : 106019
                     */

                    private String CarReferPrice;
                    private String Engine_EnvirStandard;
                    private String Engine_ExhaustForFloat;
                    private String Engine_InhaleType;
                    private String Engine_Location;
                    private String Engine_MaxNJ;
                    private String Engine_Type;
                    private String Engine_horsepower;
                    private String Oil_FuelCapacity;
                    private String Oil_FuelTab;
                    private String Oil_FuelType;
                    private String Oil_SupplyType;
                    private String OutSet_Height;
                    private String OutSet_Length;
                    private String OutSet_WheelBase;
                    private String OutSet_Width;
                    private String Perf_AccelerateTime;
                    private String Perf_DriveType;
                    private String Perf_MaxSpeed;
                    private String Perf_SeatNum;
                    private String UnderPan_ForwardGearNum;
                    private int id;
                    private int model_id;

                    public String getCarReferPrice() {
                        return CarReferPrice;
                    }

                    public void setCarReferPrice(String CarReferPrice) {
                        this.CarReferPrice = CarReferPrice;
                    }

                    public String getEngine_EnvirStandard() {
                        return Engine_EnvirStandard;
                    }

                    public void setEngine_EnvirStandard(String Engine_EnvirStandard) {
                        this.Engine_EnvirStandard = Engine_EnvirStandard;
                    }

                    public String getEngine_ExhaustForFloat() {
                        return Engine_ExhaustForFloat;
                    }

                    public void setEngine_ExhaustForFloat(String Engine_ExhaustForFloat) {
                        this.Engine_ExhaustForFloat = Engine_ExhaustForFloat;
                    }

                    public String getEngine_InhaleType() {
                        return Engine_InhaleType;
                    }

                    public void setEngine_InhaleType(String Engine_InhaleType) {
                        this.Engine_InhaleType = Engine_InhaleType;
                    }

                    public String getEngine_Location() {
                        return Engine_Location;
                    }

                    public void setEngine_Location(String Engine_Location) {
                        this.Engine_Location = Engine_Location;
                    }

                    public String getEngine_MaxNJ() {
                        return Engine_MaxNJ;
                    }

                    public void setEngine_MaxNJ(String Engine_MaxNJ) {
                        this.Engine_MaxNJ = Engine_MaxNJ;
                    }

                    public String getEngine_Type() {
                        return Engine_Type;
                    }

                    public void setEngine_Type(String Engine_Type) {
                        this.Engine_Type = Engine_Type;
                    }

                    public String getEngine_horsepower() {
                        return Engine_horsepower;
                    }

                    public void setEngine_horsepower(String Engine_horsepower) {
                        this.Engine_horsepower = Engine_horsepower;
                    }

                    public String getOil_FuelCapacity() {
                        return Oil_FuelCapacity;
                    }

                    public void setOil_FuelCapacity(String Oil_FuelCapacity) {
                        this.Oil_FuelCapacity = Oil_FuelCapacity;
                    }

                    public String getOil_FuelTab() {
                        return Oil_FuelTab;
                    }

                    public void setOil_FuelTab(String Oil_FuelTab) {
                        this.Oil_FuelTab = Oil_FuelTab;
                    }

                    public String getOil_FuelType() {
                        return Oil_FuelType;
                    }

                    public void setOil_FuelType(String Oil_FuelType) {
                        this.Oil_FuelType = Oil_FuelType;
                    }

                    public String getOil_SupplyType() {
                        return Oil_SupplyType;
                    }

                    public void setOil_SupplyType(String Oil_SupplyType) {
                        this.Oil_SupplyType = Oil_SupplyType;
                    }

                    public String getOutSet_Height() {
                        return OutSet_Height;
                    }

                    public void setOutSet_Height(String OutSet_Height) {
                        this.OutSet_Height = OutSet_Height;
                    }

                    public String getOutSet_Length() {
                        return OutSet_Length;
                    }

                    public void setOutSet_Length(String OutSet_Length) {
                        this.OutSet_Length = OutSet_Length;
                    }

                    public String getOutSet_WheelBase() {
                        return OutSet_WheelBase;
                    }

                    public void setOutSet_WheelBase(String OutSet_WheelBase) {
                        this.OutSet_WheelBase = OutSet_WheelBase;
                    }

                    public String getOutSet_Width() {
                        return OutSet_Width;
                    }

                    public void setOutSet_Width(String OutSet_Width) {
                        this.OutSet_Width = OutSet_Width;
                    }

                    public String getPerf_AccelerateTime() {
                        return Perf_AccelerateTime;
                    }

                    public void setPerf_AccelerateTime(String Perf_AccelerateTime) {
                        this.Perf_AccelerateTime = Perf_AccelerateTime;
                    }

                    public String getPerf_DriveType() {
                        return Perf_DriveType;
                    }

                    public void setPerf_DriveType(String Perf_DriveType) {
                        this.Perf_DriveType = Perf_DriveType;
                    }

                    public String getPerf_MaxSpeed() {
                        return Perf_MaxSpeed;
                    }

                    public void setPerf_MaxSpeed(String Perf_MaxSpeed) {
                        this.Perf_MaxSpeed = Perf_MaxSpeed;
                    }

                    public String getPerf_SeatNum() {
                        return Perf_SeatNum;
                    }

                    public void setPerf_SeatNum(String Perf_SeatNum) {
                        this.Perf_SeatNum = Perf_SeatNum;
                    }

                    public String getUnderPan_ForwardGearNum() {
                        return UnderPan_ForwardGearNum;
                    }

                    public void setUnderPan_ForwardGearNum(String UnderPan_ForwardGearNum) {
                        this.UnderPan_ForwardGearNum = UnderPan_ForwardGearNum;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getModel_id() {
                        return model_id;
                    }

                    public void setModel_id(int model_id) {
                        this.model_id = model_id;
                    }
                }

                public static class ModelInfoBean {
                }

                public static class PlatformInfoBean {
                }

                public static class SeriesInfoBean {
                    /**
                     * brand_id : 97
                     * series_id : 2621
                     * series_name : DB9
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

                public static class UserInfoBean {
                    /**
                     * created : 0
                     * mobile :
                     * profile : {"age":0,"avatar":"http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF","constellation":"","gender":0,"nickname":"吴简","signature":"","type":2}
                     * user_id : 456
                     * username :
                     */

                    private int created;
                    private String mobile;
                    private ProfileBean profile;
                    private int user_id;
                    private String username;

                    public int getCreated() {
                        return created;
                    }

                    public void setCreated(int created) {
                        this.created = created;
                    }

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

                    public ProfileBean getProfile() {
                        return profile;
                    }

                    public void setProfile(ProfileBean profile) {
                        this.profile = profile;
                    }

                    public int getUser_id() {
                        return user_id;
                    }

                    public void setUser_id(int user_id) {
                        this.user_id = user_id;
                    }

                    public String getUsername() {
                        return username;
                    }

                    public void setUsername(String username) {
                        this.username = username;
                    }

                    public static class ProfileBean {
                        /**
                         * age : 0
                         * avatar : http://img.bibicar.cn/FvLNIJe7tr9CD841bljUbpscp8wF
                         * constellation :
                         * gender : 0
                         * nickname : 吴简
                         * signature :
                         * type : 2
                         */

                        private int age;
                        private String avatar;
                        private String constellation;
                        private int gender;
                        private String nickname;
                        private String signature;
                        private int type;

                        public int getAge() {
                            return age;
                        }

                        public void setAge(int age) {
                            this.age = age;
                        }

                        public String getAvatar() {
                            return avatar;
                        }

                        public void setAvatar(String avatar) {
                            this.avatar = avatar;
                        }

                        public String getConstellation() {
                            return constellation;
                        }

                        public void setConstellation(String constellation) {
                            this.constellation = constellation;
                        }

                        public int getGender() {
                            return gender;
                        }

                        public void setGender(int gender) {
                            this.gender = gender;
                        }

                        public String getNickname() {
                            return nickname;
                        }

                        public void setNickname(String nickname) {
                            this.nickname = nickname;
                        }

                        public String getSignature() {
                            return signature;
                        }

                        public void setSignature(String signature) {
                            this.signature = signature;
                        }

                        public int getType() {
                            return type;
                        }

                        public void setType(int type) {
                            this.type = type;
                        }
                    }
                }

                public static class FilesBean {
                    /**
                     * file_id : 0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3
                     * file_type : 4
                     * file_url : http://img.bibicar.cn/0ad64a89-0bbc-4595-b569-ccb1f5e87c4c_3?imageMogr2/auto-orient/thumbnail/1000x/strip
                     */

                    private String file_id;
                    private String file_type;
                    private String file_url;

                    public String getFile_id() {
                        return file_id;
                    }

                    public void setFile_id(String file_id) {
                        this.file_id = file_id;
                    }

                    public String getFile_type() {
                        return file_type;
                    }

                    public void setFile_type(String file_type) {
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
        }
    }
}
