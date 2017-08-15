package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/15 10:43.
 * QQ : 971060378
 * Used as : 查询出险记录的页面
 */
public class CheckSumFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_sum;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查出险");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("查询历史");
        btn_register.setTextSize(15);
        btn_register.setTextColor(getResources().getColor(R.color.second_text_color));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("查询历史");
                break;
            default:
                break;
        }
    }
}
