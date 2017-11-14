package com.wiserz.pbibi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.fragment.CompanyRegisterFragment;
import com.wiserz.pbibi.fragment.ForgetPasswordFragment;
import com.wiserz.pbibi.fragment.OauthRegisterFragment;
import com.wiserz.pbibi.fragment.UserProtocolFragment;
import com.wiserz.pbibi.fragment.WelcomeRegisterFragment;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.login.LoginApi;
import cn.sharesdk.login.OnLoginListener;
import cn.sharesdk.login.UserInfo;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;

/**
 * 注册登录界面
 */
public class RegisterAndLoginActivity extends BaseActivity implements View.OnClickListener {

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    private EditText et_account_login;
    private EditText et_verfication_code_login;
    private TextView tv_get_verfication_code;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        findViewById(R.id.iv_back).setOnClickListener(this);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register.setVisibility(View.VISIBLE);
        SPUtils.getInstance().put(Constant.IS_USER_LOGIN, false);
        tv_get_verfication_code=(TextView) findViewById(R.id.tv_get_verfication_code);
        et_account_login = (EditText) findViewById(R.id.et_account_login);
        et_account_login.addTextChangedListener(new TextChangedListener());
        et_verfication_code_login = (EditText) findViewById(R.id.et_verfication_code_login);
        et_verfication_code_login.addTextChangedListener(new TextChangedListener());
        btn_login = (Button) findViewById(R.id.btn_login);//登录按钮
        btn_login.setOnClickListener(this);

        findViewById(R.id.btn_user_protocol).setOnClickListener(this);
        findViewById(R.id.tv_get_verfication_code).setOnClickListener(this);

        findViewById(R.id.image_btn_weixin_login).setOnClickListener(this);
        findViewById(R.id.image_btn_weibo_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_register:
                gotoPager(CompanyRegisterFragment.class, null);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_user_protocol:
                gotoPager(UserProtocolFragment.class, null);
                break;
            case R.id.tv_get_verfication_code:
             //   gotoPager(ForgetPasswordFragment.class, null);
                getCode();
                break;
            case R.id.image_btn_weixin_login:
                login("Wechat");
                break;
            case R.id.image_btn_weibo_login:
                login("SinaWeibo");
                break;
            default:
                break;
        }
    }

    private void getCode(){
        String mobile = getAccount();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, getString(R.string.please_input_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            Toast.makeText(this, getString(R.string.please_input_correct_phone), Toast.LENGTH_SHORT).show();
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
                        Log.e("aaaaaaaaa","response: "+response);
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
    }

    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --mTotalTime;
                        tv_get_verfication_code.setText(String.valueOf(mTotalTime));
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            mTimer=null;
                            tv_get_verfication_code.setText(getString(R.string.get_code));
                            tv_get_verfication_code.setBackgroundDrawable(null);
                            tv_get_verfication_code.setTextColor(getResources().getColor(R.color.btn_no_enable_color));
                        } else {
                            tv_get_verfication_code.setTextColor(Color.WHITE);
                            tv_get_verfication_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
                        }
                    }
                });
            }
        };
    }

    private void login() {
        final String account = getAccount();
        final String verficationCode = getVerficationCode();
        OkHttpUtils.post()
                .url(Constant.getUserCodeLoginUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.MOBILE, account)
                .addParams(Constant.CODE, verficationCode)
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
                                SPUtils.getInstance().put(Constant.PASSWORD, verficationCode);
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

    public void onDestroy(){
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
        }
        mTimer=null;
    }

    /**
     * 演示执行第三方登录/注册的方法
     * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
     *
     * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
     */
    private void login(final String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
            public boolean onLogin(Platform platform, HashMap<String, Object> res) {
                PlatformDb platDB = platform.getDb();//获取平台数据DB
                //通过DB获取各种数据
                platDB.getToken();
                platDB.getUserGender();
                platDB.getUserIcon();
                platDB.getUserId();
                platDB.getUserName();

                UserInfo userInfo = new UserInfo();
                userInfo.setUserIcon(platDB.getUserIcon());
                userInfo.setUserId(platDB.getUserId());
                userInfo.setUserName(platDB.getUserName());
                userInfo.setUserGender(platDB.getUserGender());
                if (platform.getName().equalsIgnoreCase("Wechat")) {
                    userInfo.setUserId((String) res.get("unionid"));
                }
                userInfo.setPlatform(platform);
                oauthLogin(userInfo);
                return true;
            }
        });
        api.login(this);
    }

    private void oauthLogin(final UserInfo userInfo) {
        String weiboId = "";
        String wxId = "";
        if (userInfo.getPlatform().getName().equalsIgnoreCase("SinaWeibo")) {
            weiboId = userInfo.getUserId();
        } else {
            wxId = userInfo.getUserId();
        }
        OkHttpUtils.post()
                .url(Constant.getOauthLoginUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.AVATAR, userInfo.getUserIcon())
                .addParams(Constant.NICKNAME, userInfo.getUserName())
                .addParams("weibo_open_id", weiboId)
                .addParams("wx_open_id", wxId)
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
                                SPUtils.getInstance().put(Constant.ACCOUNT, loginBean.getUser_info().getMobile());
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
                                int code = jsonObject.optInt("code");
                                if (code == 51008) {
                                    DataManager.getInstance().setData1(userInfo);
                                    gotoPager(OauthRegisterFragment.class, null);
                                    return;
                                }
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
            boolean user = et_account_login.getText().length() > 0;
            boolean pwd = et_verfication_code_login.getText().length() > 0;
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
