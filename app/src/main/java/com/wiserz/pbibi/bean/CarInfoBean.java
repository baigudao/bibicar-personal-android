package com.wiserz.pbibi.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/8/10 14:00.
 * QQ : 971060378
 * Used as : 汽车信息的bean
 */
public class CarInfoBean extends BaseBean {

    /**
     * brand_info : {"abbre":"L","brand_id":96,"brand_name":"路虎","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_96_100.png"}
     * car_color : 7
     * car_id : 5923f8c33da22
     * car_name : 路虎 揽胜 2017 3.0L V6 SC Vogue
     * car_status : 0
     * car_type : 0
     * created : 1495529667
     * fav_num : 1
     * files : []
     * files_img : {"hash":"Fja6zKmICAz6l6QCd7-Z0pC-bjt8","key":1.495529639332E9,"type":2}
     * is_pacted : 2
     * model_detail : {"CarReferPrice":"158.80万","Car_RepairPolicy":"三年或10万公里","Engine_EnvirStandard":"欧5","Engine_ExhaustForFloat":"3.0 L","Engine_InhaleType":"双涡轮增压","Engine_Location":"前置","Engine_MaxNJ":"600 N·m","Engine_horsepower":"258 Ps","Oil_FuelCapacity":"105 L","Oil_FuelTab":"95号","Oil_FuelType":"汽油","Oil_SupplyType":"直喷","OutSet_Height":"1835 mm","OutSet_Length":"4999 mm","OutSet_WheelBase":"2922 mm","OutSet_Width":"2073 mm","Perf_AccelerateTime":"7.4 s","Perf_DriveType":"全时四驱","Perf_MaxSpeed":"209","Perf_SeatNum":"5 个","Perf_ZongHeYouHao":"10.7 L/100km","UnderPan_ForwardGearNum":"8 挡 手自一体","id":4310,"model_id":122458}
     * model_info : {"model_id":122458,"model_name":"2017 3.0L V6 SC Vogue","model_year":"2017","series_id":2149}
     * price : 188
     * sales_volume : 0
     * series_info : {"brand_id":96,"series_id":2149,"series_name":"揽胜"}
     * updated : 1495529667
     * user_info : {"created":0,"mobile":"","profile":{"age":0,"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","constellation":"","gender":0,"nickname":"BiBi Car","signature":""},"user_id":389,"username":""}
     * visit_num : 7
     * vr_url :
     */

    private BrandInfoBean brand_info;
    private int car_color;
    private String car_id;
    private String car_name;
    private int car_status;
    private int car_type;
    private String car_intro;
    private int created;
    private int fav_num;
    private String file_img;
    private int is_pacted;
    private ModelDetailBean model_detail;
    private ModelInfoBean model_info;
    private double price;
    private int sales_volume;
    private SeriesInfoBean series_info;
    private int updated;
    private UserInfoBean user_info;
    private int visit_num;
    private String vr_url;
    private FilesBean files;
    private String contact_phone;
    private String city_name;
    private String gearbox;
    private String mileage;
    private String board_time;
    private String board_address;
    private String exchange_time;
    private int is_like;
    private int is_fav;
    private ArrayList<CarConfiguration.Configuration> car_extra_info;

    public ArrayList<CarConfiguration.Configuration> getCar_extra_info() {
        return car_extra_info;
    }

    public void setCar_extra_info(ArrayList<CarConfiguration.Configuration> car_extra_info) {
        this.car_extra_info = car_extra_info;
    }

    public String getBoard_address() {
        return board_address;
    }

    public void setBoard_address(String board_address) {
        this.board_address = board_address;
    }

    public String getCar_intro() {
        return car_intro;
    }

    public void setCar_intro(String car_intro) {
        this.car_intro = car_intro;
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

    public String getExchange_time() {
        return exchange_time;
    }

    public void setExchange_time(String exchange_time) {
        this.exchange_time = exchange_time;
    }

    public String getBoard_time() {
        return board_time;
    }

    public void setBoard_time(String board_time) {
        this.board_time = board_time;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
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

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public int getCar_status() {
        return car_status;
    }

    public void setCar_status(int car_status) {
        this.car_status = car_status;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getFav_num() {
        return fav_num;
    }

    public void setFav_num(int fav_num) {
        this.fav_num = fav_num;
    }

    public String getFiles_img() {
        return file_img;
    }

    public void setFiles_img(String file_img) {
        this.file_img = file_img;
    }

    public int getIs_pacted() {
        return is_pacted;
    }

    public void setIs_pacted(int is_pacted) {
        this.is_pacted = is_pacted;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public FilesBean getFiles() {
        return files;
    }

    public void setFiles(FilesBean files) {
        this.files = files;
    }

    public static class FilesBean {

        private List<Type1Bean> type1;
        private List<Type2Bean> type2;
        private List<Type3Bean> type3;
        private List<Type4Bean> type4;

        public List<Type1Bean> getType1() {
            return type1;
        }

        public void setType1(List<Type1Bean> type1) {
            this.type1 = type1;
        }

        public List<Type2Bean> getType2() {
            return type2;
        }

        public void setType2(List<Type2Bean> type2) {
            this.type2 = type2;
        }

        public List<Type3Bean> getType3() {
            return type3;
        }

        public void setType3(List<Type3Bean> type3) {
            this.type3 = type3;
        }

        public List<Type4Bean> getType4() {
            return type4;
        }

        public void setType4(List<Type4Bean> type4) {
            this.type4 = type4;
        }

        public static class Type1Bean {
            /**
             * file_id : Fq4aiXZUGbSnLs1iX8QCfHs549Ah
             * file_type : 1
             * file_url : http://thirtimg.bibicar.cn/13639276_624e2734917_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
             */

            private String file_id;
            private int file_type;
            private String file_url;

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

        public static class Type2Bean {
            /**
             * file_id : Fk2HRUDCo9WifHZpBescPf4soK4a
             * file_type : 2
             * file_url : http://thirtimg.bibicar.cn/13639276_53ed5659895_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
             */

            private String file_id;
            private int file_type;
            private String file_url;

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

        public static class Type3Bean {
            /**
             * file_id : FvpiM0ioJnjNdatp5N3OJWLK66kO
             * file_type : 3
             * file_url : http://thirtimg.bibicar.cn/13639276_9c0a0359328_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
             */

            private String file_id;
            private int file_type;
            private String file_url;

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

        public static class Type4Bean {
            /**
             * file_id : FsZkqgUoEAKN_ev5sJ_gI17QSHOs
             * file_type : 4
             * file_url : http://thirtimg.bibicar.cn/13639276_b4f12663493_20.jpg?imageMogr2/auto-orient/thumbnail/1000x/strip
             */

            private String file_id;
            private int file_type;
            private String file_url;

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
    }

    public static class BrandInfoBean {
        /**
         * abbre : L
         * brand_id : 96
         * brand_name : 路虎
         * brand_url : http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_96_100.png
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

    public static class ModelDetailBean {
        /**
         * CarReferPrice : 158.80万
         * Car_RepairPolicy : 三年或10万公里
         * Engine_EnvirStandard : 欧5
         * Engine_ExhaustForFloat : 3.0 L
         * Engine_InhaleType : 双涡轮增压
         * Engine_Location : 前置
         * Engine_MaxNJ : 600 N·m
         * Engine_horsepower : 258 Ps
         * Oil_FuelCapacity : 105 L
         * Oil_FuelTab : 95号
         * Oil_FuelType : 汽油
         * Oil_SupplyType : 直喷
         * OutSet_Height : 1835 mm
         * OutSet_Length : 4999 mm
         * OutSet_WheelBase : 2922 mm
         * OutSet_Width : 2073 mm
         * Perf_AccelerateTime : 7.4 s
         * Perf_DriveType : 全时四驱
         * Perf_MaxSpeed : 209
         * Perf_SeatNum : 5 个
         * Perf_ZongHeYouHao : 10.7 L/100km
         * UnderPan_ForwardGearNum : 8 挡 手自一体
         * id : 4310
         * model_id : 122458
         */

        private String CarReferPrice;
        private String Car_RepairPolicy;
        private String Engine_EnvirStandard;
        private String Engine_ExhaustForFloat;
        private String Engine_InhaleType;
        private String Engine_Location;
        private String Engine_MaxNJ;
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
        private String Engine_Type;
        private String Perf_MaxSpeed;
        private String Perf_SeatNum;
        private String Perf_ZongHeYouHao;
        private String UnderPan_ForwardGearNum;
        private String OutSet_MinGapFromEarth;
        private int id;
        private int model_id;

        public String getOutSet_MinGapFromEarth() {
            return OutSet_MinGapFromEarth;
        }

        public void setOutSet_MinGapFromEarth(String outSet_MinGapFromEarth) {
            OutSet_MinGapFromEarth = outSet_MinGapFromEarth;
        }

        public String getEngine_Type() {
            return Engine_Type;
        }

        public void setEngine_Type(String engine_Type) {
            Engine_Type = engine_Type;
        }

        public String getCarReferPrice() {
            return CarReferPrice;
        }

        public void setCarReferPrice(String CarReferPrice) {
            this.CarReferPrice = CarReferPrice;
        }

        public String getCar_RepairPolicy() {
            return Car_RepairPolicy;
        }

        public void setCar_RepairPolicy(String Car_RepairPolicy) {
            this.Car_RepairPolicy = Car_RepairPolicy;
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

        public String getPerf_ZongHeYouHao() {
            return Perf_ZongHeYouHao;
        }

        public void setPerf_ZongHeYouHao(String Perf_ZongHeYouHao) {
            this.Perf_ZongHeYouHao = Perf_ZongHeYouHao;
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
        /**
         * model_id : 122458
         * model_name : 2017 3.0L V6 SC Vogue
         * model_year : 2017
         * series_id : 2149
         */

        private int model_id;
        private String model_name;
        private String model_year;
        private int series_id;

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

        public String getModel_year() {
            return model_year;
        }

        public void setModel_year(String model_year) {
            this.model_year = model_year;
        }

        public int getSeries_id() {
            return series_id;
        }

        public void setSeries_id(int series_id) {
            this.series_id = series_id;
        }
    }

    public static class SeriesInfoBean {
        /**
         * brand_id : 96
         * series_id : 2149
         * series_name : 揽胜
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
         * profile : {"age":0,"avatar":"http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF","constellation":"","gender":0,"nickname":"BiBi Car","signature":""}
         * user_id : 389
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
             * avatar : http://img.bibicar.cn/FmDMHALgJxKY9Rw3APp5QSsnajuF
             * constellation :
             * gender : 0
             * nickname : BiBi Car
             * signature :
             */

            private int age;
            private String avatar;
            private String constellation;
            private int gender;
            private String nickname;
            private String signature;

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
        }
    }
}
