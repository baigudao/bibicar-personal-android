package com.wiserz.pbibi.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 10:24.
 * QQ : 971060378
 * Used as : 关注的页面
 */
public class FollowFragment extends BaseFragment {

    private LinearLayout ll_recommend_follow;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void initView(View view) {
        ll_recommend_follow = (LinearLayout) view.findViewById(R.id.ll_recommend_follow);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                ll_recommend_follow.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
