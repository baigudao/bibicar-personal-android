package com.wiserz.pbibi.bean;

/**
 * Created by jackie on 2017/9/29 17:58.
 * QQ : 971060378
 * Used as : 我的售车的bean
 */
public class MySellingCarBean extends BaseBean {

    /**
     * car_info : {"board_time":"","brand_info":{"abbre":"L","brand_id":96,"brand_name":"路虎","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_96_100.png"},"car_color":2,"car_id":"59c35d2c48041","car_intro":"二手车","car_name":"路虎 揽胜 2017 3.0L V6 SC Vogue","car_status":0,"car_type":1,"contact_address":"龙华","contact_name":"李小","contact_phone":"14525365623","created":1505975596,"fav_num":0,"file_img":"http://img.bibicar.cn/7eacbcd1-7c07-47fd-8c5a-31a8231cb5c0_3?imageMogr2/auto-orient/thumbnail/1000x/strip","is_pacted":2,"mileage":0,"model_detail":{"CarReferPrice":"158.80万","Car_RepairPolicy":"三年或10万公里","Engine_EnvirStandard":"欧5","Engine_ExhaustForFloat":"3.0 L","Engine_InhaleType":"双涡轮增压","Engine_Location":"前置","Engine_MaxNJ":"600 N·m","Engine_horsepower":"258 Ps","Oil_FuelCapacity":"105 L","Oil_FuelTab":"95号","Oil_FuelType":"汽油","Oil_SupplyType":"直喷","OutSet_Height":"1835 mm","OutSet_Length":"4999 mm","OutSet_WheelBase":"2922 mm","OutSet_Width":"2073 mm","Perf_AccelerateTime":"7.4 s","Perf_DriveType":"全时四驱","Perf_MaxSpeed":"209","Perf_SeatNum":"5 个","Perf_ZongHeYouHao":"10.7 L/100km","UnderPan_ForwardGearNum":"8 挡 手自一体","id":4310,"model_id":122458},"model_info":{"model_id":122458,"model_name":"2017 3.0L V6 SC Vogue","model_year":"2017","series_id":2149},"price":360.15,"sales_volume":0,"series_info":{"brand_id":96,"series_id":2149,"series_name":"揽胜"},"updated":1505975596,"user_info":{"created":0,"mobile":"","profile":{"age":0,"avatar":"","constellation":"","gender":0,"nickname":"馨子","signature":"","type":1},"user_id":454,"username":""},"verify_status":2,"visit_num":3}
     */

    private CarInfoBean car_info;

    public CarInfoBean getCar_info() {
        return car_info;
    }

    public void setCar_info(CarInfoBean car_info) {
        this.car_info = car_info;
    }

    public static class CarInfoBean {
        /**
         * board_time :
         * brand_info : {"abbre":"L","brand_id":96,"brand_name":"路虎","brand_url":"http://image.bitautoimg.com/bt/car/default/images/logo/masterbrand/png/100/m_96_100.png"}
         * car_color : 2
         * car_id : 59c35d2c48041
         * car_intro : 二手车
         * car_name : 路虎 揽胜 2017 3.0L V6 SC Vogue
         * car_status : 0
         * car_type : 1
         * contact_address : 龙华
         * contact_name : 李小
         * contact_phone : 14525365623
         * created : 1505975596
         * fav_num : 0
         * file_img : http://img.bibicar.cn/7eacbcd1-7c07-47fd-8c5a-31a8231cb5c0_3?imageMogr2/auto-orient/thumbnail/1000x/strip
         * is_pacted : 2
         * mileage : 0
         * model_detail : {"CarReferPrice":"158.80万","Car_RepairPolicy":"三年或10万公里","Engine_EnvirStandard":"欧5","Engine_ExhaustForFloat":"3.0 L","Engine_InhaleType":"双涡轮增压","Engine_Location":"前置","Engine_MaxNJ":"600 N·m","Engine_horsepower":"258 Ps","Oil_FuelCapacity":"105 L","Oil_FuelTab":"95号","Oil_FuelType":"汽油","Oil_SupplyType":"直喷","OutSet_Height":"1835 mm","OutSet_Length":"4999 mm","OutSet_WheelBase":"2922 mm","OutSet_Width":"2073 mm","Perf_AccelerateTime":"7.4 s","Perf_DriveType":"全时四驱","Perf_MaxSpeed":"209","Perf_SeatNum":"5 个","Perf_ZongHeYouHao":"10.7 L/100km","UnderPan_ForwardGearNum":"8 挡 手自一体","id":4310,"model_id":122458}
         * model_info : {"model_id":122458,"model_name":"2017 3.0L V6 SC Vogue","model_year":"2017","series_id":2149}
         * price : 360.15
         * sales_volume : 0
         * series_info : {"brand_id":96,"series_id":2149,"series_name":"揽胜"}
         * updated : 1505975596
         * user_info : {"created":0,"mobile":"","profile":{"age":0,"avatar":"","constellation":"","gender":0,"nickname":"馨子","signature":"","type":1},"user_id":454,"username":""}
         * verify_status : 2
         * visit_num : 3
         */

        private String board_time;
        private BrandInfoBean brand_info;
        private int car_color;
        private String car_id;
        private String car_intro;
        private String car_name;
        private int car_status;
        private int car_type;
        private String contact_address;
        private String contact_name;
        private String contact_phone;
        private int created;
        private int fav_num;
        private String file_img;
        private int is_pacted;
        private int mileage;
        private ModelDetailBean model_detail;
        private ModelInfoBean model_info;
        private double price;
        private int sales_volume;
        private SeriesInfoBean series_info;
        private int updated;
        private UserInfoBean user_info;
        private int verify_status;
        private int visit_num;

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

        public int getFav_num() {
            return fav_num;
        }

        public void setFav_num(int fav_num) {
            this.fav_num = fav_num;
        }

        public String getFile_img() {
            return file_img;
        }

        public void setFile_img(String file_img) {
            this.file_img = file_img;
        }

        public int getIs_pacted() {
            return is_pacted;
        }

        public void setIs_pacted(int is_pacted) {
            this.is_pacted = is_pacted;
        }

        public int getMileage() {
            return mileage;
        }

        public void setMileage(int mileage) {
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

        public int getVerify_status() {
            return verify_status;
        }

        public void setVerify_status(int verify_status) {
            this.verify_status = verify_status;
        }

        public int getVisit_num() {
            return visit_num;
        }

        public void setVisit_num(int visit_num) {
            this.visit_num = visit_num;
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
            private String Perf_MaxSpeed;
            private String Perf_SeatNum;
            private String Perf_ZongHeYouHao;
            private String UnderPan_ForwardGearNum;
            private int id;
            private int model_id;

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
             * profile : {"age":0,"avatar":"","constellation":"","gender":0,"nickname":"馨子","signature":"","type":1}
             * user_id : 454
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
                 * avatar :
                 * constellation :
                 * gender : 0
                 * nickname : 馨子
                 * signature :
                 * type : 1
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
    }
}
