package com.wiserz.pbibi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 17:11.
 * QQ : 971060378
 * Used as : 我的钱包中充值页面
 */
public class RechargeFragment extends BaseFragment {

    private Context mContext;
    private TextView tv_recharge;
    private EditText et_input_place;
    private RadioButton rb_100;
    private RadioButton rb_200;
    private RadioButton rb_300;
    private RadioButton rb_500;
    private RadioButton rb_1000;
    private RadioButton rb_other;
    private String pay_type;
    private IWXAPI api;
    private MyBroadcastReceiver broadcastReceiver;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_PAY_FLAG_WEIXIN = 2;
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
                        goBack();
                        gotoPager(MyWalletFragment.class, null);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败");
                    }
                    break;
                case SDK_PAY_FLAG_WEIXIN:
                    String result = (String) msg.obj;
                    if (result.equals("RESULT_OK")) {
                        goBack();
                        gotoPager(MyWalletFragment.class, null);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("充值");
        view.findViewById(R.id.btn_register).setVisibility(View.INVISIBLE);

        tv_recharge = (TextView) view.findViewById(R.id.tv_recharge);
        et_input_place = (EditText) view.findViewById(R.id.et_input_place);
        //设置监听器
        et_input_place.addTextChangedListener(new MyTextWatcher());

        rb_100 = (RadioButton) view.findViewById(R.id.rb_100);
        rb_100.setOnClickListener(this);
        rb_200 = (RadioButton) view.findViewById(R.id.rb_200);
        rb_200.setOnClickListener(this);
        rb_300 = (RadioButton) view.findViewById(R.id.rb_300);
        rb_300.setOnClickListener(this);
        rb_500 = (RadioButton) view.findViewById(R.id.rb_500);
        rb_500.setOnClickListener(this);
        rb_1000 = (RadioButton) view.findViewById(R.id.rb_1000);
        rb_1000.setOnClickListener(this);
        rb_other = (RadioButton) view.findViewById(R.id.rb_other);
        rb_other.setOnClickListener(this);

        //默认选择200元
        tv_recharge.setText("¥200");
        rb_200.setChecked(true);

        view.findViewById(R.id.alipay_id).setOnClickListener(this);
        view.findViewById(R.id.wexinpay_id).setOnClickListener(this);
        wechatPay();//默认选择微信支付

        view.findViewById(R.id.btn_pay).setOnClickListener(this);

        //注册广播
        broadcastReceiver = new MyBroadcastReceiver();
        mContext.registerReceiver(broadcastReceiver, new IntentFilter("BROADCAST_CASH_RECHARGE_SUCCESS"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.rb_100:
                tv_recharge.setText("¥100");
                rb_200.setChecked(false);
                rb_300.setChecked(false);
                rb_500.setChecked(false);
                rb_1000.setChecked(false);
                rb_other.setChecked(false);
                tv_recharge.setVisibility(View.VISIBLE);
                et_input_place.setVisibility(View.GONE);
                showInputMethod(false);
                break;
            case R.id.rb_200:
                tv_recharge.setText("¥200");
                rb_100.setChecked(false);
                rb_300.setChecked(false);
                rb_500.setChecked(false);
                rb_1000.setChecked(false);
                rb_other.setChecked(false);
                tv_recharge.setVisibility(View.VISIBLE);
                et_input_place.setVisibility(View.GONE);
                showInputMethod(false);
                break;
            case R.id.rb_300:
                tv_recharge.setText("¥300");
                rb_100.setChecked(false);
                rb_200.setChecked(false);
                rb_500.setChecked(false);
                rb_1000.setChecked(false);
                rb_other.setChecked(false);
                tv_recharge.setVisibility(View.VISIBLE);
                et_input_place.setVisibility(View.GONE);
                showInputMethod(false);
                break;
            case R.id.rb_500:
                tv_recharge.setText("¥500");
                rb_100.setChecked(false);
                rb_200.setChecked(false);
                rb_300.setChecked(false);
                rb_1000.setChecked(false);
                rb_other.setChecked(false);
                tv_recharge.setVisibility(View.VISIBLE);
                et_input_place.setVisibility(View.GONE);
                showInputMethod(false);
                break;
            case R.id.rb_1000:
                tv_recharge.setText("¥1000");
                rb_100.setChecked(false);
                rb_200.setChecked(false);
                rb_300.setChecked(false);
                rb_500.setChecked(false);
                rb_other.setChecked(false);
                tv_recharge.setVisibility(View.VISIBLE);
                et_input_place.setVisibility(View.GONE);
                showInputMethod(false);
                break;
            case R.id.rb_other:
                tv_recharge.setText("¥0");
                rb_100.setChecked(false);
                rb_200.setChecked(false);
                rb_300.setChecked(false);
                rb_500.setChecked(false);
                rb_1000.setChecked(false);
                tv_recharge.setVisibility(View.GONE);
                et_input_place.setVisibility(View.VISIBLE);
                showInputMethod(true);
                break;
            case R.id.btn_pay:
                makeSurePay();
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

    private void makeSurePay() {
        String money = tv_recharge.getText().toString().trim().substring(1);
        if (money.equals("0")) {
            String money_et = et_input_place.getText().toString().trim();
            if (money_et.isEmpty()) {
                Toast.makeText(mContext, "请输入充值金额", Toast.LENGTH_SHORT).show();
                return;
            }
            money = money_et;
        }
        OkHttpUtils.post()
                .url(Constant.getSellerRechargeUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.MONEY, money)
                .addParams(Constant.PAY_TYPE, pay_type)
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
                                    //卖家充值的数据渲染
                                    showDataForSellerRecharge(jsonObjectData);
                                }
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showDataForSellerRecharge(JSONObject jsonObjectData) {
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

    private void showInputMethod(boolean show) {
        InputMethodManager imm = (InputMethodManager) et_input_place.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            et_input_place.requestFocus();
        } else {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(et_input_place.getWindowToken(), 0);
            }
        }
    }

    private void wechatPay() {
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.INVISIBLE);
            pay_type = "2";
            regToWx();
        }
    }

    private void aliPay() {
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.VISIBLE);
            pay_type = "1";
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int len = s.toString().length();
            if (len == 1 && text.equals("0")) {
                Toast.makeText(mContext, "金额不能以0开头", Toast.LENGTH_SHORT).show();
                s.clear();
            }
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constant.APP_ID);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG_WEIXIN;
                    msg.obj = "RESULT_OK";
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }
}
