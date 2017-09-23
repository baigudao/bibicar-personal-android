package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/18 10:26.
 * QQ : 971060378
 * Used as : 查维保详情的页面
 */
public class GuaranteeDetailFragment extends BaseFragment {

    private String report_sn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guarantee_detail;
    }

    @Override
    protected void initView(View view) {
        report_sn = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("维保记录");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(report_sn)) {
            getDataFromNet();
        } else {
            ToastUtils.showShort("报告单号为空！");
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getCheckDetailUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.REPORT_SN, report_sn)
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
                                String logo = jsonObjectData.optJSONObject("brand_info").optString("logo");
                                String name = jsonObjectData.optJSONObject("brand_info").optString("name");
                                String vin = jsonObjectData.optString("vin");
                                if (getView() != null) {
                                    Glide.with(mContext)
                                            .load(logo)
                                            .placeholder(R.drawable.user_photo)
                                            .error(R.drawable.user_photo)
                                            .into((ImageView) getView().findViewById(R.id.iv_image_brand));
                                    ((TextView) getView().findViewById(R.id.tv_car_brand)).setText("车型：" + name);
                                    ((TextView) getView().findViewById(R.id.tv_car_vin)).setText("VIN: " + vin);
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
