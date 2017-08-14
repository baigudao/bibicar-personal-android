package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 16:02.
 * QQ : 971060378
 * Used as : 创建话题页面
 */
public class CreateTopicFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_topic;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("创建话题");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("创建");
        btn_register.setOnClickListener(this);
        view.findViewById(R.id.ll_setting_background).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("创建");
                break;
            case R.id.ll_setting_background:
                ToastUtils.showShort("设置背景");
                break;
            default:
                break;
        }
    }
}
