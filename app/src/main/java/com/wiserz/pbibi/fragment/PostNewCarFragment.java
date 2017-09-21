package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.UploadCarPhotoInfo;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/7 16:04.
 * QQ : 971060378
 * Used as : 上传新车的页面
 */
public class PostNewCarFragment extends BaseFragment {

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_new_car;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传新车");
        view.findViewById(R.id.iv_add_car_photo).setOnClickListener(this);
        view.findViewById(R.id.btn_post_new_car).setOnClickListener(this);

        view.findViewById(R.id.rl_choose_car_color).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_city).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_type).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_add_car_photo:
                gotoPager(PostPhotoFragment.class, null);
                break;
            case R.id.btn_post_new_car:
                publishNewCar();
                break;
            case R.id.rl_choose_car_type:
                gotoPager(SelectCarBrandFragment.class, null);
                break;
            case R.id.rl_choose_city:
                ToastUtils.showShort("选择城市");
                break;
            case R.id.rl_choose_car_color:
                gotoPager(SelectCarColorFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            LogUtils.e("heard");
        } else {
            LogUtils.e("hehe");
        }
    }

    private String getInputProfile() {
        return ((EditText) getView().findViewById(R.id.et_input_profile)).getText().toString().trim();
    }

    private String getInputPhoneNum() {
        return ((EditText) getView().findViewById(R.id.et_input_phone_num)).getText().toString().trim();
    }

    private String getInputPlace() {
        return ((EditText) getView().findViewById(R.id.et_input_place)).getText().toString().trim();
    }

    private String getInputName() {
        return ((EditText) getView().findViewById(R.id.et_input_name)).getText().toString().trim();
    }

    private String getInputPrice() {
        return ((EditText) getView().findViewById(R.id.et_input_price)).getText().toString().trim();
    }

    private void publishNewCar() {
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

        UploadManager uploadManager = new UploadManager();

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

            String profile = getInputProfile();
            String phone_num = getInputPhoneNum();
            String place = getInputPlace();
            String name = getInputName();
            String car_price = getInputPrice();

            if (EmptyUtils.isNotEmpty(profile) && EmptyUtils.isNotEmpty(phone_num) && EmptyUtils.isNotEmpty(place) && EmptyUtils.isNotEmpty(name) && EmptyUtils.isNotEmpty(car_price)) {
                OkHttpUtils.post()
                        .url(Constant.getPublishNewCarUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                        .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                        .addParams(Constant.FILES_ID, mPhotoFile.toString())
                        .addParams(Constant.CAR_COLOR, String.valueOf(2))
                        .addParams(Constant.CONTACT_NAME, name)
                        .addParams(Constant.CAR_INTRO, profile)
                        .addParams(Constant.PRICE, car_price)
                        .addParams(Constant.CONTACT_PHONE, phone_num)
                        .addParams(Constant.CONTACT_ADDRESS, place)
                        .addParams(Constant.CITY_ID, String.valueOf(0))
                        .addParams(Constant.BRAND_ID, String.valueOf(96))//车品牌id                    ooo(必填)
                        .addParams(Constant.MODEL_ID, String.valueOf(122458))//车型id                  ooo(必填)
                        .addParams(Constant.CAR_TYPE, String.valueOf(1))
                        .addParams(Constant.SERIES_ID, String.valueOf(2149))//车系列id                  ooo(必填)
                        .addParams(Constant.CAR_NO, "")//车牌号码
                        .addParams(Constant.ACTION, String.valueOf(1))//上传车类型
                        .addParams(Constant.IS_TRANSFER, String.valueOf(0))//是否过户
                        .addParams(Constant.CAR_STATUS, String.valueOf(0))//车辆状态
                        .addParams(Constant.VIN_NO, "")//vin码
                        .addParams(Constant.EXCHANGE_TIME, String.valueOf(0))//交易时间
                        .addParams(Constant.ENGINE_NO, "")//发动机号
                        .addParams(Constant.VIN_FILE, "")//vin文件
                        .addParams(Constant.MAINTAIN, String.valueOf(0))
                        .addParams(Constant.BOARD_TIME, "")
                        .addParams(Constant.MILEAGE, String.valueOf(0.000000))
                        .addParams(Constant.CAR_ID, "")
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
                                        ToastUtils.showShort("上传新车成功！");
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
