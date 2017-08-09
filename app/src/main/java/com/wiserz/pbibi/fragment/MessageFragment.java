package com.wiserz.pbibi.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jackie on 2017/8/9 18:01.
 * QQ : 971060378
 * Used as : 消息页面
 */
public class MessageFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected View initView(View view) {
        TextView textView = new TextView(mContext);
        textView.setText("消息");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
