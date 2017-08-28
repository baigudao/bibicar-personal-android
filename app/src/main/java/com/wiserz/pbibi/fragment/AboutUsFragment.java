package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;

/**
 * Created by jackie on 2017/8/28 22:18.
 * QQ : 971060378
 * Used as : 关于BibiCar的页面
 */

public class AboutUsFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("关于BiBiCar");
        view.findViewById(R.id.btn_register).setVisibility(View.INVISIBLE);

        String versionName = CommonUtil.getVersionName(mContext);
        ((TextView) view.findViewById(R.id.tvVersion)).setText(versionName);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            goBack();
        }
    }
}
