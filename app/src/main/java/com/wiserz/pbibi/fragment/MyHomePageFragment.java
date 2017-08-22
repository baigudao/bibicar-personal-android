package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/21 16:39.
 * QQ : 971060378
 * Used as : 我的主页的页面
 */
public class MyHomePageFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_home_page;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人主页");
        view.findViewById(R.id.tv_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tv_edit:
                gotoPager(EditUserProfileFragment.class, null);
                break;
            default:
                break;
        }
    }
}
