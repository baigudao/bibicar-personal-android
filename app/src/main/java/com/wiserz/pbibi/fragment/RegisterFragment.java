package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.EmptyActivity;
import com.wiserz.pbibi.activity.MainActivity;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/26 11:23.
 * QQ : 971060378
 * Used as : 个人注册的页面
 */
public class RegisterFragment extends BaseFragment {

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    private Button get_verification_code;
    private EditText nickname_register;
    private EditText phone_register;
    private EditText input_verification_code_register;
    private EditText input_password_register;
    private Button btn_private_register;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    private String getNickName() {
        return nickname_register.getText().toString().trim();
    }

    private String getMobile() {
        return phone_register.getText().toString().trim();
    }

    private String getVerificationCode() {
        return input_verification_code_register.getText().toString().trim();
    }

    private String getNewPassword() {
        return input_password_register.getText().toString().trim();
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);//初始化返回图标
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人注册");

        nickname_register = (EditText) view.findViewById(R.id.nickname_register);//初始化昵称
        nickname_register.addTextChangedListener(new MyTextChangedListener());
        phone_register = (EditText) view.findViewById(R.id.phone_register);//初始化手机号
        phone_register.addTextChangedListener(new MyTextChangedListener());
        get_verification_code = (Button) view.findViewById(R.id.get_verification_code);//初始化验证码按钮
        get_verification_code.setOnClickListener(this);
        input_verification_code_register = (EditText) view.findViewById(R.id.input_verification_code_register);//初始化输入验证码
        input_verification_code_register.addTextChangedListener(new MyTextChangedListener());
        input_password_register = (EditText) view.findViewById(R.id.input_password_register);//初始化密码
        input_password_register.addTextChangedListener(new MyTextChangedListener());
        btn_private_register = (Button) view.findViewById(R.id.btn_private_register);//初始化注册按钮
        btn_private_register.setOnClickListener(this);
        view.findViewById(R.id.btn_user_protocol).setOnClickListener(this);//初始化用户协议按钮
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回图标
                goBack();
                break;
            case R.id.get_verification_code://获取验证码按钮
                if (mTotalTime > 0) {
                    return;
                }
                final String mobile = getMobile();
                if (!RegexUtils.isMobileExact(mobile)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_phone_or_name), Toast.LENGTH_SHORT).show();
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
            case R.id.btn_private_register://注册按钮
                final String name = getNickName();
                final String phone = getMobile();
                final String verCode = getVerificationCode();
                final String password = getNewPassword();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(verCode) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_all_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttpUtils.post()
                        .url(Constant.getUserRegisterUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.NICKNAME, name)
                        .addParams(Constant.MOBILE, phone)
                        .addParams(Constant.CODE, verCode)
                        .addParams(Constant.PASSWORD, EncryptUtils.encryptMD5ToString(password))
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
                                        Gson gson = new Gson();
                                        LoginBean loginBean = gson.fromJson(jsonObjectData.toString(), LoginBean.class);
                                        //存储个人相关信息
                                        SPUtils.getInstance().put(Constant.SESSION_ID, loginBean.getSession_id());
                                        SPUtils.getInstance().put(Constant.ACCOUNT, phone);
                                        SPUtils.getInstance().put(Constant.PASSWORD, password);
                                        SPUtils.getInstance().put(Constant.CHAT_TOKEN, loginBean.getUser_info().getChat_token());
                                        SPUtils.getInstance().put(Constant.USER_ID, loginBean.getUser_info().getUser_id());
                                        DataManager.getInstance().setUserInfo(loginBean.getUser_info());

                                        startActivity(new Intent(mContext, MainActivity.class));
                                        ((EmptyActivity) mContext).finish();
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
            case R.id.btn_user_protocol://用户协议
                gotoPager(UserProtocolFragment.class, null);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化时间任务器
     */
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
                        get_verification_code.setText(String.valueOf(mTotalTime));
                        get_verification_code.setTextSize(15);
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            get_verification_code.setText(getString(R.string.free_get));//免费获取
                            get_verification_code.setTextSize(12);
                            get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_selector));
                        } else {
                            get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
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
            boolean nickName = nickname_register.getText().length() > 0;
            boolean phone = phone_register.getText().length() > 0;
            boolean verificationCode = input_verification_code_register.getText().length() > 0;
            boolean password = input_password_register.getText().length() > 0;
            if (phone) {
                get_verification_code.setEnabled(true);
            } else {
                get_verification_code.setEnabled(false);
            }
            if (nickName & phone & verificationCode & password) {
                btn_private_register.setEnabled(true);
            } else {
                btn_private_register.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
