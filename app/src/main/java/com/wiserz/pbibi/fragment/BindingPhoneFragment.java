package com.wiserz.pbibi.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/28 21:40.
 * QQ : 971060378
 * Used as : 绑定手机号码
 */

public class BindingPhoneFragment extends BaseFragment {

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    private Button btn_get_verification_code;
    private EditText etPhone;
    private EditText get_verification_code;
    private Button tvNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_binding_phone;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("绑定手机");

        btn_get_verification_code = (Button) view.findViewById(R.id.btn_get_verification_code);//免费获取验证码
        btn_get_verification_code.setOnClickListener(this);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(new MyTextChangedListener());
        get_verification_code = (EditText) view.findViewById(R.id.get_verification_code);
        get_verification_code.addTextChangedListener(new MyTextChangedListener());
        tvNext = (Button) view.findViewById(R.id.tvNext);
        tvNext.setOnClickListener(this);
    }

    private String getMobile() {
        return etPhone.getText().toString().trim();
    }

    private String getVerificationCode() {
        return get_verification_code.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_get_verification_code:
                if (mTotalTime > 0) {
                    return;
                }
                final String mobile = getMobile();
                if (!RegexUtils.isMobileExact(mobile)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_phone_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                mTotalTime = 60;
                mTimer = new Timer();
                initTimerTask();
                mTimer.schedule(mTask, 1000, 1000);
                OkHttpUtils.post()
                        .url(Constant.getVerificationCodeUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.MOBILE, mobile)
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
                                        int code = jsonObjectData.optInt("code");
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
                break;
            case R.id.tvNext:
                final String phone = getMobile();
                final String verCode = getVerificationCode();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verCode)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_all_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttpUtils.post()
                        .url(Constant.getBindingPhoneUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                        .addParams(Constant.MOBILE, phone)
                        .addParams(Constant.CODE, verCode)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                goBack();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    int status = jsonObject.optInt("status");
                                    JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                    if (status == 1) {

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
                break;
            default:
                break;
        }
    }

    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (getView() == null) {
                    mTimer.cancel();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --mTotalTime;
                        if (getView() == null) {
                            mTimer.cancel();
                            return;
                        }
                        btn_get_verification_code.setText(String.valueOf(mTotalTime));
                        btn_get_verification_code.setTextSize(15);
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            btn_get_verification_code.setText(getString(R.string.free_get));
                            btn_get_verification_code.setTextSize(12);
                            btn_get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_selector));
                        } else {
                            btn_get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
                        }
                    }
                });
            }
        };
    }

    private class MyTextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean user = etPhone.getText().length() > 0;
            boolean verificationCode = get_verification_code.getText().length() > 0;
            if (user) {
                btn_get_verification_code.setEnabled(true);
            } else {
                btn_get_verification_code.setEnabled(false);
            }
            if (user & verificationCode) {
                tvNext.setEnabled(true);
            } else {
                tvNext.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}