package com.wiserz.pbibi.fragment;

import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.PeccancyRecordBean;
import com.wiserz.pbibi.bean.ProvinceBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.WheelViewPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 9:42.
 * QQ : 971060378
 * Used as : 查违章的页面
 */
public class CheckPeccancyFragment extends BaseFragment {

    private ArrayList<ProvinceBean> mProvinceBeenList;
    private ProvinceBean mSelectProvince;
    private CityBean mSelectCity;

    private EditText et_engine_num;
    private EditText et_car_frame_num;
    private EditText et_car_num;
    private TextView tv_city_short;
    private TextView tv_city;

    private String cityCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_peccancy;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查违章");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setText("查询历史");
        btn_register.setTextSize(15);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setTextColor(getResources().getColor(R.color.second_text_color));
        btn_register.setOnClickListener(this);

        et_engine_num = (EditText) view.findViewById(R.id.et_engine_num);
        et_car_frame_num = (EditText) view.findViewById(R.id.et_car_frame_num);
        et_car_num = (EditText) view.findViewById(R.id.et_car_num);
        tv_city_short = (TextView) view.findViewById(R.id.tv_city_short);
        tv_city = (TextView) view.findViewById(R.id.tv_city);

        view.findViewById(R.id.rl_check_city).setOnClickListener(this);
        view.findViewById(R.id.tv_city_short).setOnClickListener(this);
        view.findViewById(R.id.btn_start_check).setOnClickListener(this);

        mProvinceBeenList = new ArrayList<>();
    }

    private String getEngineNum() {
        return et_engine_num.getText().toString().trim();
    }

    private String getFrameNum() {
        return et_car_frame_num.getText().toString().trim();
    }

    private String getCarNum() {
        return et_car_num.getText().toString().trim();
    }

    private String getCityShort() {
        return tv_city_short.getText().toString().trim();
    }

    private String getCity() {
        return getCityCode() == null ? "GD_SZ" : getCityCode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                gotoPager(CheckHistoryFragment.class, null);
                break;
            case R.id.rl_check_city:
                if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
                    getProvinceList();
                }
                WheelViewPopupWindow mWheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
                    @Override
                    public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                        mSelectProvince = (ProvinceBean) value1;
                        mSelectCity = (CityBean) value2;
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
            case R.id.tv_city_short:
                showProvinceShort();
                break;
            case R.id.btn_start_check:
                startCheck();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
            getProvinceList();
        }
    }

    private void showProvinceShort() {
        View view = View.inflate(mContext, R.layout.custom_dialog_province, null);

        final TextView tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("1");
            }
        });
        TextView tv2 = (TextView) view.findViewById(R.id.tv2);
        TextView tv3 = (TextView) view.findViewById(R.id.tv3);
        TextView tv4 = (TextView) view.findViewById(R.id.tv4);
        TextView tv5 = (TextView) view.findViewById(R.id.tv5);
        TextView tv6 = (TextView) view.findViewById(R.id.tv6);
        TextView tv7 = (TextView) view.findViewById(R.id.tv7);
        TextView tv8 = (TextView) view.findViewById(R.id.tv8);
        TextView tv9 = (TextView) view.findViewById(R.id.tv9);
        TextView tv10 = (TextView) view.findViewById(R.id.tv10);
        TextView tv11 = (TextView) view.findViewById(R.id.tv11);
        TextView tv12 = (TextView) view.findViewById(R.id.tv12);
        TextView tv13 = (TextView) view.findViewById(R.id.tv13);
        TextView tv14 = (TextView) view.findViewById(R.id.tv14);
        TextView tv15 = (TextView) view.findViewById(R.id.tv15);
        TextView tv16 = (TextView) view.findViewById(R.id.tv16);
        TextView tv17 = (TextView) view.findViewById(R.id.tv17);
        TextView tv18 = (TextView) view.findViewById(R.id.tv18);
        TextView tv19 = (TextView) view.findViewById(R.id.tv19);
        TextView tv20 = (TextView) view.findViewById(R.id.tv20);
        TextView tv21 = (TextView) view.findViewById(R.id.tv21);
        TextView tv22 = (TextView) view.findViewById(R.id.tv22);
        TextView tv23 = (TextView) view.findViewById(R.id.tv23);
        TextView tv24 = (TextView) view.findViewById(R.id.tv24);
        TextView tv25 = (TextView) view.findViewById(R.id.tv25);
        TextView tv26 = (TextView) view.findViewById(R.id.tv26);
        TextView tv27 = (TextView) view.findViewById(R.id.tv27);
        TextView tv28 = (TextView) view.findViewById(R.id.tv28);
        TextView tv29 = (TextView) view.findViewById(R.id.tv29);
        TextView tv30 = (TextView) view.findViewById(R.id.tv30);
        TextView tv31 = (TextView) view.findViewById(R.id.tv31);
        TextView tv32 = (TextView) view.findViewById(R.id.tv32);
        TextView tv33 = (TextView) view.findViewById(R.id.tv33);
        TextView tv34 = (TextView) view.findViewById(R.id.tv34);

        for (int i = 1; i < 35; i++) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
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

    private void startCheck() {
        String engineNum = getEngineNum();
        String frameNum = getFrameNum();
        String carNum = getCarNum();
        String cityShort = getCityShort();
        if (EmptyUtils.isEmpty(engineNum)) {
            ToastUtils.showShort("请输入发动机号！");
            return;
        }
        if (EmptyUtils.isEmpty(frameNum)) {
            ToastUtils.showShort("请输入车架号！");
            return;
        }
        if (EmptyUtils.isEmpty(carNum)) {
            ToastUtils.showShort("请输入车牌号");
            return;
        }
        OkHttpUtils.post()
                .url(Constant.getCheckPeccancyUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.CITY, getCity())
                .addParams(Constant.HPHM, cityShort + carNum)//车牌号
                .addParams(Constant.CLASSNO, frameNum)//车架后六位
                .addParams(Constant.ENGINENO, engineNum)//发动机号六位
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
                                String resultcode = jsonObjectData.optString("resultcode");
                                if (EmptyUtils.isNotEmpty(resultcode) && resultcode.equals("200")) {
                                    Gson gson = new Gson();
                                    PeccancyRecordBean peccancyRecordBean = gson.fromJson(jsonObjectData.optJSONObject("result").toString(), PeccancyRecordBean.class);
                                    if (EmptyUtils.isNotEmpty(peccancyRecordBean)) {
                                        DataManager.getInstance().setData1(peccancyRecordBean);
                                        gotoPager(PeccancyRecordFragment.class, null);
                                    } else {
                                        ToastUtils.showShort("没有数据");
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
}
