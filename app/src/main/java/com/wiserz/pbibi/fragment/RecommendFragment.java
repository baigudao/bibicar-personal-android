package com.wiserz.pbibi.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jackie on 2017/8/9 17:58.
 * QQ : 971060378
 * Used as : 推荐页面
 */
public class RecommendFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected View initView(View view) {
        TextView textView = new TextView(mContext);
        textView.setText("推荐");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
