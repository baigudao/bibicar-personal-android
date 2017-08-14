package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 22:55.
 * QQ : 971060378
 * Used as : 具体参数的页面
 */

public class ConcreteParameterFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_concrete_parameter;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("具体参数");
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
