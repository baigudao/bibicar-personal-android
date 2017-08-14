package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 17:00.
 * QQ : 971060378
 * Used as : 话题成员的页面
 */
public class TopicMemberFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_member;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.tv_title)).setText("话题成员");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }
}
