package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.SeekBar;

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

    }
}
