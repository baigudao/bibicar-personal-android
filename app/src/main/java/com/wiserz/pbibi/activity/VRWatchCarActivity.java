package com.wiserz.pbibi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/29 14:02.
 * QQ : 971060378
 * Used as : VR看车的界面
 */
public class VRWatchCarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.hideStatusBar(this);//隐藏状态栏
        setContentView(R.layout.activity_vr_watch_car);
    }
}
