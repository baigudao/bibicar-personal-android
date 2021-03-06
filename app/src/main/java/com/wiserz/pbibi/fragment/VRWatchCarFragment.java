package com.wiserz.pbibi.fragment;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.BannerBean;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.NumberProgressBar;

/**
 * Created by jackie on 2017/9/21 16:37.
 * QQ : 971060378
 * Used as : 备用
 */
public class VRWatchCarFragment extends BaseFragment {

    private NumberProgressBar numberProgressBar;

    private WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vr_watch_car;
    }

    @Override
    protected void initView(View view) {
        webView = (WebView) view.findViewById(R.id.tencent_web_view);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("VR看车");

        numberProgressBar = (NumberProgressBar) view.findViewById(R.id.numberProgressBar);
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
        BannerBean bannerBean = (BannerBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        if (EmptyUtils.isNotEmpty(bannerBean)) {
            if (bannerBean.getType().equals("0")) {
                final String vrUrl = bannerBean.getAppUrl();
                if (EmptyUtils.isNotEmpty(vrUrl)) {

                    WebSettings ws = webView.getSettings();

                    ws.setJavaScriptEnabled(true);
                    ws.setBuiltInZoomControls(false);// 隐藏缩放按钮
                    ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
                    ws.setUseWideViewPort(true);// 可任意比例缩放
                    ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
                    ws.setSavePassword(true);
                    ws.setSaveFormData(true);// 保存表单数据
                    ws.setGeolocationEnabled(true);// 启用地理定位
                    ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
                    ws.setDomStorageEnabled(true);

                    if (Build.VERSION.SDK_INT < 19) {
                        if (Build.VERSION.SDK_INT > 8) {
                            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                        }
                    }

                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                            webView.loadUrl(vrUrl);
                            return true;
                        }
                    });

                    webView.loadUrl(vrUrl);

                    webView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView webView, int i) {
                            super.onProgressChanged(webView, i);
                            if (i == 100) {
                                numberProgressBar.setVisibility(View.GONE);
                                return;
                            }
                            numberProgressBar.incrementProgressBy(i);
                        }
                    });
                }
            }
        }
    }
}
