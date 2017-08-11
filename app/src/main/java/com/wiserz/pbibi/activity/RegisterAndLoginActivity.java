package com.wiserz.pbibi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;

/**
 * 注册登录界面
 */
public class RegisterAndLoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_account_login;
    private EditText et_password_login;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        findViewById(R.id.btn_register).setOnClickListener(this);

        et_account_login = (EditText) findViewById(R.id.et_account_login);
        et_account_login.addTextChangedListener(new TextChangedListener());
        et_password_login = (EditText) findViewById(R.id.et_password_login);
        et_password_login.addTextChangedListener(new TextChangedListener());
        btn_login = (Button) findViewById(R.id.btn_login);//登录按钮
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                ToastUtils.showShort("注册");
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        final String account = getAccount();
        final String password = getPassword();
        OkHttpUtils.post()
                .url(Constant.getUserLoginUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.MOBILE, account)
                .addParams(Constant.PASSWORD, EncryptUtils.encryptMD5ToString(password))
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
                                Gson gson = new Gson();
                                LoginBean loginBean = gson.fromJson(jsonObjectData.toString(), LoginBean.class);
                                //存储个人相关信息
                                SPUtils.getInstance().put(Constant.SESSION_ID, loginBean.getSession_id());
                                SPUtils.getInstance().put(Constant.ACCOUNT, account);
                                SPUtils.getInstance().put(Constant.PASSWORD, password);
                                SPUtils.getInstance().put(Constant.CHAT_TOKEN, loginBean.getUser_info().getChat_token());
                                SPUtils.getInstance().put(Constant.USER_ID, loginBean.getUser_info().getUser_id());
                                DataManager.getInstance().setUserInfo(loginBean.getUser_info());

                                if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(Constant.SESSION_ID))) {
                                    SPUtils.getInstance().put(Constant.IS_USER_LOGIN, true);
                                }

                                startActivity(new Intent(RegisterAndLoginActivity.this, MainActivity.class));
                                RegisterAndLoginActivity.this.finish();
                                connect(SPUtils.getInstance().getString(Constant.CHAT_TOKEN));//建立与融云服务器的连接
                                ToastUtils.showShort(R.string.login_successful);
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
     * 得到账号
     *
     * @return
     */
    public String getAccount() {
        return et_account_login.getText().toString().trim();
    }

    /**
     * 得到密码
     *
     * @return
     */
    public String getPassword() {
        return et_password_login.getText().toString().trim();
    }

    private class TextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean user = et_account_login.getText().length() > 0;
            boolean pwd = et_password_login.getText().length() > 0;
            if (user & pwd) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
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

        if (getApplicationInfo().packageName.equals(BaseApplication.getCurProcessName(getApplicationContext()))) {

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
}
