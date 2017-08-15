package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.bean.CarRentOrderBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 16:32.
 * QQ : 971060378
 * Used as : 创建租车订单的页面
 */
public class CreateCarRentOrderFragment extends BaseFragment {

    private CarRentInfoBean carRentInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_car_rent;
    }

    @Override
    protected void initView(View view) {
        carRentInfoBean = (CarRentInfoBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("创建订单");

        view.findViewById(R.id.btn_pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_pay:
                createCarRentOrder();
                break;
            default:
                break;
        }
    }

    /**
     * 创建租车订单
     */
    private void createCarRentOrder() {
        OkHttpUtils.post()
                .url(Constant.getCreateCarRentOrderUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.CAR_ID, carRentInfoBean.getCar_id())
                .addParams(Constant.RENTAL_TIME_START, "450")
                .addParams(Constant.RENTAL_TIME_END, "9854")
                .addParams(Constant.MOBILE, "13325458956")
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
                                JSONObject jsonObjectOrderInfo = jsonObjectData.optJSONObject("order_info");
                                Gson gson = new Gson();
                                CarRentOrderBean carRentOrderBean = gson.fromJson(jsonObjectOrderInfo.toString(), CarRentOrderBean.class);
                                if (EmptyUtils.isNotEmpty(carRentOrderBean)) {
                                    createCarRentPay(carRentOrderBean.getOrder_sn());
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

    /**
     * 创建租车支付
     *
     * @param order_sn
     */
    private void createCarRentPay(String order_sn) {
        OkHttpUtils.post()
                .url(Constant.getCreateCarRentPayUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.PAY_TYPE, "2")
                .addParams(Constant.ORDER_SN, order_sn)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
}
