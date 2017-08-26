package com.wiserz.pbibi.fragment;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;

/**
 * Created by jackie on 2017/8/26 9:38.
 * QQ : 971060378
 * Used as : 用户协议的页面
 */
public class UserProtocolFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_protocol;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("用户协议");

        WebView wb_user_protocol = (WebView) view.findViewById(R.id.wb_user_protocol);
        String user_protocol_url = Constant.getUserProtocolUrl();
        if (!TextUtils.isEmpty(user_protocol_url)) {
            wb_user_protocol.getSettings().setJavaScriptEnabled(true);
            wb_user_protocol.getSettings().setSupportZoom(true);
            wb_user_protocol.getSettings().setUseWideViewPort(true);
            wb_user_protocol.getSettings().setLoadWithOverviewMode(true);
            wb_user_protocol.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT > 8) {
                    wb_user_protocol.getSettings().setPluginState(WebSettings.PluginState.ON);
                }
            }
            wb_user_protocol.loadUrl(user_protocol_url);
        }
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
}
