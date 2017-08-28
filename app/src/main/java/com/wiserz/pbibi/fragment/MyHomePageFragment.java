package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/8/21 16:39.
 * QQ : 971060378
 * Used as : 我的主页的页面
 */
public class MyHomePageFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private static final int MY_CAR_REPERTORY_DATA_TYPE = 24;

    private static final int MY_STATE_DATA_TYPE = 25;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_home_page;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人主页");
        view.findViewById(R.id.tv_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tv_edit:
                gotoPager(EditUserProfileFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        if (getView() != null) {
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                strings.add("hehe");
            }
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, MY_CAR_REPERTORY_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);


            RecyclerView state_recyclerView = (RecyclerView) getView().findViewById(R.id.state_recyclerView);
            BaseRecyclerViewAdapter baseRecyclerViewAdapter1 = new BaseRecyclerViewAdapter(mContext, strings, MY_STATE_DATA_TYPE);
            state_recyclerView.setAdapter(baseRecyclerViewAdapter1);
            state_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {

    }
}
