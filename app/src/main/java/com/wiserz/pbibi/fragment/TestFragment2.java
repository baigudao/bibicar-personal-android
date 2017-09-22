package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/9/22 15:23.
 * QQ : 971060378
 * Used as : xxx
 */
public class TestFragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment_2, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //显示
            LogUtils.e("显示TestFragment2 onHiddenChanged");
        } else {
            //隐藏
            LogUtils.e("隐藏TestFragment2 onHiddenChanged");
        }
    }
}
