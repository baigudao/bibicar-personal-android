package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.ProvinceBean;
import com.wiserz.pbibi.bean.UploadCarPhotoInfo;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GifSizeFilter;
import com.wiserz.pbibi.view.WheelViewPopupWindow;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/9/7 16:06.
 * QQ : 971060378
 * Used as : 上传二手车的页面
 */
public class PostSecondHandCarFragment extends BaseFragment {

    private EditText et_input_profile;
    private EditText et_input_phone_num;
    private EditText et_input_place;
    private EditText et_input_name;
    private EditText et_input_vin_no;
    private EditText et_input_engine_no;
    private EditText et_input_table_mileage;
    private EditText et_input_price;
    private ImageView iv_image_vin;
    private TextView tv_contain_fee;
    private TextView tv_car_color;
    private TextView tv_city;
    private TextView tv_car_type;
    private TextView tv_first_post_license;
    private TextView tv_car_status_using;
    private TextView tv_car_status_stop;
    private TextView tv_exchange_owner_time;
    private TextView tv_car_keep_fit_no;
    private TextView tv_car_keep_fit_4s_no;
    private TextView tv_car_keep_fit_4s_yes;
    private TextView tv_insurance_date_time;
    private TextView tv_annual_survey_date;
    private LinearLayout ll_image_vin;

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    private static final int REQUEST_CODE_CHOOSE = 55;
    private UploadManager uploadManager;
    private String vin_hash;

    private int model_id;
    private int mColor;
    private int brand_id;
    private int series_id;

    private ArrayList<ProvinceBean> mProvinceBeenList;

    private String cityCode;
    private String firstPostLicenseTime;

    private int mCarStatus;//1：日常使用中；0：停驶中
    private int mMaintain;
    private String mIsTransfer;

    private int flag;

    private String insuranceDateTime;//保险到期日
    private String annualSurveyDate;//年检到期日

    public String getInsuranceDateTime() {
        return insuranceDateTime;
    }

    public void setInsuranceDateTime(String insuranceDateTime) {
        this.insuranceDateTime = insuranceDateTime;
    }

    public String getAnnualSurveyDate() {
        return annualSurveyDate;
    }

    public void setAnnualSurveyDate(String annualSurveyDate) {
        this.annualSurveyDate = annualSurveyDate;
    }

    public String getmIsTransfer() {
        return mIsTransfer;
    }

    public void setmIsTransfer(String mIsTransfer) {
        this.mIsTransfer = mIsTransfer;
    }

    private String mExchangeTime;

    public String getmExchangeTime() {
        return mExchangeTime;
    }

    public void setmExchangeTime(String mExchangeTime) {
        this.mExchangeTime = mExchangeTime;
    }

    public String getFirstPostLicenseTime() {
        return firstPostLicenseTime;
    }

    public void setFirstPostLicenseTime(String firstPostLicenseTime) {
        this.firstPostLicenseTime = firstPostLicenseTime;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_second_hand_car;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传二手车");

        view.findViewById(R.id.btn_post_second_car).setOnClickListener(this);
        view.findViewById(R.id.rl_annual_survey_date).setOnClickListener(this);
        view.findViewById(R.id.rl_insurance_date_time).setOnClickListener(this);
        view.findViewById(R.id.rl_contain_fee).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_color).setOnClickListener(this);
        view.findViewById(R.id.rl_first_post_license).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_city).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_type).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_exchange_owner_time).setOnClickListener(this);
        ll_image_vin = (LinearLayout) view.findViewById(R.id.ll_image_vin);
        tv_contain_fee = (TextView) view.findViewById(R.id.tv_contain_fee);
        tv_car_color = (TextView) view.findViewById(R.id.tv_car_color);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_car_type = (TextView) view.findViewById(R.id.tv_car_type);
        tv_insurance_date_time = (TextView) view.findViewById(R.id.tv_insurance_date_time);
        tv_annual_survey_date = (TextView) view.findViewById(R.id.tv_annual_survey_date);
        tv_exchange_owner_time = (TextView) view.findViewById(R.id.tv_exchange_owner_time);
        tv_first_post_license = (TextView) view.findViewById(R.id.tv_first_post_license);
        tv_car_status_stop = (TextView) view.findViewById(R.id.tv_car_status_stop);
        tv_car_status_stop.setOnClickListener(this);
        tv_car_status_using = (TextView) view.findViewById(R.id.tv_car_status_using);
        tv_car_status_using.setOnClickListener(this);
        tv_car_keep_fit_no = (TextView) view.findViewById(R.id.tv_car_keep_fit_no);
        tv_car_keep_fit_no.setOnClickListener(this);
        tv_car_keep_fit_4s_no = (TextView) view.findViewById(R.id.tv_car_keep_fit_4s_no);
        tv_car_keep_fit_4s_no.setOnClickListener(this);
        tv_car_keep_fit_4s_yes = (TextView) view.findViewById(R.id.tv_car_keep_fit_4s_yes);
        tv_car_keep_fit_4s_yes.setOnClickListener(this);
        et_input_profile = (EditText) view.findViewById(R.id.et_input_profile);
        et_input_phone_num = (EditText) view.findViewById(R.id.et_input_phone_num);
        et_input_place = (EditText) view.findViewById(R.id.et_input_place);
        et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_price = (EditText) view.findViewById(R.id.et_input_price);
        et_input_vin_no = (EditText) view.findViewById(R.id.et_input_vin_no);
        et_input_engine_no = (EditText) view.findViewById(R.id.et_input_engine_no);
        et_input_table_mileage = (EditText) view.findViewById(R.id.et_input_table_mileage);
        ImageView iv_add_car_vin = (ImageView) view.findViewById(R.id.iv_add_car_vin);
        iv_add_car_vin.setOnClickListener(this);
        ImageView iv_add_car_photo = (ImageView) view.findViewById(R.id.iv_add_car_photo);
        iv_add_car_photo.setOnClickListener(this);
        iv_image_vin = (ImageView) view.findViewById(R.id.iv_image_vin);

        mProvinceBeenList = new ArrayList<>();
        uploadManager = new UploadManager();

        mCarStatus = 1;
        resetCarUsingStatusView();

        mMaintain = 2;
        resetCarMaintainView();
    }

    //车主说
    private String getCarOwnerSay() {
        return et_input_profile.getText().toString().trim();
    }

    //得到车主电话
    private String getPhoneNum() {
        return et_input_phone_num.getText().toString().trim();
    }

    //得到车主地址
    private String getPlace() {
        return et_input_place.getText().toString().trim();
    }

    //得到车主姓名
    private String getName() {
        return et_input_name.getText().toString().trim();
    }

    private String getVIN() {
        return et_input_vin_no.getText().toString().trim();
    }

    private String getEngine() {
        return et_input_engine_no.getText().toString().trim();
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
            case R.id.btn_post_second_car:
                publishSecondCar();
                break;
            case R.id.rl_annual_survey_date:
                flag = 2;
                showWheelView(tv_annual_survey_date, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA);
                break;
            case R.id.rl_insurance_date_time:
                flag = 1;
                showWheelView(tv_insurance_date_time, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA);
                break;
            case R.id.rl_contain_fee:
                showWheelView(tv_contain_fee, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_YES_NO);
                break;
            case R.id.rl_choose_car_color:
                gotoPager(SelectCarColorFragment.class, null);
                break;
            case R.id.rl_choose_exchange_owner_time:
                showWheelView(tv_exchange_owner_time, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_NUMBER);
                break;
            case R.id.rl_first_post_license:
                flag = 0;
                showWheelView(tv_first_post_license, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA);
                break;
            case R.id.rl_choose_city:
                if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
                    getProvinceList();
                }
                WheelViewPopupWindow mWheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
                    @Override
                    public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                        ProvinceBean mSelectProvince = (ProvinceBean) value1;
                        CityBean mSelectCity = (CityBean) value2;
                        if (EmptyUtils.isNotEmpty(mSelectProvince) && EmptyUtils.isNotEmpty(mSelectCity) && getView() != null) {
                            tv_city.setText(mSelectProvince.getProvince() + " " + mSelectCity.getCity_name());
                            setCityCode(mSelectCity.getCity_code());
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
            case R.id.iv_add_car_vin:
                addVinImage();
                break;
            case R.id.iv_add_car_photo:
                gotoPager(PostPhotoFragment.class, null);
                break;
            case R.id.tv_car_status_using:
                mCarStatus = 1;
                resetCarUsingStatusView();
                break;
            case R.id.tv_car_status_stop:
                mCarStatus = 0;
                resetCarUsingStatusView();
                break;
            case R.id.tv_car_keep_fit_no:
                mMaintain = 0;
                resetCarMaintainView();
                break;
            case R.id.tv_car_keep_fit_4s_yes:
                mMaintain = 1;
                resetCarMaintainView();
                break;
            case R.id.tv_car_keep_fit_4s_no:
                mMaintain = 2;
                resetCarMaintainView();
                break;
            default:
                break;
        }
    }

    private void resetCarMaintainView() {
        int color = getResources().getColor(R.color.third_text_color);
        changeText(tv_car_keep_fit_no, color);
        changeText(tv_car_keep_fit_4s_yes, color);
        changeText(tv_car_keep_fit_4s_no, color);
        color = getResources().getColor(R.color.main_color);
        if (mMaintain == 0) {
            changeText(tv_car_keep_fit_no, color);
        } else if (mMaintain == 1) {
            changeText(tv_car_keep_fit_4s_yes, color);
        } else {
            changeText(tv_car_keep_fit_4s_no, color);
        }
    }

    private void resetCarUsingStatusView() {
        int color = getResources().getColor(R.color.third_text_color);
        changeText(tv_car_status_using, color);
        changeText(tv_car_status_stop, color);
        color = getResources().getColor(R.color.main_color);
        if (mCarStatus == 1) {
            changeText(tv_car_status_using, color);
        } else {
            changeText(tv_car_status_stop, color);
        }
    }

    private void changeText(TextView tv, int color) {
        tv.setTextColor(color);
        GradientDrawable background = (GradientDrawable) tv.getBackground();
        background.setStroke(SizeUtils.dp2px(1), color);
    }

    private void showWheelView(final TextView tvSelect, final WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE type) {
        WheelViewPopupWindow wheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
            @Override
            public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                if (type == WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_YES_NO) {
                    switch (index1) {
                        case 0:
                            setmIsTransfer("1");
                            break;
                        case 1:
                            setmIsTransfer("0");
                            break;
                        default:
                            break;
                    }
                    tvSelect.setText((String) value1);
                } else if (type == WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_NUMBER) {
                    setmExchangeTime(String.valueOf(index1));
                    tvSelect.setText(String.valueOf(index1));
                } else if (type == WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA) {
                    int year = Calendar.getInstance().get(Calendar.YEAR) - 10 + index1 - 40;
                    int month = index2 + 1;
                    int day = index3 + 1;
                    String choose_time = year + "-" + (month < 10 ? (0 + "" + String.valueOf(month)) : String.valueOf(month)) + "-" + (day < 10 ? (0 + "" + String.valueOf(day)) : String.valueOf(day));
                    tvSelect.setText(choose_time);
                    switch (flag) {
                        case 0:
                            setFirstPostLicenseTime(choose_time);
                            break;
                        case 1:
                            setInsuranceDateTime(choose_time);
                            break;
                        case 2:
                            setAnnualSurveyDate(choose_time);
                            break;
                        default:
                            break;
                    }
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
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
    }

    private void resetColorView(int color) {
        Resources res = getResources();
        String text = res.getString(res.getIdentifier("car_color_" + (color + 1), "string", getActivity().getPackageName()));
        tv_car_color.setText(text);
    }

    private void addVinImage() {
        Matisse.from(PostSecondHandCarFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//MimeType.ofAll()
                .countable(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Zhihu)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                //                .capture(true)
                //                .captureStrategy(new CaptureStrategy(true,"com.bibicar.capture"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    List<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected) && mSelected.size() != 0) {
                File file_vin = new File(CommonUtil.getRealFilePath(mContext, mSelected.get(0)));
                ll_image_vin.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(file_vin)
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .into(iv_image_vin);
                uploadVinImage(file_vin);
            } else {
                ToastUtils.showShort("选择图片失败");
            }
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
                                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
        String place = getPlace();
        String profile = getCarOwnerSay();

        if (EmptyUtils.isEmpty(model_id) || model_id == 0) {
            ToastUtils.showShort("请选择车型");
            return;
        }

        if (EmptyUtils.isEmpty(getCityCode())) {
            ToastUtils.showShort("请选择所在城市");
            return;
        }

        if (EmptyUtils.isEmpty(tableMileage)) {
            ToastUtils.showShort("请输入表显里程数");
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

        if (EmptyUtils.isEmpty(getmExchangeTime())) {
            ToastUtils.showShort("请选择过户次数");
            return;
        }

        if (EmptyUtils.isEmpty(mColor) || mColor == 0) {
            ToastUtils.showShort("请选择车辆颜色");
            return;
        }

        if (EmptyUtils.isEmpty(getEngine())) {
            ToastUtils.showShort("请输入发动机号");
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

        if (EmptyUtils.isEmpty(place)) {
            ToastUtils.showShort("请填写看车地点");
            return;
        }

        if (EmptyUtils.isEmpty(phone_num)) {
            ToastUtils.showShort("请填写你的联系电话");
            return;
        }

        if (EmptyUtils.isEmpty(getmIsTransfer())) {
            ToastUtils.showShort("请选择是否包含过户费");
            return;
        }

        if (EmptyUtils.isEmpty(getInsuranceDateTime())) {
            ToastUtils.showShort("请选择保险到期日期");
            return;
        }

        if (EmptyUtils.isEmpty(getAnnualSurveyDate())) {
            ToastUtils.showShort("请选择年检到期日期");
            return;
        }

        if (EmptyUtils.isEmpty(profile)) {
            ToastUtils.showShort("请填写车主说的内容");
            return;
        }

        ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList = (ArrayList<UploadCarPhotoInfo>) DataManager.getInstance().getData8();
        String upload_token = (String) DataManager.getInstance().getData9();
        DataManager.getInstance().setData8(null);
        DataManager.getInstance().setData9(null);

        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            uploadImage(uploadCarPhotoInfoArrayList, upload_token);
        } else {
            ToastUtils.showShort("请添加车辆照片！");
        }
    }

    private void uploadImage(ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList, String upload_token) {
        final int mSize = uploadCarPhotoInfoArrayList.size();

        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();

        for (int i = 0; i < mSize; i++) {
            UploadCarPhotoInfo uploadCarPhotoInfo = uploadCarPhotoInfoArrayList.get(i);

            if (EmptyUtils.isNotEmpty(uploadCarPhotoInfo)) {
                int file_type = uploadCarPhotoInfo.getFile_type();
                if (EmptyUtils.isNotEmpty(file_type)) {
                    uploadManager.put(uploadCarPhotoInfo.getFile(), UUID.randomUUID().toString() + "_" + String.valueOf(file_type), upload_token, new UpCompletionHandler() {
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
                                    if (mPhotoTypes.length() == mSize && mPhotoFile.length() == mSize) {
                                        commitDataToServer(mPhotoTypes, mPhotoFile);
                                    }
                                } else {
                                    String code = response.optString("code");
                                    String msg = jsonObjectData.optString("msg");
                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
    }

    private void commitDataToServer(JSONArray mPhotoTypes, JSONArray mPhotoFile) {
        if (EmptyUtils.isNotEmpty(mPhotoTypes) && EmptyUtils.isNotEmpty(mPhotoFile)) {

            String tableMileage = getTableMileage();//表显里程
            String profile = getCarOwnerSay();
            String phone_num = getPhoneNum();
            String place = getPlace();
            String name = getName();
            String car_price = getPrice();

            OkHttpUtils.post()
                    .url(Constant.getPublishSecondCarUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                    .addParams(Constant.FILES_ID, mPhotoFile.toString())
                    .addParams(Constant.CAR_COLOR, String.valueOf(mColor))//车辆颜色
                    .addParams(Constant.CAR_INTRO, profile)
                    .addParams(Constant.PRICE, car_price)
                    .addParams(Constant.CONTACT_NAME, name)
                    .addParams(Constant.CONTACT_PHONE, phone_num)
                    .addParams(Constant.CONTACT_ADDRESS, place)
                    .addParams(Constant.CITY_ID, getCityCode())
                    .addParams(Constant.BRAND_ID, String.valueOf(brand_id))//车品牌id                    ooo(必填)
                    .addParams(Constant.SERIES_ID, String.valueOf(series_id))//车系列id                  ooo(必填)
                    .addParams(Constant.MODEL_ID, String.valueOf(model_id))//车型id                      ooo(必填)
                    .addParams(Constant.CAR_TYPE, String.valueOf(1))
                    .addParams(Constant.CAR_NO, "")//车牌号码
                    .addParams(Constant.ACTION, String.valueOf(1))//上传车类型
                    .addParams(Constant.IS_TRANSFER, getmIsTransfer())//是否过户
                    .addParams(Constant.CAR_STATUS, String.valueOf(mCarStatus))//车辆状态
                    .addParams(Constant.VIN_NO, getVIN())//vin码        LGBP12E21DY196239                ooo(必填)
                    .addParams(Constant.VIN_FILE, vin_hash)//vin文件
                    .addParams(Constant.EXCHANGE_TIME, String.valueOf(0))//交易时间
                    .addParams(Constant.ENGINE_NO, getEngine())//发动机号
                    .addParams(Constant.MAINTAIN, String.valueOf(mMaintain))
                    .addParams(Constant.BOARD_TIME, getFirstPostLicenseTime())
                    .addParams(Constant.MILEAGE, tableMileage)
                    .addParams(Constant.CAR_ID, "")
                    .addParams(Constant.CHECK_EXPIRATION_TIME, getAnnualSurveyDate())//年检到期日期
                    .addParams(Constant.INSURANCE_DUE_TIME, getInsuranceDateTime())//保险到期日期
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
                                    ToastUtils.showShort("上传二手车成功！");
                                    goBack();
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
    }
}
