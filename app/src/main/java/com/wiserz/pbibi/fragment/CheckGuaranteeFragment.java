package com.wiserz.pbibi.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 10:35.
 * QQ : 971060378
 * Used as : 查维保的页面
 */
public class CheckGuaranteeFragment extends BaseFragment {

    private EditText et_input_vin;
    private String pay_type;
    private IWXAPI api;
    private TextView tv_brand;

    private String report_sn;

    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付成功");
                        DataManager.getInstance().setData1(report_sn);
                        gotoPager(GuaranteeDetailFragment.class, null);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_guarantee;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查维保");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("查询历史");
        btn_register.setTextSize(15);
        btn_register.setTextColor(getResources().getColor(R.color.second_text_color));
        btn_register.setOnClickListener(this);

        et_input_vin = (EditText) view.findViewById(R.id.et_input_vin);
        tv_brand = (TextView) view.findViewById(R.id.tv_brand);
        view.findViewById(R.id.btn_check).setOnClickListener(this);

        view.findViewById(R.id.rl_check_brand).setOnClickListener(this);

        view.findViewById(R.id.wexinpay_id).setOnClickListener(this);
        view.findViewById(R.id.alipay_id).setOnClickListener(this);

        regToWx();//注册微信支付
        wechatPay();//默认选择微信支付
    }

    private void wechatPay() {
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.INVISIBLE);
            pay_type = "2";
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constant.APP_ID);
    }

    private void aliPay() {
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.VISIBLE);
            pay_type = "1";
        }
    }

    private String getInputVin() {
        return et_input_vin.getText().toString().trim();
    }

    private String getBrandInfo() {
        return tv_brand.getText().toString().trim();
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
            case R.id.btn_check:
                startCheck();
                break;
            case R.id.rl_check_brand:
                gotoPager(SelectCarBrandFragment.class, null);
                break;
            case R.id.alipay_id:
                aliPay();
                break;
            case R.id.wexinpay_id:
                wechatPay();
                break;
            default:
                break;
        }
    }

    private void startCheck() {
        String vin_string = getInputVin();
        String brand_info = getBrandInfo();
        if (EmptyUtils.isEmpty(vin_string)) {
            ToastUtils.showShort("请输入VIN码！");
            return;
        }
        if (EmptyUtils.isEmpty(brand_info)) {
            ToastUtils.showShort("请选择车辆的品牌！");
            return;
        }
        //测试数据：vin=LGBP12E21DY196239&brand_id=4
        OkHttpUtils.post()
                .url(Constant.getCheckGuaranteeUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.PAY_TYPE, pay_type)
                .addParams(Constant.VIN, vin_string)
                .addParams(Constant.BRAND_ID, brand_info)
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
                                report_sn = jsonObjectData.optString("report_sn");
                                if (getView() != null) {
                                    handlerDataForPay(jsonObjectData);
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

    private void handlerDataForPay(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            //得到预约支付的数据
            String type = jsonObjectData.optString("type");
            if (type.equals("Wxpay")) {
                //微信支付
                String appid = jsonObjectData.optString("appid");
                String noncestr = jsonObjectData.optString("noncestr");
                String packages = jsonObjectData.optString("package");
                String partnerid = jsonObjectData.optString("partnerid");
                String prepayid = jsonObjectData.optString("prepayid");
                String sign = jsonObjectData.optString("sign");
                String timestamp = jsonObjectData.optString("timestamp");

                PayReq request = new PayReq();
                request.appId = appid;
                request.partnerId = partnerid;
                request.prepayId = prepayid;
                request.packageValue = packages;
                request.nonceStr = noncestr;
                request.timeStamp = timestamp;
                request.sign = sign;
                api.sendReq(request);
            } else if (type.equals("Alipay")) {
                final String orderInfo = jsonObjectData.optString("orderstr");// 订单信息
                ///支付宝支付
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(getActivity());
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            LogUtils.e("onHiddenChanged");
        } else {  // 在最前端显示 相当于调用了onResume();
            //网络数据刷新
            LogUtils.e("否则");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.e("setUserVisibleHint");
        } else {
            LogUtils.e("否则");
        }
    }
}
