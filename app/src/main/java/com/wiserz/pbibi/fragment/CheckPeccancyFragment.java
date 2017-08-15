package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/15 9:42.
 * QQ : 971060378
 * Used as : 查违章的页面
 */
public class CheckPeccancyFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_peccancy;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查违章");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setText("查询历史");
        btn_register.setTextSize(15);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setTextColor(getResources().getColor(R.color.second_text_color));
        btn_register.setOnClickListener(this);

        view.findViewById(R.id.tv_city).setOnClickListener(this);
        view.findViewById(R.id.tv_city_short).setOnClickListener(this);
        view.findViewById(R.id.btn_start_check).setOnClickListener(this);
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
            case R.id.tv_city:
                ToastUtils.showShort("选择城市");
                break;
            case R.id.tv_city_short:
                ToastUtils.showShort("选择省份简称");
                break;
            case R.id.btn_start_check:
                ToastUtils.showShort("立即查询");
                break;
            default:
                break;
        }
    }
}
