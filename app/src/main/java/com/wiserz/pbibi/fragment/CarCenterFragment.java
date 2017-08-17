package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBeanForCarCenter;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/9 18:00.
 * QQ : 971060378
 * Used as : 汽车中心页面
 */
public class CarCenterFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    public LocationClient mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    private TextView tvLocation;

    private RecyclerView recyclerView;
    private int mPage;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0001) {
                String city = (String) msg.obj;
                tvLocation.setText(city);
            } else if (msg.what == 0x0002) {
                tvLocation.setText("深圳");
                ToastUtils.showShort("定位失败，请检查网络是否通畅");
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_center;
    }

    @Override
    protected void initView(View view) {
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        mLocationClient = new LocationClient(BaseApplication.getAppContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        initLocation();
        mLocationClient.start();//开始定位

        view.findViewById(R.id.ivRight).setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPage = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                //                gotoPager(LoanPlanFragment.class,null);//贷款方案

                //                gotoPager(ConcreteParameterFragment.class, null);//具体参数

                gotoPager(TestFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getCarListDataFromNet(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private void getCarListDataFromNet(String keyword, String order_id, String brand_id, String series_id,
                                       String min_price, String max_price, String min_mileage, String max_mileage,
                                       String min_board_time, String max_board_time, String has_vr, String old, String source) {
        OkHttpUtils.post()
                .url(Constant.getCarListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.KEY_WORD, keyword == null ? "" : keyword)//关键字
                .addParams(Constant.ORDER_ID, order_id == null ? "" : order_id)//排序id
                .addParams(Constant.BRAND_ID, brand_id == null ? "" : brand_id)//车品牌id
                .addParams(Constant.SERIES_ID, series_id == null ? "" : series_id)//车系列id
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
                .addParams(Constant.MIN_MILEAGE, min_mileage == null ? "" : min_mileage)//最低里程
                .addParams(Constant.MAX_MILEAGE, max_mileage == null ? "" : max_mileage)//最高里程
                .addParams(Constant.MIN_BOARD_TIME, min_board_time == null ? "" : min_board_time)//最短上牌时间
                .addParams(Constant.MAX_BOARD_TIME, max_board_time == null ? "" : max_board_time)//最长上牌时间
                .addParams(Constant.HAS_VR, has_vr == null ? "" : has_vr)//是否有VR看车功能  1:是
                .addParams(Constant.OLD, old == null ? "" : old)//是否新车二手车 1:新车 2 二手车
                .addParams(Constant.SOURCE, source == null ? "" : source)//车辆来源 1:个人 2 商家
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                handlerCarListData(jsonObjectData);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBeanForCarCenter> carInfoBeanForCarCenterArrayList = getCarListData(jsonObjectData);
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanForCarCenterArrayList, CAR_LIST_FOR_CAR_CENTER);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    private ArrayList<CarInfoBeanForCarCenter> getCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBeanForCarCenter> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            list = new ArrayList<>();
            JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCarInfo = jsonArray.optJSONObject(i).optJSONObject("car_info");
                CarInfoBeanForCarCenter carInfoBeanForCarCenter = gson.fromJson(jsonObjectCarInfo.toString(), CarInfoBeanForCarCenter.class);
                list.add(carInfoBeanForCarCenter);
            }
        }
        return list;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBeanForCarCenter")) {
            CarInfoBeanForCarCenter carInfoBeanForCarCenter = (CarInfoBeanForCarCenter) data;
            if (EmptyUtils.isNotEmpty(carInfoBeanForCarCenter)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBeanForCarCenter.getCar_id());
                gotoPager(CarDetailFragment.class, bundle);
            }
        }
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getCity();
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getAddress().city;
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                //当前为网络定位结果

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getAddress().city;
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeServerError) {

                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                handler.sendEmptyMessage(0x0002);
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                //当前网络不通
                handler.sendEmptyMessage(0x0002);
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                handler.sendEmptyMessage(0x0002);
            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！

            }
        }
    }
}
