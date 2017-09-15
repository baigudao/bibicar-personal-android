package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/16 21:25.
 * QQ : 971060378
 * Used as : xxx
 */

public class TestFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.car_center_price;//car_center_sort排序;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("测试页面");
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                LogUtils.e(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtils.e("开始");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtils.e("结束");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //                goBack();
                //                gotoPager(PayFailFragment.class, null);//支付失败
                //                gotoPager(PaySuccessFragment.class, null);//支付成功
                gotoPager(LoanPlanFragment.class, null);//贷款方案
                break;
            default:
                break;
        }
    }
}
