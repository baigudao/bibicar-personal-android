package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/9/15 15:37.
 * QQ : 971060378
 * Used as : 支付失败的页面
 */
public class PayFailFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay_fail;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("支付失败");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }
}
