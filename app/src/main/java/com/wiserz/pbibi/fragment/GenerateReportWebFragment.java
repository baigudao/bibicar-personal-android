package com.wiserz.pbibi.fragment;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;

import java.util.Calendar;

/**
 * Created by skylar on 2017/12/28 9:13.
 * Used as : 报价单生成页面
 */
public class GenerateReportWebFragment extends BaseFragment {

    private String mCarId;
    private String url = "http://192.168.1.28:4000/views/list/offer.html?ident=df66ea9ed5db39f90238ea46ab137fa7&session=session5a3a603c386cb&id=5a4073a13d8e4";
    private String choose_time;
    private int mYear,mMonth,mDay;

    private BridgeWebView mWebView;
    private RelativeLayout rl_date;

    private CallBackFunction mReportTimeFunction;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_generate_report_web;
    }

    @Override
    protected void initView(View view) {
        mCarId = getArguments().getString(Constant.CAR_ID);
        mWebView = (BridgeWebView) view.findViewById(R.id.webView);
        rl_date = (RelativeLayout) view.findViewById(R.id.rl_date);


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                ToastUtils.showShort(message);
                return super.onJsAlert(view, url, message, result);
            }
        });


        //选择报价时间
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth= Calendar.getInstance().get(Calendar.MONTH);
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        ((DatePicker) view.findViewById(R.id.datePicker)).init(mYear,
                mMonth,
                mDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        mYear=year;
                        mMonth=month;
                        mDay=day;
                    }
                });
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_date.setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.tvSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month=mMonth+1;
                choose_time = mYear + "-" + (month < 10 ? (0 + "" + String.valueOf(month)) : String.valueOf(month)) + "-" + (mDay < 10 ? (0 + "" + String.valueOf(mDay)) : String.valueOf(mDay));
                rl_date.setVisibility(View.GONE);

                callJsMethod("chooseDate",CommonUtil.dateToStamp(choose_time));
            }
        });

        initJsMethods();
    }

    @Override
    public void onClick(View view) {

    }

    private void initJsMethods() {
        mWebView.registerHandler("chooseDate", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                rl_date.bringToFront();
                rl_date.setVisibility(View.VISIBLE);
//                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }
        });

    }

    private void callJsMethod(String functionInJs,String sendData){
        mWebView.callHandler(functionInJs, sendData, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                Log.i("TESTLOG", "reponse data from js " + data);
            }

        });
    }


}
