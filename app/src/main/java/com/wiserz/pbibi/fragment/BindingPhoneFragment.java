package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.MainActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
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

    private EditText et_account_login;
    private EditText et_verfication_code_login;
    private TextView tv_get_verfication_code;
    private Button btn_bind;

    private LoginBean loginBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_binding_phone;
    }

    @Override
    protected void initView(View view) {
        if(DataManager.getInstance().getObject()==null){
            goBack();
        }
        loginBean= (LoginBean) DataManager.getInstance().getObject();
        DataManager.getInstance().setObject(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("绑定手机号");

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        tv_get_verfication_code = (TextView) view.findViewById(R.id.tv_get_verfication_code);
        et_account_login = (EditText) view.findViewById(R.id.et_account_login);
        et_account_login.addTextChangedListener(new TextChangedListener());
        et_verfication_code_login = (EditText) view.findViewById(R.id.et_verfication_code_login);
        et_verfication_code_login.addTextChangedListener(new TextChangedListener());
        btn_bind = (Button) view.findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(this);

        view.findViewById(R.id.btn_user_protocol).setOnClickListener(this);
        view.findViewById(R.id.tv_get_verfication_code).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.btn_bind:
                final String phone = getAccount();
                final String verCode = getVerficationCode();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verCode)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_all_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttpUtils.post()
                        .url(Constant.getBindingPhoneUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, loginBean.getSession_id())
                        .addParams(Constant.MOBILE, phone)
                        .addParams(Constant.CODE, verCode)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("aaaaaaaaa", "response: " + response);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    int status = jsonObject.optInt("status");
                                    JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                    if (status == 1) {
                                        SPUtils.getInstance().put(Constant.SESSION_ID, loginBean.getSession_id());
                                        SPUtils.getInstance().put(Constant.ACCOUNT, loginBean.getUser_info().getMobile());
                                        SPUtils.getInstance().put(Constant.CHAT_TOKEN, loginBean.getUser_info().getChat_token());
                                        SPUtils.getInstance().put(Constant.USER_ID, loginBean.getUser_info().getUser_id());
                                        DataManager.getInstance().setUserInfo(loginBean.getUser_info());

                                        if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(Constant.SESSION_ID))) {
                                            SPUtils.getInstance().put(Constant.IS_USER_LOGIN, true);
                                        }
                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                        connect(SPUtils.getInstance().getString(Constant.CHAT_TOKEN));//建立与融云服务器的连接
                                        ToastUtils.showShort(R.string.login_successful);
                                    } else {
                                        String code = jsonObject.optString("code");
                                        String msg = jsonObjectData.optString("msg");
                                        ToastUtils.showShort("" + msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case R.id.btn_user_protocol:
                gotoPager(UserProtocolFragment.class, null);
                break;
            case R.id.tv_get_verfication_code:
                //   gotoPager(ForgetPasswordFragment.class, null);
                getCode();
                break;
            default:
                break;
        }
    }

    private void getCode() {
        String mobile = getAccount();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getActivity(), getString(R.string.please_input_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            Toast.makeText(getActivity(), getString(R.string.please_input_correct_phone), Toast.LENGTH_SHORT).show();
            return;
        }
        mTotalTime = 60;
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTask, 1000, 1000);
        tv_get_verfication_code.setEnabled(false);
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
                        Log.e("aaaaaaaaa", "response: " + response);
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
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getView()==null){

                        }
                        --mTotalTime;
                        tv_get_verfication_code.setText(String.valueOf(mTotalTime));
                        if (mTotalTime <= 0) {
                            tv_get_verfication_code.setEnabled(true);
                            mTimer.cancel();
                            mTimer = null;
                            tv_get_verfication_code.setText(getString(R.string.get_code));
                            tv_get_verfication_code.setBackgroundDrawable(null);
                            tv_get_verfication_code.setTextColor(getResources().getColor(R.color.btn_no_enable_color));
                            boolean user = et_account_login.getText().length() == 11;
                            tv_get_verfication_code.setEnabled(user);
                            tv_get_verfication_code.setTextColor(getResources().getColor(user ? R.color.main_color : R.color.btn_no_enable_color));
                        } else {
                            tv_get_verfication_code.setTextColor(Color.WHITE);
                            tv_get_verfication_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
                        }
                    }
                });
            }
        };
    }

    public String getAccount() {
        return et_account_login.getText().toString().trim();
    }

    public String getVerficationCode() {
        return et_verfication_code_login.getText().toString().trim();
    }

    private class TextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean user = et_account_login.getText().length() == 11;
            if (mTotalTime <= 0) {
                tv_get_verfication_code.setEnabled(user);
                tv_get_verfication_code.setTextColor(getResources().getColor(user ? R.color.main_color : R.color.btn_no_enable_color));
            }
            boolean pwd = et_verfication_code_login.getText().length() == 4;
            if (user & pwd) {
                btn_bind.setEnabled(true);
            } else {
                btn_bind.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 RongIM.init() 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        if (getActivity().getApplicationInfo().packageName.equals(BaseApplication.getCurProcessName(getActivity().getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
    }
}
