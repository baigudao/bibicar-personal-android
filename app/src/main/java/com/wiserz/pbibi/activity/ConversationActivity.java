package com.wiserz.pbibi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by jackie on 2017/8/10 0:23.
 * QQ : 971060378
 * Used as : 会话界面
 */

public class ConversationActivity extends BaseActivity implements View.OnClickListener {

    private String mTargetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        connect(SPUtils.getInstance().getString(Constant.CHAT_TOKEN));//建立与融云服务器的连接
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_register).setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            mTargetId = intent.getData().getQueryParameter("targetId");
            String title = intent.getData().getQueryParameter("title");
            ((TextView) findViewById(R.id.tv_title)).setText(title == null ? "" : title);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
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
