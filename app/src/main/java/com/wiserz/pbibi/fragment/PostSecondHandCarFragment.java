package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.UploadCarPhotoInfo;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GifSizeFilter;
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
    private EditText et_input_exchange_owner_time;
    private EditText et_input_table_mileage;
    private EditText et_input_price;
    private ImageView iv_add_car_vin;
    private ImageView iv_add_car_photo;
    private ImageView iv_image_vin;
    private TextView tv_contain_fee;
    private TextView tv_car_color;
    private TextView tv_city;
    private TextView tv_car_type;
    private TextView tv_first_post_license;
    private LinearLayout ll_image_vin;

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    private static final int REQUEST_CODE_CHOOSE = 55;
    private UploadManager uploadManager;
    private String vin_hash;

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
        ll_image_vin = (LinearLayout) view.findViewById(R.id.ll_image_vin);
        tv_contain_fee = (TextView) view.findViewById(R.id.tv_contain_fee);
        tv_car_color = (TextView) view.findViewById(R.id.tv_car_color);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_car_type = (TextView) view.findViewById(R.id.tv_car_type);
        tv_first_post_license = (TextView) view.findViewById(R.id.tv_first_post_license);
        et_input_profile = (EditText) view.findViewById(R.id.et_input_profile);
        et_input_phone_num = (EditText) view.findViewById(R.id.et_input_phone_num);
        et_input_place = (EditText) view.findViewById(R.id.et_input_place);
        et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_price = (EditText) view.findViewById(R.id.et_input_price);
        et_input_vin_no = (EditText) view.findViewById(R.id.et_input_vin_no);
        et_input_engine_no = (EditText) view.findViewById(R.id.et_input_engine_no);
        et_input_exchange_owner_time = (EditText) view.findViewById(R.id.et_input_exchange_owner_time);
        et_input_table_mileage = (EditText) view.findViewById(R.id.et_input_table_mileage);
        iv_add_car_vin = (ImageView) view.findViewById(R.id.iv_add_car_vin);
        iv_add_car_vin.setOnClickListener(this);
        iv_add_car_photo = (ImageView) view.findViewById(R.id.iv_add_car_photo);
        iv_add_car_photo.setOnClickListener(this);
        iv_image_vin = (ImageView) view.findViewById(R.id.iv_image_vin);
    }

    //车主说
    private String getCarOwnerSay() {
        return et_input_profile.getText().toString().trim();
    }

    //是否包含过户费
    private String getFeeIsOrNot() {
        return tv_contain_fee.getText().toString().trim();
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

    //过户次数
    private String getExchangeOwnerTime() {
        return et_input_exchange_owner_time.getText().toString().trim();
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
                ToastUtils.showShort("年检到期日期");
                break;
            case R.id.rl_insurance_date_time:
                ToastUtils.showShort("保险到期日期");
                break;
            case R.id.rl_contain_fee:
                ToastUtils.showShort("是否包含过户费");
                break;
            case R.id.rl_choose_car_color:
                gotoPager(SelectCarColorFragment.class, null);
                break;
            case R.id.rl_first_post_license:
                ToastUtils.showShort("首次上牌");
                break;
            case R.id.rl_choose_city:
                ToastUtils.showShort("选择城市");
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
            default:
                break;
        }
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
        ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList = (ArrayList<UploadCarPhotoInfo>) DataManager.getInstance().getData1();
        String upload_token = (String) DataManager.getInstance().getData2();
        DataManager.getInstance().setData1(null);
        DataManager.getInstance().setData2(null);

        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            uploadImage(uploadCarPhotoInfoArrayList, upload_token);
        } else {
            ToastUtils.showShort("请填写完整的信息！");
        }
    }

    private void uploadImage(ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList, String upload_token) {
        final int mSize = uploadCarPhotoInfoArrayList.size();

        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();

        uploadManager = new UploadManager();

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

            String profile = getCarOwnerSay();
            String phone_num = getPhoneNum();
            String place = getPlace();
            String name = getName();
            String car_price = getPrice();

            if (EmptyUtils.isEmpty(vin_hash) && EmptyUtils.isEmpty(getVIN())) {
                ToastUtils.showShort("请输入VIN码或者上传VIN照片");
                return;
            }

            if (EmptyUtils.isNotEmpty(profile) && EmptyUtils.isNotEmpty(phone_num) && EmptyUtils.isNotEmpty(place) && EmptyUtils.isNotEmpty(name) && EmptyUtils.isNotEmpty(car_price)) {
                OkHttpUtils.post()
                        .url(Constant.getPublishSecondCarUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                        .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                        .addParams(Constant.FILES_ID, mPhotoFile.toString())
                        .addParams(Constant.CAR_COLOR, String.valueOf(2))//车辆颜色
                        .addParams(Constant.CONTACT_NAME, name)
                        .addParams(Constant.CAR_INTRO, profile)
                        .addParams(Constant.PRICE, car_price)
                        .addParams(Constant.CONTACT_PHONE, phone_num)
                        .addParams(Constant.CONTACT_ADDRESS, place)
                        .addParams(Constant.CITY_ID, String.valueOf(1))
                        .addParams(Constant.BRAND_ID, String.valueOf(96))//车品牌id                    ooo(必填)
                        .addParams(Constant.MODEL_ID, String.valueOf(122458))//车型id                  ooo(必填)
                        .addParams(Constant.CAR_TYPE, String.valueOf(1))
                        .addParams(Constant.SERIES_ID, String.valueOf(2149))//车系列id                  ooo(必填)
                        .addParams(Constant.CAR_NO, "")//车牌号码
                        .addParams(Constant.ACTION, String.valueOf(1))//上传车类型
                        .addParams(Constant.IS_TRANSFER, String.valueOf(0))//是否过户
                        .addParams(Constant.CAR_STATUS, String.valueOf(0))//车辆状态
                        .addParams(Constant.VIN_NO, "LGBP12E21DY196239")//vin码                        ooo(必填)
                        .addParams(Constant.EXCHANGE_TIME, String.valueOf(0))//交易时间
                        .addParams(Constant.ENGINE_NO, "")//发动机号
                        .addParams(Constant.VIN_FILE, vin_hash)//vin文件
                        .addParams(Constant.MAINTAIN, String.valueOf(0))
                        .addParams(Constant.BOARD_TIME, "")
                        .addParams(Constant.MILEAGE, String.valueOf(0.000000))
                        .addParams(Constant.CAR_ID, "")
                        .addParams(Constant.CHECK_EXPIRATION_TIME, "")//年检到期日期
                        .addParams(Constant.INSURANCE_DUE_TIME, "")//保险到期日期
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtils.e(response);
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

            } else {
                ToastUtils.showShort("请填写完整的信息！");
            }
        }
    }
}
