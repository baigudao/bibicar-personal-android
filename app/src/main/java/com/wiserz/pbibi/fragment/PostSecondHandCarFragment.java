package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.ProvinceBean;
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
    private EditText et_input_name;
    private EditText et_input_vin_no;
    private EditText et_input_table_mileage;
    private EditText et_input_price;
    private ImageView iv_image_vin;
    private TextView tv_car_color;
    private TextView tv_car_type;
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

    private ArrayList<File> mUploadPhotos;
    private String upload_token;
    private ArrayList<CarConfiguration> mCarConfigurationList;
    private ArrayList<String> mSelectedConfig = new ArrayList<>();

    private ArrayList<File> getUploadPhotos() {
        if (mUploadPhotos == null) {
            mUploadPhotos = new ArrayList<>();
        }
        return mUploadPhotos;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_second_hand_car;
    }

    @Override
    protected void initView(View view) {
        DataManager.getInstance().setObject(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传二手车");

        view.findViewById(R.id.btn_post_second_car).setOnClickListener(this);
        view.findViewById(R.id.btn_post_second_car_2).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_color).setOnClickListener(this);
        view.findViewById(R.id.rl_first_post_license).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_type).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_brand_place).setOnClickListener(this);
        ll_image_vin = (LinearLayout) view.findViewById(R.id.ll_image_vin);
        tv_car_color = (TextView) view.findViewById(R.id.tv_car_color);
        tv_car_type = (TextView) view.findViewById(R.id.tv_car_type);

        view.findViewById(R.id.tvCloseWarning).setOnClickListener(this);
        view.findViewById(R.id.tvDetailMsg).setOnClickListener(this);
        view.findViewById(R.id.tvBasicMsg).setOnClickListener(this);

        et_input_profile = (EditText) view.findViewById(R.id.et_input_profile);
        et_input_phone_num = (EditText) view.findViewById(R.id.et_input_phone_num);
        et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_price = (EditText) view.findViewById(R.id.et_input_price);
        et_input_vin_no = (EditText) view.findViewById(R.id.et_input_vin_no);
        et_input_table_mileage = (EditText) view.findViewById(R.id.et_input_table_mileage);
        ImageView iv_add_car_vin = (ImageView) view.findViewById(R.id.iv_add_car_vin);
        iv_add_car_vin.setOnClickListener(this);
        iv_image_vin = (ImageView) view.findViewById(R.id.iv_image_vin);
        iv_image_vin.setOnClickListener(this);

        mProvinceBeenList = new ArrayList<>();
        uploadManager = new UploadManager();

        mColor=0;
        resetColorView(mColor);

        getTokenFromNet();
        getCarExtraInfo();
        gotoPager(SelectPhotoFragment.class, null, true);
    }

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
                                resetCarConfigurations(mCarConfigurationList);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
        LinearLayout llDetailMsg = (LinearLayout) getView().findViewById(R.id.llDetailMsg);
        llDetailMsg.removeAllViews();
        mSelectedConfig.clear();
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
            int rowCount = 3;
            int typeId = config.getType_id();
            if (typeId == 3 || typeId == 4 || typeId == 5) {
                rowCount = 2;
            }
            int row = itemCount % rowCount == 0 ? itemCount / rowCount : itemCount / rowCount + 1;
            LinearLayout itemLayout;
            TextView tvName1, tvName2, tvName3;
            for (int j = 0; j < row; ++j) {
                itemLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_configuration_detail, null);
                ((LinearLayout) layout.findViewById(R.id.llDetails)).addView(itemLayout);
                tvName1 = ((TextView) itemLayout.findViewById(R.id.tvName1));
                tvName2 = ((TextView) itemLayout.findViewById(R.id.tvName2));
                tvName3 = ((TextView) itemLayout.findViewById(R.id.tvName3));
                if (j * rowCount < itemCount) {
                    tvName1.setText(itemList.get(j * rowCount).getName());
                    tvName1.setTag(R.id.tag, itemList.get(j * rowCount).getId());
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
                }
                if (j * rowCount + 1 < itemCount) {
                    tvName2.setText(itemList.get(j * rowCount + 1).getName());
                    tvName2.setTag(R.id.tag, itemList.get(j * rowCount).getId());
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
                }
                if (rowCount == 2) {
                    tvName3.setVisibility(View.GONE);
                } else {
                    if (j * rowCount + 2 < itemCount) {
                        tvName3.setText(itemList.get(j * rowCount + 2).getName());
                        tvName3.setTag(R.id.tag, itemList.get(j * rowCount + 2).getId());
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
                    }
                }
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
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
            getUploadPhotos().set(index, new File(newPath));
            DataManager.getInstance().setObject(null);
            DataManager.getInstance().setData1(null);
        } else if (DataManager.getInstance().getData8() != null) {
            int index = (int) DataManager.getInstance().getData8();
            getUploadPhotos().remove(index);
            DataManager.getInstance().setData8(null);
        } else if (DataManager.getInstance().getObject() != null) {
            getUploadPhotos().addAll((ArrayList<File>) DataManager.getInstance().getObject());
            DataManager.getInstance().setObject(null);
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


        resetCarPhotosView();
    }

    private void resetCarPhotosView() {
        LinearLayout llCarPhotos = (LinearLayout) getView().findViewById(R.id.llCarPhotos);
        llCarPhotos.removeAllViews();
        int size;
        if (getUploadPhotos().size() == Constant.MAX_UPLOAD_PHOTO_NUM) {
            size = Constant.MAX_UPLOAD_PHOTO_NUM;
        } else {
            size = getUploadPhotos().size() + 1;
        }
        int row = (size % 3 == 0) ? size / 3 : size / 3 + 1;
        LinearLayout itemView;
        for (int i = 0; i < row; ++i) {
            itemView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_car_photo, null);
            llCarPhotos.addView(itemView);
            int index = i * 3;
            setPhoto((ViewGroup) itemView.getChildAt(0), index < getUploadPhotos().size() ? getUploadPhotos().get(index) : null, index, index == size - 1);
            setPhoto((ViewGroup) itemView.getChildAt(1), index + 1 < getUploadPhotos().size() ? getUploadPhotos().get(index + 1) : null, index + 1, index + 1 == size - 1);
            setPhoto((ViewGroup) itemView.getChildAt(2), index + 2 < getUploadPhotos().size() ? getUploadPhotos().get(index + 2) : null, index + 2, index + 2 == size - 1);
        }
    }

    private void setPhoto(ViewGroup itemView, File photo, int index, boolean isLast) {
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
                        DataManager.getInstance().setObject(getUploadPhotos().get(pos).getAbsolutePath());
                        DataManager.getInstance().setData1(pos);
                        DataManager.getInstance().setData2(getUploadPhotos().size());
                        ((BaseActivity) mContext).gotoPager(EditPhotoFragment.class, null, true);
                    }
                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    File file = getUploadPhotos().remove(pos);
                    if (file != null) {
                        file.delete();
                    }
                    resetCarPhotosView();
                }
            });
        } else {
            if (isLast) {
                ivPhoto.setImageResource(R.drawable.add_car_photo);
                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataManager.getInstance().setObject(getUploadPhotos().size());
                        gotoPager(SelectPhotoFragment.class, null, true);
                    }
                });
                ivClose.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
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
            case R.id.btn_post_second_car_2:
            case R.id.btn_post_second_car:
                publishSecondCar();
                break;
            case R.id.tvBasicMsg:
                ((TextView) getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                ((TextView) getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.line2).setVisibility(View.GONE);
                getView().findViewById(R.id.scrollBasicMsg).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.scrollDetailMsg).setVisibility(View.GONE);
                break;
            case R.id.tvDetailMsg:
                ((TextView) getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                ((TextView) getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.GONE);
                getView().findViewById(R.id.line2).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.scrollBasicMsg).setVisibility(View.GONE);
                getView().findViewById(R.id.scrollDetailMsg).setVisibility(View.VISIBLE);
                break;
            case R.id.tvCloseWarning:
                getView().findViewById(R.id.rlWarning).setVisibility(View.GONE);
                break;
            case R.id.rl_choose_car_color:
                gotoPager(SelectCarColorFragment.class, null);
                break;
            case R.id.rl_first_post_license:
                showWheelView((TextView) getView().findViewById(R.id.tv_first_post_license), WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA);
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
                            ((TextView) getView().findViewById(R.id.tv_brand_place)).setText(mSelectCity.getCity_name());
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
            default:
                break;
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
        String text = res.getString(res.getIdentifier("car_color_" + color, "string", getActivity().getPackageName()));
        tv_car_color.setText(text);
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

        if (EmptyUtils.isEmpty(model_id) || model_id == 0) {
            ToastUtils.showShort("请选择车型");
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
            uploadImage(getUploadPhotos(), upload_token);
        } else {
            ToastUtils.showShort("请添加车辆照片！");
        }
    }

    private void uploadImage(ArrayList<File> uploadPhotos, String upload_token) {
        final int mSize = uploadPhotos.size();

        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();

        for (int i = 0; i < mSize; i++) {
            File photoPath = uploadPhotos.get(i);

            if (EmptyUtils.isNotEmpty(photoPath)) {
                int file_type = 1;
                if (EmptyUtils.isNotEmpty(file_type)) {
                    uploadManager.put(photoPath, UUID.randomUUID().toString() + "_" + String.valueOf(file_type), upload_token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.e("aaaaaaaaaa", "response: " + response.toString());
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
                    .addParams(Constant.CONTACT_ADDRESS, "")
                    .addParams(Constant.CITY_ID, "")
                    .addParams(Constant.BRAND_ID, String.valueOf(brand_id))//车品牌id                    ooo(必填)
                    .addParams(Constant.SERIES_ID, String.valueOf(series_id))//车系列id                  ooo(必填)
                    .addParams(Constant.MODEL_ID, String.valueOf(model_id))//车型id                      ooo(必填)
                    .addParams(Constant.CAR_TYPE, String.valueOf(1))
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
