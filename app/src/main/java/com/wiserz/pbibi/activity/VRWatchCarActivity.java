package com.wiserz.pbibi.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.BannerBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;

/**
 * Created by jackie on 2017/8/29 14:02.
 * QQ : 971060378
 * Used as : VR看车的界面
 */
public class VRWatchCarActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.hideStatusBar(this);//隐藏状态栏
        setContentView(R.layout.activity_vr_watch_car);
        webView = (WebView) findViewById(R.id.tencent_web_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BannerBean bannerBean = (BannerBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        if (EmptyUtils.isNotEmpty(bannerBean)) {
            if (bannerBean.getType().equals("0")) {
                String vrUrl = bannerBean.getAppUrl() + "?ident=" + SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER) + "&session=" + SPUtils.getInstance().getString(Constant.SESSION_ID);
                LogUtils.e(vrUrl);
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
                    webView.loadUrl(vrUrl);
                }
            }
        }
    }
}
