package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 17:39.
 * QQ : 971060378
 * Used as : 发布动态的页面
 */
public class PublishDynamicStateFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_publish_dynamic_state;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("发布动态");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("发布");
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("发布");
                break;
            default:
                break;
        }
    }
}
