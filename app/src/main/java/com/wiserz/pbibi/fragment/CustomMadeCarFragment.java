package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/4 11:15.
 * QQ : 971060378
 * Used as : 订制车辆的页面
 */
public class CustomMadeCarFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_made_car;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("订制车辆");
        view.findViewById(R.id.btn_start_made).setOnClickListener(this);
    }

    private String getCustomMadeCar() {
        return ((EditText) getView().findViewById(R.id.et_input_car)).getText().toString().trim();
    }

    private String getCustomMadeCarYear() {
        return ((EditText) getView().findViewById(R.id.et_input_car_year)).getText().toString().trim();
    }

    private String getCustomMadeCarPhone() {
        return ((EditText) getView().findViewById(R.id.et_input_phone)).getText().toString().trim();
    }

    private String getCustomMadeCarOther() {
        return ((EditText) getView().findViewById(R.id.et_input_other)).getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_start_made:
                startCustomMadeCar();
                break;
            default:
                break;
        }
    }

    private void startCustomMadeCar() {
        String car_brand = getCustomMadeCar();
        String car_year = getCustomMadeCarYear();
        String car_phone = getCustomMadeCarPhone();
        String car_other = getCustomMadeCarOther();
        if (EmptyUtils.isNotEmpty(car_brand) && EmptyUtils.isNotEmpty(car_year) && EmptyUtils.isNotEmpty(car_phone) && EmptyUtils.isNotEmpty(car_other)) {
            OkHttpUtils.post()
                    .url(Constant.getCustomMadeCarUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.BRAND_NAME, car_brand)
                    .addParams(Constant.AGE, car_year)
                    .addParams(Constant.PHONE, car_phone)
                    .addParams(Constant.DESC, car_other)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.e(response);
                        }
                    });
        } else {
            ToastUtils.showShort("请输入完整信息");
        }
    }
}
