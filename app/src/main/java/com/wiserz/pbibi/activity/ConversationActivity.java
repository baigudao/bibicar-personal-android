package com.wiserz.pbibi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

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
}
