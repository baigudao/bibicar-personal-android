package com.wiserz.pbibi.fragment;

import android.view.View;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/21 17:38.
 * QQ : 971060378
 * Used as : 总资产的页面
 */
public class TotalPropertyFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_total_property;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.tv_title).setVisibility(View.GONE);
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
