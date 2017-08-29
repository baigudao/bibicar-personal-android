package com.wiserz.pbibi.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by jackie on 2017/8/21 18:27.
 * QQ : 971060378
 * Used as : 汽车服务的页面
 */
public class CarServiceFragment extends BaseFragment {

    private static final String BASE_URL = "http://wap.bibicar.cn/insurance?ident=";
    private static final String ADD_URL = "&session=";

    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api;

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
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败");
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regToWx();//注册微信支付
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_service;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("汽车服务");

        WebView wb = (WebView) view.findViewById(R.id.webview_for_server);
        String webview_for_server = BASE_URL + Arrays.toString(EncodeUtils.base64Encode(SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER)))
                + ADD_URL + Arrays.toString(EncodeUtils.base64Encode(SPUtils.getInstance().getString(Constant.SESSION_ID)));
        if (!TextUtils.isEmpty(webview_for_server)) {
            wb.getSettings().setJavaScriptEnabled(true);
            wb.getSettings().setSupportZoom(true);
            wb.getSettings().setUseWideViewPort(true);
            wb.getSettings().setLoadWithOverviewMode(true);
            wb.getSettings().setDomStorageEnabled(true);
            wb.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
            String appCachePath = BaseApplication.getAppContext().getCacheDir().getAbsolutePath();
            wb.getSettings().setAppCachePath(appCachePath);
            wb.getSettings().setAllowFileAccess(true);
            wb.getSettings().setAppCacheEnabled(true);
            wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT > 8) {
                    wb.getSettings().setPluginState(WebSettings.PluginState.ON);
                }
            }
            wb.loadUrl(webview_for_server);
        }

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String result_url = url.split("//")[1];
                if (result_url.startsWith("pay?info=")) {

                    String u = url.split("info=")[1];
                    try {
                        String decode_str = URLDecoder.decode(u, "UTF-8");
                        JSONObject jo = new JSONObject(decode_str);
                        String type = jo.getString("type");

                        if (type.equals("Wxpay")) {
                            //微信支付
                            PayReq request = new PayReq();
                            request.appId = jo.getString("appid");
                            request.partnerId = jo.getString("partnerid");
                            request.prepayId = jo.getString("prepayid");
                            request.packageValue = jo.getString("package");
                            request.nonceStr = jo.getString("noncestr");
                            request.timeStamp = String.valueOf(jo.getLong("timestamp"));
                            request.sign = jo.getString("sign");
                            api.sendReq(request);
                        } else {
                            ///支付宝支付
                            final String orderInfo = jo.getString("orderstr"); // 订单信息
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
                        return true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
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

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
    }
}
