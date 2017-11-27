package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.alipay.PayResult;
import com.wiserz.pbibi.bean.BannerBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by jackie on 2017/8/24 22:26.
 * QQ : 971060378
 * Used as : 轮播图的页面
 */

public class BannerFragment extends BaseFragment {

    private BannerBean bannerBean;
    private IWXAPI api;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到传过来的数据
        bannerBean = (BannerBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        //注册微信支付
        regToWx();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(mContext, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
    }


    @Override
    protected int getLayoutId() {
        return bannerBean.getType().equals(String.valueOf(0)) ? R.layout.fragment_banner_web : R.layout.fragment_banner;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);

        if (EmptyUtils.isNotEmpty(bannerBean)) {
            if (bannerBean.getType().equals("0")) {
                //进入网页
                ((TextView) view.findViewById(R.id.tv_title)).setText("web页面");
                ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                iv_image.setVisibility(View.VISIBLE);
                iv_image.setImageResource(R.drawable.share_selector);
                iv_image.setOnClickListener(this);

                WebView webView = (WebView) view.findViewById(R.id.tencent_web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
                String appCachePath = BaseApplication.getAppContext().getCacheDir().getAbsolutePath();
                webView.getSettings().setAppCachePath(appCachePath);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAppCacheEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        String result_url = url.split("//")[1];
                        if (result_url.equals("success")) {
                            ToastUtils.showShort("发布我的爱车");
                            //                            gotoPager(PostMyLoveCarFragment.class, null);
                            return true;
                        } else if (result_url.startsWith("pay?info=")) {

                            String u = url.split("info=")[1];
                            try {
                                String decode_str = URLDecoder.decode(u, "UTF-8");
                                JSONObject jsonObject = new JSONObject(decode_str);
                                String type = jsonObject.getString("type");
                                if (type.equals("Wxpay")) {
                                    //微信支付
                                    PayReq request = new PayReq();
                                    request.appId = jsonObject.getString("appid");
                                    request.partnerId = jsonObject.getString("partnerid");
                                    request.prepayId = jsonObject.getString("prepayid");
                                    request.packageValue = jsonObject.getString("package");
                                    request.nonceStr = jsonObject.getString("noncestr");
                                    request.timeStamp = String.valueOf(jsonObject.getLong("timestamp"));
                                    request.sign = jsonObject.getString("sign");
                                    api.sendReq(request);
                                } else {
                                    ///支付宝支付
                                    final String orderInfo = jsonObject.getString("orderstr");
                                    // 订单信息
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

                    @Override
                    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                        super.onPageStarted(webView, s, bitmap);
                        //                        webView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView webView, String s) {
                        super.onPageFinished(webView, s);
                        //                        webView.setVisibility(View.VISIBLE);
                    }
                });

                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {

                    }
                });

                if (Build.VERSION.SDK_INT < 19) {
                    if (Build.VERSION.SDK_INT > 8) {
                        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    }
                }

                webView.loadUrl(bannerBean.getAppUrl());
            } else {
                //进入话题
                ToastUtils.showShort("话题");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                showSharePlatformPopWindow();//分享
                break;
            default:
                break;
        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(getActivity(), new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onSinaWeiboClicked() {
                showShare(mContext, "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                showShare(mContext, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                showShare(mContext, "WechatMoments", true);
            }

            @Override
            public void onCreatQr() {

            }

            @Override
            public void onCancelBtnClicked() {

            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    private void showShare(Context context, String platformToShare, boolean showContentEdit) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        //        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();

        oks.setTitle(getString(R.string.vr_see_car));
        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
            oks.setText(getString(R.string.bibicar_vr) + "\n" + bannerBean.getAppUrl());
        } else {
            oks.setText(getString(R.string.bibicar_vr));
            oks.setImageUrl(bannerBean.getImgUrl());
            oks.setUrl(bannerBean.getAppUrl());
        }

        // 启动分享
        oks.show(context);
    }
}
