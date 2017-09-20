package com.wiserz.pbibi.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.ThemeManager;
import com.rey.material.app.TimePickerDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.bean.CarRentDetailInfoBean;
import com.wiserz.pbibi.bean.CarRentOrderBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 16:32.
 * QQ : 971060378
 * Used as : 创建租车订单的页面
 */
public class CreateCarRentOrderFragment extends BaseFragment {

    private IWXAPI iwxapi;
    private CarRentDetailInfoBean carRentDetailInfoBean;

    private static final int SDK_PAY_FLAG = 1;
    private int pay_type;

    private int time;//进入日期时间选择器的次数

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
        carRentDetailInfoBean = (CarRentDetailInfoBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("创建订单");

        if (EmptyUtils.isNotEmpty(carRentDetailInfoBean)) {
            Glide.with(mContext)
                    .load(carRentDetailInfoBean.getFiles().getType1().get(0).getFile_url())
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                    .into((ImageView) view.findViewById(R.id.iv_car_image));
            ((TextView) view.findViewById(R.id.tv_car_name)).setText(carRentDetailInfoBean.getCar_name());
            ((TextView) view.findViewById(R.id.tv_subscription)).setText("¥" + carRentDetailInfoBean.getRental_info().getDeposit() + "元/天");//订金
            ((TextView) view.findViewById(R.id.tv_deposit)).setText("¥" + carRentDetailInfoBean.getRental_info().getSubscription() + "元/天");//押金
            ((TextView) view.findViewById(R.id.tv_rent_price)).setText("¥" + carRentDetailInfoBean.getRental_info().getOne() + "元/天");//租金

            double sum = carRentDetailInfoBean.getRental_info().getDeposit() + carRentDetailInfoBean.getRental_info().getOne() +
                    carRentDetailInfoBean.getRental_info().getSubscription();
            ((TextView) view.findViewById(R.id.tv_cost)).setText("费用：" + sum + "元");
        }

        view.findViewById(R.id.rl_weixinpay).setOnClickListener(this);
        view.findViewById(R.id.rl_alipay).setOnClickListener(this);

        view.findViewById(R.id.btn_choose_time).setOnClickListener(this);
        view.findViewById(R.id.btn_pay).setOnClickListener(this);

        time = 0;

        weixinPay();
    }

    private void aliPay() {//支付方式 1:支付宝 2微信
        pay_type = 1;
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.GONE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.VISIBLE);
        }
    }

    private void weixinPay() {
        pay_type = 2;
        regToWx();
        if (getView() != null) {
            getView().findViewById(R.id.iv_weixin_choose).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.iv_alipay_choose).setVisibility(View.GONE);
        }
    }

    private String getName() {
        return ((EditText) getView().findViewById(R.id.et_input_name)).getText().toString().trim();
    }

    private String getPhone() {
        return ((EditText) getView().findViewById(R.id.et_input_phone)).getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_choose_time:
                chooseData();
                break;
            case R.id.rl_weixinpay:
                weixinPay();
                break;
            case R.id.rl_alipay:
                aliPay();
                break;
            case R.id.btn_pay:
                createCarRentOrder();
                break;
            default:
                break;
        }
    }

    /**
     * 选择日期
     */
    private void chooseData() {
        ++time;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(isLightTheme ? R.style.Material_App_Dialog_DatePicker_Light : R.style.Material_App_Dialog_DatePicker) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                if (getView() != null) {
                    switch (time) {
                        case 1:
                            ((TextView) getView().findViewById(R.id.tv_rent_date)).setText((dialog.getMonth() + 1) + "-" + dialog.getDay());//07-13
                            chooseTime();//选择时间
                            break;
                        case 3:
                            ((TextView) getView().findViewById(R.id.tv_back_date)).setText((dialog.getMonth() + 1) + "-" + dialog.getDay());//07-13
                            chooseTime();
                            break;
                        default:
                            break;
                    }
                }
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        builder.positiveAction("确定")
                .negativeAction("取消");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    /**
     * 选择时间
     */
    private void chooseTime() {
        ++time;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(isLightTheme ? R.style.Material_App_Dialog_TimePicker_Light : R.style.Material_App_Dialog_TimePicker, 24, 00) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                if (getView() != null) {
                    switch (time) {
                        case 2:
                            ((TextView) getView().findViewById(R.id.tv_rent_time)).setText(dialog.getHour() + ":" + dialog.getMinute());//18:00
                            chooseData();
                            break;
                        case 4:
                            ((TextView) getView().findViewById(R.id.tv_back_time)).setText(dialog.getHour() + ":" + dialog.getMinute());//18:00
                            break;
                        default:
                            break;
                    }
                }
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        builder.positiveAction("确定")
                .negativeAction("取消");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    private String getRentCarTime() {
        return ((TextView) getView().findViewById(R.id.tv_rent_date)).getText().toString() + ((TextView) getView().findViewById(R.id.tv_rent_time)).getText().toString();
    }

    private String getBackCarTime() {
        return ((TextView) getView().findViewById(R.id.tv_back_date)).getText().toString() + ((TextView) getView().findViewById(R.id.tv_back_time)).getText().toString();
    }

    /**
     * 创建租车订单
     */
    private void createCarRentOrder() {
        if (EmptyUtils.isEmpty(getName()) && EmptyUtils.isEmpty(getPhone())) {
            ToastUtils.showShort("请输入姓名和手机号");
            return;
        }
        OkHttpUtils.post()
                .url(Constant.getCreateCarRentOrderUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.CAR_ID, carRentDetailInfoBean.getCar_id())
                .addParams(Constant.RENTAL_TIME_START, getRentCarTime())
                .addParams(Constant.RENTAL_TIME_END, getBackCarTime())
                .addParams(Constant.MOBILE, SPUtils.getInstance().getString(Constant.ACCOUNT))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
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
                .addParams(Constant.PAY_TYPE, String.valueOf(pay_type))
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
