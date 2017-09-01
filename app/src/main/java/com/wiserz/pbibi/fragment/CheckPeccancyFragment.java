package com.wiserz.pbibi.fragment;

import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.ProvinceBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.view.WheelViewPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

import static com.wiserz.pbibi.util.CommonUtil.isListNullOrEmpty;

/**
 * Created by jackie on 2017/8/15 9:42.
 * QQ : 971060378
 * Used as : 查违章的页面
 */
public class CheckPeccancyFragment extends BaseFragment {

    private ArrayList<ProvinceBean> mProvinceBeenList;
    private ProvinceBean mSelectProvince;
    private CityBean mSelectCity;
    private WheelViewPopupWindow mWheelViewPopupWindow;

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

        view.findViewById(R.id.rl_check_city).setOnClickListener(this);
        view.findViewById(R.id.tv_city_short).setOnClickListener(this);
        view.findViewById(R.id.btn_start_check).setOnClickListener(this);

        mProvinceBeenList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("查询历史");
                break;
            case R.id.rl_check_city:
                if (CommonUtil.isListNullOrEmpty(mProvinceBeenList)) {
                    getProvinceList();
                }
                mWheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
                    @Override
                    public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                        mSelectProvince = (ProvinceBean) value1;
                        mSelectCity = (CityBean) value2;
                        if (EmptyUtils.isNotEmpty(mSelectProvince) && EmptyUtils.isNotEmpty(mSelectCity) && getView() != null) {
                            ((TextView) getView().findViewById(R.id.tv_city)).setText(mSelectProvince.getProvince() + " " + mSelectCity.getCity_name());
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
                ToastUtils.showShort("立即查询");
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isListNullOrEmpty(mProvinceBeenList)) {
            getProvinceList();
        }
    }

    private void showProvinceShort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(R.layout.custom_dialog_province);
        builder.create().show();
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
}
