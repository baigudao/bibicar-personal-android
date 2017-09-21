package com.wiserz.pbibi.fragment;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;


/**
 * Created by jackie on 2017/9/21 15:47.
 * QQ : 971060378
 * Used as : 抽奖的页面
 */
public class LotteryDrawFragment extends BaseFragment {

    private String appUrl;
    private WebView tencent_web_view;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lottery_draw;
    }

    @Override
    protected void initView(View view) {
        appUrl = getArguments().getString(Constant.APP_URL);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("抽奖活动");

        tencent_web_view = (WebView) view.findViewById(R.id.tencent_web_view);
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
        if (EmptyUtils.isNotEmpty(appUrl)) {
            String real_app_url = appUrl + "?ident=" + SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER)
                    + "&session=" + SPUtils.getInstance().getString(Constant.SESSION_ID);
            WebSettings webSettings = tencent_web_view.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
            String appCachePath = BaseApplication.getAppContext().getCacheDir().getAbsolutePath();
            webSettings.setAppCachePath(appCachePath);
            webSettings.setAllowFileAccess(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT > 8) {
                    webSettings.setPluginState(WebSettings.PluginState.ON);
                }
            }

            tencent_web_view.loadUrl(real_app_url);

            tencent_web_view.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                    return super.shouldOverrideUrlLoading(webView, s);
                }
            });

            tencent_web_view.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView webView, int i) {
                    super.onProgressChanged(webView, i);
                }
            });
        }
    }
}
