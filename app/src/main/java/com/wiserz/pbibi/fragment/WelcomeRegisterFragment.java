package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/26 10:49.
 * QQ : 971060378
 * Used as : 欢迎注册的页面
 */
public class WelcomeRegisterFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welcome_register;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("注册");

        view.findViewById(R.id.btn_welcome_private_register).setOnClickListener(this);//个人注册按钮
        view.findViewById(R.id.btn_welcome_company_register).setOnClickListener(this);//企业注册按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_welcome_private_register:
                gotoPager(RegisterFragment.class, null);
                break;
            case R.id.btn_welcome_company_register:
                gotoPager(CompanyRegisterFragment.class, null);
                break;
            default:
                break;
        }
    }
}
