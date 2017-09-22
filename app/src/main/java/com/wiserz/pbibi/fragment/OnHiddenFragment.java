package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/9/22 15:06.
 * QQ : 971060378
 * Used as : xxx
 */
public class OnHiddenFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_on_hidden;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("OnHiddenFragment");
        view.findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn:
                gotoPager(OnHiddenFragment2.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("OnHiddenFragment onHiddenChanged");
    }
}
