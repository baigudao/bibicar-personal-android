package com.wiserz.pbibi.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.bean.ProvinceBean;
import com.wiserz.pbibi.bean.TypeBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.MorePopupWindow;
import com.wiserz.pbibi.view.WheelViewPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/7 16:06.
 * QQ : 971060378
 * Used as : 上传二手车的页面
 */
public class PostSecondHandCarFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener{


    private EditText et_input_phone_num;
    private EditText et_input_name;
    private EditText et_input_vin_no;
    private ImageView iv_image_vin;
    private LinearLayout ll_image_vin;

    private TextView tv_car_color;
    private TextView tv_car_type;
    private TextView tv_car_level;
    private TextView tv_brand_place;//上牌地点
    private TextView tv_first_post_license;//上牌时间
    private EditText et_input_table_mileage;//行驶里程
    private EditText et_input_price;
    private EditText et_input_profile;//车辆描述

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    private static final int REQUEST_CODE_CHOOSE = 55;
    private UploadManager uploadManager;
    private String vin_hash;

    private int model_id;
    private int mColor;
    private int brand_id;
    private int series_id;

    private String car_level = "";

    private ArrayList<ProvinceBean> mProvinceBeenList;

    ArrayList<View> viewList=new ArrayList<>();

    private String firstPostLicenseTime;
    private String broadPlace;

    public String getFirstPostLicenseTime() {
        return firstPostLicenseTime;
    }

    public void setFirstPostLicenseTime(String firstPostLicenseTime) {
        this.firstPostLicenseTime = firstPostLicenseTime;
    }

    public void setBroadPlace(String broadPlace) {
        this.broadPlace = broadPlace;
    }

    public String getBroadPlace() {
        return this.broadPlace;
    }

//    private ArrayList<File> mUploadPhotos;
    private Map<Integer,File> fileMap;
    private String upload_token;
    private ArrayList<CarConfiguration> mCarConfigurationList;
    private ArrayList<String> mSelectedConfig = new ArrayList<>();

    private CarInfoBean mCarInfoBean = null;
    private ArrayList<TypeBean> mAllImageUrls = new ArrayList<>();//存放该车辆的网络图片
    private String mCarId = null;
    private PopupWindow popupWindow;
    private static final int SELECT_CAR_COLOR_DATA_TYPE = 33;
    private String dstDirFile ;
    private boolean from_sell = false;
    private int i;
    private static final int DOWN_SUCCESS = 1;

    private Map<Integer,File> getUploadPhotos() {
        if (fileMap == null) {
            fileMap = new LinkedHashMap<>();
        }
        return fileMap;
    }
    private int mYear,mMonth,mDay;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_second_hand_car;
    }

    @Override
    protected void initView(View view) {
        mCarId = getArguments().getString(Constant.CAR_ID);
        from_sell = getArguments().getBoolean(Constant.FROM_SELL);

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.topLine).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传二手车");

        view.findViewById(R.id.rlSelectDate).setOnClickListener(this);
        mYear=Calendar.getInstance().get(Calendar.YEAR);
        mMonth= Calendar.getInstance().get(Calendar.MONTH);
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        ((DatePicker) view.findViewById(R.id.datePicker)).init(mYear,
                mMonth,
                mDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        mYear=year;
                        mMonth=month;
                        mDay=day;
                    }
                });

        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getView().findViewById(R.id.rlSelectDate).setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.tvSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month=mMonth+1;
                String choose_time = mYear + "-" + (month < 10 ? (0 + "" + String.valueOf(month)) : String.valueOf(month)) + "-" + (mDay < 10 ? (0 + "" + String.valueOf(mDay)) : String.valueOf(mDay));
                ((TextView) getView().findViewById(R.id.tv_first_post_license)).setText(choose_time);
                ((TextView) getView().findViewById(R.id.tv_first_post_license)).setTextColor(getResources().getColor(R.color.main_text_color));
                setFirstPostLicenseTime(choose_time);
                getView().findViewById(R.id.rlSelectDate).setVisibility(View.GONE);
            }
        });

        ViewPager viewPager=(ViewPager) view.findViewById(R.id.customViewPager);
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        viewList.add(inflater.inflate(R.layout.item_post_second_handle_car_basic_msg,null));
        viewList.add(inflater.inflate(R.layout.item_post_car_detail_msg,null));

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 添加一个 页卡

                container.addView(viewList.get(position));

                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // 删除
                container.removeView(viewList.get(position));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    getView().findViewById(R.id.tvBasicMsg).performClick();
                }else{
                    getView().findViewById(R.id.tvDetailMsg).performClick();
                    resetCarConfigurations(mCarConfigurationList);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        view.findViewById(R.id.btn_post_second_car).setOnClickListener(this);
        viewList.get(0).findViewById(R.id.rl_choose_car_level).setOnClickListener(this);
        viewList.get(0).findViewById(R.id.rl_choose_car_color).setOnClickListener(this);
        viewList.get(0).findViewById(R.id.rl_first_post_license).setOnClickListener(this);
        viewList.get(0).findViewById(R.id.rl_choose_car_type).setOnClickListener(this);
        viewList.get(0).findViewById(R.id.rl_choose_brand_place).setOnClickListener(this);
        ll_image_vin = (LinearLayout) viewList.get(0).findViewById(R.id.ll_image_vin);
        tv_car_color = (TextView) viewList.get(0).findViewById(R.id.tv_car_color);
        tv_car_type = (TextView) viewList.get(0).findViewById(R.id.tv_car_type);

        view.findViewById(R.id.tvCloseWarning).setOnClickListener(this);
        view.findViewById(R.id.tvDetailMsg).setOnClickListener(this);
        view.findViewById(R.id.tvBasicMsg).setOnClickListener(this);

        tv_car_level = (TextView) viewList.get(0).findViewById(R.id.tv_car_level);
        tv_brand_place = (TextView) viewList.get(0).findViewById(R.id.tv_brand_place);//上牌地点
        tv_first_post_license  = (TextView) viewList.get(0).findViewById(R.id.tv_first_post_license);//上牌时间
        et_input_profile = (EditText) viewList.get(0).findViewById(R.id.et_input_profile);
        et_input_phone_num = (EditText) viewList.get(0).findViewById(R.id.et_input_phone_num);
        et_input_name = (EditText) viewList.get(0).findViewById(R.id.et_input_name);
        et_input_price = (EditText) viewList.get(0).findViewById(R.id.et_input_price);
        et_input_vin_no = (EditText) viewList.get(0).findViewById(R.id.et_input_vin_no);
        et_input_table_mileage = (EditText) viewList.get(0).findViewById(R.id.et_input_table_mileage);
        ImageView iv_add_car_vin = (ImageView) viewList.get(0).findViewById(R.id.iv_add_car_vin);
        iv_add_car_vin.setOnClickListener(this);
        iv_image_vin = (ImageView) viewList.get(0).findViewById(R.id.iv_image_vin);
        iv_image_vin.setOnClickListener(this);

        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        if(userInfoBean!=null){
            et_input_phone_num.setText(TextUtils.isEmpty(userInfoBean.getMobile())?"":userInfoBean.getMobile());
            et_input_name.setText(TextUtils.isEmpty(userInfoBean.getProfile().getNickname())?"":userInfoBean.getProfile().getNickname());
        }

        mProvinceBeenList = new ArrayList<>();
        uploadManager = new UploadManager();

        mColor=-1;

        dstDirFile = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName() + "/download";

        getTokenFromNet();
        getCarExtraInfo();
//        gotoPager(SelectPhotoFragment.class, null, true);

  //      resetCarPhotosView();
    }

    @Override
    protected void initData() {
        super.initData();

        if(EmptyUtils.isNotEmpty(mCarId)){
            getCarDetailData();
        }
    }

    private void getCarDetailData(){
        OkHttpUtils.post()
                .url(Constant.getCarDetailUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.CAR_ID, mCarId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("TESTLOG","getcardetail onError==="+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("TESTLOG","getcardetail response==="+response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                handlerCarInfoData(jsonObjectData.optJSONObject("car_info"));//处理此车辆的数据
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerCarInfoData(JSONObject car_info) {
        Gson gson = new Gson();
        mCarInfoBean = gson.fromJson(car_info.toString(), CarInfoBean.class);

        if (EmptyUtils.isNotEmpty(mCarInfoBean)){

            init();

            //获取图片,不能超过12张
            mAllImageUrls.clear();
            mAllImageUrls = mCarInfoBean.getFiles();

            //设置图片
            if(EmptyUtils.isNotEmpty(mAllImageUrls)){
                ToastUtils.showShort("图片正在加载中，请稍后");
                downLoadPic();
            }else{
                resetCarPhotosView();
            }
        }else{
            Log.i("TESTLOG","carbean is  null ===");
        }

    }

    private void init(){
        if(mCarInfoBean != null){//从售车页面进来的
            //车型
            brand_id = mCarInfoBean.getBrand_info().getBrand_id();
            model_id = mCarInfoBean.getModel_info().getModel_id();
            series_id = mCarInfoBean.getSeries_info().getSeries_id();
            resetCarModelView(mCarInfoBean.getBrand_info().getBrand_name(),mCarInfoBean.getSeries_info().getSeries_name(),mCarInfoBean.getModel_info().getModel_name());

            //车辆级别
            car_level = String.valueOf(mCarInfoBean.getCar_level());
            Log.i("TESTLOG","car_level=="+car_level);
            if(mCarInfoBean.getCar_level()!=0){
                switch (mCarInfoBean.getCar_level()){
                    case 6:
                        tv_car_level.setText("轿车");
                        break;
                    case 7:
                        tv_car_level.setText("MVP");
                        break;
                    case 8:
                        tv_car_level.setText("SUV");
                        break;
                    case 9:
                        tv_car_level.setText("跑车");
                        break;
                    case 11:
                        tv_car_level.setText("皮卡");
                        break;
                    case 13:
                        tv_car_level.setText("敞篷跑车");
                        break;
                }
                tv_car_level.setTextColor(getResources().getColor(R.color.main_text_color));
            }


            //车辆价格
            et_input_price.setText(String.valueOf(mCarInfoBean.getPrice()));

            //车辆颜色
            mColor = mCarInfoBean.getCar_color();
            resetColorView(mColor);

            //车辆简介
            et_input_profile.setText(mCarInfoBean.getCar_intro());

            //上牌地点
            if(EmptyUtils.isNotEmpty(mCarInfoBean.getBoard_address())){
                tv_brand_place.setText(mCarInfoBean.getBoard_address());
                tv_brand_place.setTextColor(getResources().getColor(R.color.main_text_color));
                setBroadPlace(mCarInfoBean.getBoard_address());
            }
            //首次上牌时间
            if(EmptyUtils.isNotEmpty(mCarInfoBean.getBoard_time())){
                tv_first_post_license.setText(CommonUtil.timeToDate(mCarInfoBean.getBoard_time()));
                tv_first_post_license.setTextColor(getResources().getColor(R.color.main_text_color));
                setFirstPostLicenseTime(mCarInfoBean.getBoard_time());
            }
            //行驶里程
            et_input_table_mileage.setText(mCarInfoBean.getMileage());
            //配置详情
            ArrayList<CarConfiguration.Configuration> configList = mCarInfoBean.getCar_extra_info();
            if(EmptyUtils.isNotEmpty(configList)){
                //将详情配置数据存入mSelectedConfig(id)
                for(CarConfiguration.Configuration configuration : configList){
                    mSelectedConfig.add(String.valueOf(configuration.getId()));
                }
            }
        }
    }

    private void downLoadPic(){
        String fileName = UUID.randomUUID().toString() + ".jpg";
        Log.i("TESTLOG","downLoadPic === url == " + mAllImageUrls.get(i).getFile_url() );
        Log.i("TESTLOG","downLoadPic === fileName ==" + fileName );
        OkHttpUtils.get()
                .url(mAllImageUrls.get(i).getFile_url())
                .build()
                .execute(new FileCallBack(dstDirFile,fileName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        getUploadPhotos().put(mAllImageUrls.get(i).getFile_type(),response);
                        mHandler.sendEmptyMessage(DOWN_SUCCESS);
                    }
                });

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWN_SUCCESS:
                    if(i == mAllImageUrls.size()-1){
                        Log.i("TESTLOG","downLoadPic === dowanPicFinish");
                        //下载完毕
                        resetCarPhotosView();
                    }else{
                        //下载下一个
                        i++;
                        downLoadPic();
                    }

                    break;
            }
        }
    };

    private void getCarExtraInfo() {
        OkHttpUtils.post()
                .url(Constant.getCarExtraInfoUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
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
                            JSONArray jsonObjectData = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                mCarConfigurationList = getCarConfigurations(jsonObjectData);
//                                resetCarConfigurations(mCarConfigurationList);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private ArrayList<CarConfiguration> getCarConfigurations(JSONArray array) {
        if (array == null) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(array.toString(), new TypeToken<ArrayList<CarConfiguration>>() {
            }.getType());
        }
    }

    private void resetCarConfigurations(ArrayList<CarConfiguration> list) {
        LinearLayout llDetailMsg = (LinearLayout) viewList.get(1).findViewById(R.id.llDetailMsg);
        llDetailMsg.removeAllViews();
//        mSelectedConfig.clear();
        if (list == null || list.isEmpty()) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout;
        ArrayList<CarConfiguration.Configuration> itemList;
        for (CarConfiguration config : list) {
            layout = (LinearLayout) layoutInflater.inflate(R.layout.item_car_configuration, null);
            llDetailMsg.addView(layout);
            ((TextView) layout.findViewById(R.id.tvConfigName)).setText(config.getType_name());
            itemList = config.getList();
            int itemCount = itemList.size();
            LinearLayout itemLayout;
            TextView tvName1, tvName2, tvName3;
            int index=0;
            int charCount;
            for (int j = 0; j < Integer.MAX_VALUE; ++j) {
                charCount=0;
                itemLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_configuration_detail, null);
                ((LinearLayout) layout.findViewById(R.id.llDetails)).addView(itemLayout);
                tvName1 = ((TextView) itemLayout.findViewById(R.id.tvName1));
                tvName2 = ((TextView) itemLayout.findViewById(R.id.tvName2));
                tvName3 = ((TextView) itemLayout.findViewById(R.id.tvName3));
                if (index < itemCount) {
                    charCount+=itemList.get(index).getName().length();

                    if(mSelectedConfig.contains(String.valueOf(itemList.get(index).getId()))){//选中状态
                        tvName1.setBackgroundResource(R.drawable.back_config_selected);
                        tvName1.setTextColor(getResources().getColor(R.color.main_color));
                    }

                    tvName1.setText(itemList.get(index).getName());
                    tvName1.setTag(R.id.tag, itemList.get(index).getId());
                    tvName1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id = String.valueOf(view.getTag(R.id.tag));
                            if (mSelectedConfig.contains(id)) {
                                mSelectedConfig.remove(id);
                                view.setBackgroundResource(R.drawable.back_config_not_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_text_color));
                            } else {
                                mSelectedConfig.add(id);
                                view.setBackgroundResource(R.drawable.back_config_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_color));
                            }
                        }
                    });
                } else {
                    tvName1.setVisibility(View.GONE);
                    tvName2.setVisibility(View.GONE);
                    tvName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
                if (index < itemCount) {
                    charCount+=itemList.get(index).getName().length();

                    if(mSelectedConfig.contains(String.valueOf(itemList.get(index).getId()))){//选中状态
                        tvName2.setBackgroundResource(R.drawable.back_config_selected);
                        tvName2.setTextColor(getResources().getColor(R.color.main_color));
                    }

                    tvName2.setText(itemList.get(index).getName());
                    tvName2.setTag(R.id.tag, itemList.get(index).getId());
                    tvName2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id = String.valueOf(view.getTag(R.id.tag));
                            if (mSelectedConfig.contains(id)) {
                                mSelectedConfig.remove(id);
                                view.setBackgroundResource(R.drawable.back_config_not_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_text_color));
                            } else {
                                mSelectedConfig.add(id);
                                view.setBackgroundResource(R.drawable.back_config_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_color));
                            }
                        }
                    });
                } else {
                    tvName2.setVisibility(View.GONE);
                    tvName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
                if (index < itemCount) {
                    if(charCount+itemList.get(index).getName().length()>18){
                        tvName3.setVisibility(View.GONE);
                        continue;
                    }

                    if(mSelectedConfig.contains(String.valueOf(itemList.get(index).getId()))){//选中状态
                        tvName3.setBackgroundResource(R.drawable.back_config_selected);
                        tvName3.setTextColor(getResources().getColor(R.color.main_color));
                    }

                    tvName3.setText(itemList.get(index).getName());
                    tvName3.setTag(R.id.tag, itemList.get(index).getId());
                    tvName3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id = String.valueOf(view.getTag(R.id.tag));
                            if (mSelectedConfig.contains(id)) {
                                mSelectedConfig.remove(id);
                                view.setBackgroundResource(R.drawable.back_config_not_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_text_color));
                            } else {
                                mSelectedConfig.add(id);
                                view.setBackgroundResource(R.drawable.back_config_selected);
                                ((TextView) view).setTextColor(getResources().getColor(R.color.main_color));
                            }
                        }
                    });
                } else {
                    tvName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
            }
        }
    }

    private void getTokenFromNet() {
        OkHttpUtils.post()
                .url(Constant.getUploadTokenUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                upload_token = jsonObjectData.optString("upload_token");
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onResume() {
        super.onResume();
        if (DataManager.getInstance().getObject() != null && DataManager.getInstance().getData1() != null) {
            String newPath = (String) DataManager.getInstance().getObject();
            int index = (int) DataManager.getInstance().getData1();
            getUploadPhotos().put(index, new File(newPath));
            DataManager.getInstance().setObject(null);
            DataManager.getInstance().setData1(null);
            resetCarPhotosView();
        } else if (DataManager.getInstance().getData8() != null) {
            int index = (int) DataManager.getInstance().getData8();
            getUploadPhotos().remove(index);
            DataManager.getInstance().setData8(null);
            resetCarPhotosView();
        } else if (DataManager.getInstance().getObject() != null) {
            if(EmptyUtils.isNotEmpty(getUploadPhotos())){
                getUploadPhotos().clear();
            }
            getUploadPhotos().putAll( (HashMap<Integer,File>)DataManager.getInstance().getObject());
            DataManager.getInstance().setObject(null);
            resetCarPhotosView();
        } else if (DataManager.getInstance().getData9() != null) {
            File file_vin = new File((String) DataManager.getInstance().getData9());
            ll_image_vin.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(file_vin)
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .into(iv_image_vin);
            uploadVinImage(file_vin);
            DataManager.getInstance().setData9(null);
        }



    }

    private void resetCarPhotosView() {
        LinearLayout llCarPhotos = (LinearLayout) viewList.get(0).findViewById(R.id.llCarPhotos);
        llCarPhotos.removeAllViews();
        int size;
        if (getUploadPhotos().size() == Constant.MAX_UPLOAD_PHOTO_NUM) {
            size = Constant.MAX_UPLOAD_PHOTO_NUM;
        } else {
            size = getUploadPhotos().size() + 1;
        }

        Iterator<Map.Entry<Integer, File>> iterator = getUploadPhotos().entrySet().iterator();

        int row = (size % 2 == 0) ? size / 2 : size / 2 + 1;
        LinearLayout itemView;
        for (int i = 0; i < row; ++i) {
            itemView = (LinearLayout) View.inflate(mContext,R.layout.item_report_car_photo, null);
            llCarPhotos.addView(itemView);

            int index = i * 2;
            File photo = null;
            int key = -1;
            if(iterator.hasNext()) {
                Map.Entry<Integer, File> entry = iterator.next();
                key = entry.getKey();
                photo = entry.getValue();
            }

            setPhoto((ViewGroup) itemView.getChildAt(0),photo,key,index, index == size - 1);

            key = -1;
            photo = null;
            if(iterator.hasNext()) {
                Map.Entry<Integer, File> entry = iterator.next();
                key = entry.getKey();
                photo = entry.getValue();
            }

            setPhoto((ViewGroup) itemView.getChildAt(1),photo,key,index + 1, index + 1 == size - 1);

        }
    }

    private void setPhoto(ViewGroup itemView, final File photo, final int key, int index, boolean isLast) {
        ImageView ivPhoto = (ImageView) itemView.getChildAt(0);
        ImageView ivClose = (ImageView) itemView.getChildAt(1);

        if (photo != null) {
            CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(photo), ivPhoto);
            ivClose.setVisibility(View.VISIBLE);
            ivPhoto.setTag(R.id.tag, index);
            ivClose.setTag(R.id.tag, index);
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag(R.id.tag);
                    if (pos < getUploadPhotos().size()) {
                        DataManager.getInstance().setObject(photo.getAbsolutePath());
                        DataManager.getInstance().setData1(pos);
                        DataManager.getInstance().setData2(getUploadPhotos().size());
                        DataManager.getInstance().setData3(key);
                        ((BaseActivity) mContext).gotoPager(EditPhotoFragment.class, null, true);
                    }

                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    File file = getUploadPhotos().remove(key);
                    if (file != null) {
                        file.delete();
                    }
                    resetCarPhotosView();

                }
            });

        }else {
            if (isLast) {
                ivPhoto.setImageResource(R.drawable.add_car_photo);
                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //选择进入相机或者相册
                        showSelectPhotoWindow();
                    }
                });
                ivClose.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showSelectPhotoWindow() {
        MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(), new MorePopupWindow.MorePopupWindowClickListener() {
            @Override
            public void onFirstBtnClicked() {
                //拍照
                Bundle bundle = new Bundle();
                bundle.putBoolean("select_album", false);
                DataManager.getInstance().setObject(getUploadPhotos().size());
                DataManager.getInstance().setData1(getUploadPhotos());
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onSecondBtnClicked() {
                //相册
                Bundle bundle = new Bundle();
                bundle.putBoolean("select_album", true);
                DataManager.getInstance().setObject(getUploadPhotos().size());
                DataManager.getInstance().setData1(getUploadPhotos());
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onThirdBtnClicked() {

            }

            @Override
            public void onFourthBtnClicked() {

            }

            @Override
            public void onCancelBtnClicked() {

            }
        }, MorePopupWindow.MORE_POPUP_WINDOW_TYPE.TYPE_ADD_PICTURE_MODEL);
        morePopupWindow.initView();
        morePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //车主说
    private String getCarOwnerSay() {
        return et_input_profile.getText().toString().trim();
    }

    //得到车主电话
    private String getPhoneNum() {
        return et_input_phone_num.getText().toString().trim();
    }

    //得到车主姓名
    private String getName() {
        return et_input_name.getText().toString().trim();
    }

    private String getVIN() {
        return et_input_vin_no.getText().toString().trim();
    }

    private String getPrice() {
        return et_input_price.getText().toString().trim();
    }

    private String getTableMileage() {
        return et_input_table_mileage.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.rlSelectDate:
                getView().findViewById(R.id.rlSelectDate).setVisibility(View.GONE);
                break;
            case R.id.btn_post_second_car:
                publishSecondCar();
                break;
            case R.id.tvBasicMsg:
                ((ViewPager) getView().findViewById(R.id.customViewPager)).setCurrentItem(0);
                ((TextView) getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                ((TextView) getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.line2).setVisibility(View.GONE);
                break;
            case R.id.tvDetailMsg:
                ((ViewPager) getView().findViewById(R.id.customViewPager)).setCurrentItem(1);
                ((TextView) getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                ((TextView) getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.GONE);
                getView().findViewById(R.id.line2).setVisibility(View.VISIBLE);
                break;
            case R.id.tvCloseWarning:
                getView().findViewById(R.id.rlWarning).setVisibility(View.GONE);
                break;
            case R.id.rl_choose_car_color:
//                gotoPager(SelectCarColorFragment.class, null);
                View colorView = View.inflate(mContext, R.layout.layout_popwindow, null);
                carSelectColorPopupWindow(colorView);
                break;
            case R.id.rl_first_post_license:
            //    showWheelView((TextView) getView().findViewById(R.id.tv_first_post_license), WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA);
                getView().findViewById(R.id.rlSelectDate).bringToFront();
                getView().findViewById(R.id.rlSelectDate).setVisibility(View.VISIBLE);
                break;
            case R.id.rl_choose_brand_place:
                if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
                    getProvinceList();
                    return;
                }
                WheelViewPopupWindow mWheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
                    @Override
                    public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                        ProvinceBean mSelectProvince = (ProvinceBean) value1;
                        CityBean mSelectCity = (CityBean) value2;
                        if (EmptyUtils.isNotEmpty(mSelectProvince) && EmptyUtils.isNotEmpty(mSelectCity) && getView() != null) {
                            ((TextView) viewList.get(0).findViewById(R.id.tv_brand_place)).setText(mSelectCity.getCity_name());
                            ((TextView) viewList.get(0).findViewById(R.id.tv_brand_place)).setTextColor(getResources().getColor(R.color.main_text_color));
                            setBroadPlace(mSelectCity.getCity_name());
                        }
                    }
                }, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_PROVINCE_CITY);
                mWheelViewPopupWindow.setProvinceList(mProvinceBeenList);
                mWheelViewPopupWindow.initView();
                mWheelViewPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_choose_car_type:
                gotoPager(SelectCarBrandFragment.class, null);
                break;
            case R.id.iv_image_vin:
            case R.id.iv_add_car_vin:
                DataManager.getInstance().setObject(-1);
                gotoPager(SelectPhotoFragment.class, null, true);
                break;
            case R.id.rl_choose_car_level:
                carSelectPopupWindow(LayoutInflater.from(getActivity()).inflate(R.layout.layout_select_car_level,null));
                break;
            default:
                break;
        }
    }

    //弹出侧边栏
    private void carSelectColorPopupWindow(final View view) {
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //外部是否可以点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(getView().findViewById(R.id.topView));
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation2);

        //对返回按键的捕获并处理
        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        //点击阴影部分退出
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //                if(flag == 1){
                //                    resetColorView(mColor);
                //                }else if(flag == 2){
                //                    resetFilterView(llTop);
                //                }
            }
        });

        //点击返回箭头退出，加入选择的配置详情item到报价单
        view.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //                if(flag == 1){
                //                    resetColorView(mColor);
                //                }else if(flag == 2){
                //                    resetFilterView(llTop);
                //                }
            }
        });


        resetColorConfiguration(view);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getView() != null) {
                    resetColorView(mColor);

                }
            }
        });
    }

    private void resetColorConfiguration(View view){
        TextView tvBack = (TextView) view.findViewById(R.id.tvBack);
        tvBack.setText("选择颜色");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            strings.add("color" + i);
        }
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, SELECT_CAR_COLOR_DATA_TYPE);
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("String")) {
            Log.i("TESTLOG","onItemClick ===");
            mColor = position;

            if(EmptyUtils.isNotEmpty(popupWindow)){
                popupWindow.dismiss();
            }
        }
    }

    private void carSelectPopupWindow(final View view) {
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //外部是否可以点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(getView().findViewById(R.id.topView));
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation2);

        //对返回按键的捕获并处理
        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        LinearLayout llCarLevel = (LinearLayout) view.findViewById(R.id.llCarLevel);
        int childCount = llCarLevel.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            ViewGroup viewGroup = (ViewGroup) llCarLevel.getChildAt(i);
            for (int j = 0; j < viewGroup.getChildCount(); ++j) {
                viewGroup.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        car_level=(String)v.getTag();
                        ((TextView)getView().findViewById(R.id.tv_car_level)).setTextColor(getResources().getColor(R.color.main_text_color));
                        ((TextView)getView().findViewById(R.id.tv_car_level)).setText(((TextView)((ViewGroup) v).getChildAt(1)).getText().toString());
                        popupWindow.dismiss();
                    }
                });
            }
        }
    }


    private void showWheelView(final TextView tvSelect, final WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE type) {
        WheelViewPopupWindow wheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
            @Override
            public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                if (type == WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA) {
                    int year = Calendar.getInstance().get(Calendar.YEAR) - 10 + index1 - 40;
                    int month = index2 + 1;
                    int day = index3 + 1;
                    String choose_time = year + "-" + (month < 10 ? (0 + "" + String.valueOf(month)) : String.valueOf(month)) + "-" + (day < 10 ? (0 + "" + String.valueOf(day)) : String.valueOf(day));
                    tvSelect.setText(choose_time);
                    tvSelect.setTextColor(getResources().getColor(R.color.main_text_color));
                    setFirstPostLicenseTime(choose_time);
                }
            }
        }, type);
        wheelViewPopupWindow.initView();
        wheelViewPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
            getProvinceList();
        }
    }

    private void getProvinceList() {
        OkHttpUtils.post()
                .url(Constant.getProvinceListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                JSONArray jsonArray = jsonObjectData.optJSONArray("province_list");
                                if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                                    Gson gson = new Gson();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                        ProvinceBean provinceBean = gson.fromJson(jsonObject1.toString(), ProvinceBean.class);
                                        mProvinceBeenList.add(provinceBean);
                                    }
                                }
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            Object data = DataManager.getInstance().getData7();
            if (EmptyUtils.isNotEmpty(data)) {
                if (data instanceof Integer) {
                    //颜色回调
                    mColor = (int) data;
                    resetColorView(mColor);
                }
                DataManager.getInstance().setData7(null);
            }

            Object data1 = DataManager.getInstance().getData1();
            Object data2 = DataManager.getInstance().getData2();
            Object data3 = DataManager.getInstance().getData3();
            Object data4 = DataManager.getInstance().getData4();
            Object data5 = DataManager.getInstance().getData5();
            Object data6 = DataManager.getInstance().getData6();
            if (EmptyUtils.isNotEmpty(data1)
                    && EmptyUtils.isNotEmpty(data2)
                    && EmptyUtils.isNotEmpty(data3)
                    && EmptyUtils.isNotEmpty(data4)
                    && EmptyUtils.isNotEmpty(data5)
                    && EmptyUtils.isNotEmpty(data6)) {
                if (data1 instanceof Integer && data2 instanceof String
                        && data3 instanceof Integer && data4 instanceof String
                        && data5 instanceof Integer && data6 instanceof String) {
                    brand_id = (int) data1;
                    String brand_name = (String) data2;
                    series_id = (int) data3;
                    String series_name = (String) data4;
                    model_id = (int) data5;
                    String model_name = (String) data6;
                    resetCarModelView(brand_name, series_name, model_name);
                }
                DataManager.getInstance().setData1(null);
                DataManager.getInstance().setData2(null);
                DataManager.getInstance().setData3(null);
                DataManager.getInstance().setData4(null);
                DataManager.getInstance().setData5(null);
                DataManager.getInstance().setData6(null);
            }
        }
    }

    private void resetCarModelView(String brand_name, String series_name, String model_name) {
        tv_car_type.setText(brand_name + " " + series_name + " " + model_name);
        tv_car_type.setTextColor(getResources().getColor(R.color.main_text_color));
    }

    private void resetColorView(int color) {
        if(color>=0) {
            Resources res = getResources();
            String text = res.getString(res.getIdentifier("car_color_" + color, "string", getActivity().getPackageName()));
            tv_car_color.setText(text+">");
            tv_car_color.setTextColor(getResources().getColor(R.color.main_text_color));
        }
    }

    private void uploadVinImage(final File file_vin) {
        if (EmptyUtils.isNotEmpty(file_vin)) {
            OkHttpUtils.post()
                    .url(Constant.getUploadTokenUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                    String upload_token = jsonObjectData.optString("upload_token");
                                    uploadManager.put(file_vin, UUID.randomUUID().toString(), upload_token, new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject response) {
                                            if (info.isOK()) {
                                                //上传成功
                                                int status = response.optInt("status");
                                                JSONObject jsonObjectData = response.optJSONObject("data");
                                                if (status == 1) {
                                                    vin_hash = jsonObjectData.optString("hash");
                                                } else {
                                                    String code = response.optString("code");
                                                    String msg = jsonObjectData.optString("msg");
                                                    ToastUtils.showShort("" + msg);
                                                }
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtils.showShort("VIN照片上传失败，请重新上传");
                                                    }
                                                });
                                            }
                                        }
                                    }, null);
                                } else {
                                    String code = jsonObject.optString("code");
                                    String msg = jsonObjectData.optString("msg");
                                    ToastUtils.showShort("" + msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void publishSecondCar() {
        String tableMileage = getTableMileage();//表显里程
        String first_brand_time = getFirstPostLicenseTime();
        String car_price = getPrice();
        String name = getName();
        String phone_num = getPhoneNum();

        if (EmptyUtils.isEmpty(model_id) || model_id == 0) {
            ToastUtils.showShort("请选择车型");
            return;
        }

        if (TextUtils.isEmpty(car_level)) {
            ToastUtils.showShort("请选择车辆级别");
            return;
        }

        if (EmptyUtils.isEmpty(getBroadPlace())) {
            ToastUtils.showShort("请选择上牌地点");
            return;
        }

        if (EmptyUtils.isEmpty(tableMileage)) {
            ToastUtils.showShort("请输入行驶里程");
            return;
        }

        if (EmptyUtils.isEmpty(first_brand_time)) {
            ToastUtils.showShort("请选择首次上牌时间");
            return;
        }

        if (EmptyUtils.isEmpty(car_price)) {
            ToastUtils.showShort("请输入车辆价格");
            return;
        }


        if (EmptyUtils.isEmpty(getVIN()) && EmptyUtils.isEmpty(vin_hash)) {
            ToastUtils.showShort("请输入VIN码或上传VIN照片");
            return;
        }

        if (EmptyUtils.isEmpty(name)) {
            ToastUtils.showShort("请填写联系人");
            return;
        }


        if (EmptyUtils.isEmpty(phone_num)) {
            ToastUtils.showShort("请填写你的联系电话");
            return;
        }

        if (EmptyUtils.isEmpty(upload_token)) {
            getTokenFromNet();
            return;
        }

        if (!getUploadPhotos().isEmpty()) {
            ToastUtils.showShort("上传图片中，请稍后");
            uploadImage(getUploadPhotos(), upload_token);
        } else {
            ToastUtils.showShort("请添加车辆照片！");
        }
    }

    private void uploadImage(Map<Integer,File> uploadPhotos, String upload_token) {
        final int mSize = uploadPhotos.size();

        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();

        UploadManager uploadManager = new UploadManager();

        Iterator<Map.Entry<Integer, File>> iterator = uploadPhotos.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, File> entry = iterator.next();
            int file_type = entry.getKey();
            File photoPath = entry.getValue();

            if(EmptyUtils.isNotEmpty(photoPath)){
                uploadManager.put(photoPath, UUID.randomUUID().toString() + "_" + String.valueOf(file_type), upload_token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            //上传成功
                            int status = response.optInt("status");
                            JSONObject jsonObjectData = response.optJSONObject("data");
                            if (status == 1) {
                                String file_type = key.substring(key.lastIndexOf("_") + 1);
                                mPhotoTypes.put(file_type);
                                String hash = jsonObjectData.optString("hash");
                                mPhotoFile.put(hash);
                                if (mPhotoTypes.length() == mSize&& mPhotoFile.length() == mSize ) {
                                    commitDataToServer(mPhotoTypes, mPhotoFile);
                                }
                            } else {
                                String code = response.optString("code");
                                final String msg = jsonObjectData.optString("msg");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort("" + msg);
                                    }
                                });
                            }
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort("车辆照片上传失败，请重新上传");
                                }
                            });
                        }
                    }
                }, null);
            }

        }

    }

    private void commitDataToServer(JSONArray mPhotoTypes, JSONArray mPhotoFile) {
        if (EmptyUtils.isNotEmpty(mPhotoTypes) && EmptyUtils.isNotEmpty(mPhotoFile)) {

            String tableMileage = getTableMileage();//表显里程
            String profile = getCarOwnerSay();
            String phone_num = getPhoneNum();
            String name = getName();
            String car_price = getPrice();

            String carInfoIds = "";
            int size = mSelectedConfig.size();
            for (int i = 0; i < size; ++i) {
                carInfoIds += mSelectedConfig.get(i);
                if (i < size - 1) {
                    carInfoIds += ",";
                }
            }

            String url = Constant.getPublishSecondCarUrl();
            if(from_sell){
                url = Constant.getUpdateCarUrl(); //更新
            }
            OkHttpUtils.post()
                    .url(url)
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                    .addParams(Constant.FILES_ID, mPhotoFile.toString())
                    .addParams(Constant.CAR_COLOR, mColor<0?"0":String.valueOf(mColor))//车辆颜色
                    .addParams(Constant.CAR_INTRO, profile)
                    .addParams(Constant.PRICE, car_price)
                    .addParams(Constant.CONTACT_NAME, name)
                    .addParams(Constant.CONTACT_PHONE, phone_num)
                    .addParams(Constant.CONTACT_ADDRESS, "")
                    .addParams(Constant.CITY_ID, "")
                    .addParams(Constant.BRAND_ID, String.valueOf(brand_id))//车品牌id                    ooo(必填)
                    .addParams(Constant.SERIES_ID, String.valueOf(series_id))//车系列id                  ooo(必填)
                    .addParams(Constant.MODEL_ID, String.valueOf(model_id))//车型id                      ooo(必填)
                    .addParams(Constant.CAR_TYPE, String.valueOf(1))
                    .addParams(Constant.CAR_LEVEL, car_level)
                    .addParams(Constant.CAR_NO, "")//车牌号码
                    .addParams(Constant.ACTION, String.valueOf(1))//上传车类型
                    .addParams(Constant.IS_TRANSFER, String.valueOf(0))//是否过户
                    .addParams(Constant.CAR_STATUS, String.valueOf(0))//车辆状态
                    .addParams(Constant.VIN_NO, getVIN())//vin码        LGBP12E21DY196239                ooo(必填)
                    .addParams(Constant.VIN_FILE, vin_hash == null ? "" : vin_hash)//vin文件
                    .addParams(Constant.EXCHANGE_TIME, String.valueOf(0))//交易时间
                    .addParams(Constant.ENGINE_NO, "")//发动机号
                    .addParams(Constant.MAINTAIN, String.valueOf(0))
                    .addParams(Constant.BOARD_TIME, getFirstPostLicenseTime())
                    .addParams(Constant.MILEAGE, tableMileage)
                    .addParams(Constant.BOARD_ADDRESS, getBroadPlace())
                    .addParams(Constant.CAR_INFO_IDS, carInfoIds)
                    .addParams(Constant.CAR_ID, mCarInfoBean!=null?mCarInfoBean.getCar_id():"")//更新需要carid
                    .addParams(Constant.CHECK_EXPIRATION_TIME, "")//年检到期日期
                    .addParams(Constant.INSURANCE_DUE_TIME, "")//保险到期日期
                    .addParams(Constant.CITY_CODE,SPUtils.getInstance().getString(Constant.CITY_CODE,""))
                    .addParams(Constant.CITY_LAT,SPUtils.getInstance().getString(Constant.CITY_LAT,""))
                    .addParams(Constant.CITY_LNG,SPUtils.getInstance().getString(Constant.CITY_LNG,""))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("TESTLOG","commitDataToServer img response ==="+response);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int status = jsonObject.optInt("status");
                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                if (status == 1) {
                                    String text;
                                    LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
                                    if (userInfoBean != null && userInfoBean.getProfile().getType()==2) {
                                        text="等待审核中";
                                    }else{
                                        text="发布成功";
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    AlertDialog alertDialog = builder.setMessage(text)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).create();
                                    alertDialog.show();
                                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            goBack();
                                        }
                                    });
                                } else {
                                    String code = jsonObject.optString("code");
                                    String msg = jsonObjectData.optString("msg");
                                    ToastUtils.showShort("" + msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
