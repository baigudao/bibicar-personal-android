package com.wiserz.pbibi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 注册登录界面
 */
public class RegisterAndLoginActivity extends Activity implements View.OnClickListener {

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
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(final String token) {

    }
}
