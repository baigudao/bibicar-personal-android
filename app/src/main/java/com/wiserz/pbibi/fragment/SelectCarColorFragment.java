package com.wiserz.pbibi.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/9/11 17:35.
 * QQ : 971060378
 * Used as : 选择车辆颜色的页面
 */
public class SelectCarColorFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;

    private static final int SELECT_CAR_COLOR_DATA_TYPE = 33;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_car_color;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("选择颜色");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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

    @Override
    protected void initData() {
        super.initData();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            strings.add("color" + i);
        }
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, SELECT_CAR_COLOR_DATA_TYPE);
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("String")) {
            DataManager.getInstance().setData7(position);
            goBack();
        }
    }
}
