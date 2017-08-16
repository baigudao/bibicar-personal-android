package com.wiserz.pbibi.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.bean.CarRentOrderBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 16:32.
 * QQ : 971060378
 * Used as : 创建租车订单的页面
 */
public class CreateCarRentOrderFragment extends BaseFragment {

    private CarRentInfoBean carRentInfoBean;
    private IWXAPI iwxapi;

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
                        goBack();
                        ToastUtils.showShort("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败");
                    }
                default:
                    break;
            }
        }
    };

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

        regToWx();
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
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                if (getView() != null) {
                                    //处理支付数据
                                    handlePayData(jsonObjectData);
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

    private void handlePayData(JSONObject jsonObjectData) {
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
            iwxapi.sendReq(request);
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

    /**
     * 注册微信支付
     */
    private void regToWx() {
        iwxapi = WXAPIFactory.createWXAPI(mContext, Constant.APP_ID, false);//false：表示checkSignature
        iwxapi.registerApp(Constant.APP_ID);
    }
}
