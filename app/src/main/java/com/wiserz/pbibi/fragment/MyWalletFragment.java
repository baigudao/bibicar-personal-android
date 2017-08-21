package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 17:01.
 * QQ : 971060378
 * Used as : 我的钱包的页面
 */
public class MyWalletFragment extends BaseFragment {
    private Context mContext;
    private TextView tv_current_cash;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_wallet;
    }

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("我的钱包");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setOnClickListener(this);
        btn_register.setText("金额明细");
        btn_register.setTextSize(16);
        btn_register.setTextColor(getResources().getColor(R.color.main_text_color));

        tv_current_cash = (TextView) view.findViewById(R.id.tv_current_cash);
        tv_current_cash.setText("¥ 0");
        view.findViewById(R.id.btn_recharge).setOnClickListener(this);
        view.findViewById(R.id.btn_take_cash).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getBalanceUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                    int balance = jsonObjectData.optInt("balance");
                                    tv_current_cash.setText("¥ " + String.valueOf(balance));
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
     * 提现
     */
    private void takeCash() {
        OkHttpUtils.post()
                .url(Constant.getTakeCashUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {//{"status":1,"code":0,"data":{"msg":"Success"}}
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                if (getView() != null) {
                                    //卖家提现的数据
                                    String status_take_cash = jsonObjectData.optString("msg");
                                    if (status_take_cash.equals("Success")) {
                                        getDataFromNet();
                                        Toast.makeText(mContext, "提现成功!", Toast.LENGTH_SHORT).show();
                                    }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                gotoPager(CashDetailFragment.class, null);
                break;
            case R.id.btn_recharge:
                gotoPager(RechargeFragment.class, null);
                break;
            case R.id.btn_take_cash:
                takeCash();
                break;
            default:
                break;
        }
    }
}
