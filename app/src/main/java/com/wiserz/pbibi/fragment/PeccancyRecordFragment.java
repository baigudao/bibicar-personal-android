package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.PeccancyRecordBean;
import com.wiserz.pbibi.util.DataManager;

import java.util.List;

/**
 * Created by jackie on 2017/9/14 11:58.
 * QQ : 971060378
 * Used as : 违章记录的页面
 */
public class PeccancyRecordFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<PeccancyRecordBean.ListsBean> listsBeanList;

    private static final int PECCANCY_RECORD_DATA_TYPE = 35;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_peccancy_record;
    }

    @Override
    protected void initView(View view) {
        PeccancyRecordBean peccancyRecordBean = (PeccancyRecordBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("违章记录");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        if (EmptyUtils.isNotEmpty(peccancyRecordBean)) {
            ((TextView) view.findViewById(R.id.tv_car_num)).setText(peccancyRecordBean.getHphm());
            listsBeanList = peccancyRecordBean.getLists();
        }
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
        if (EmptyUtils.isNotEmpty(listsBeanList) && listsBeanList.size() != 0 && getView() != null) {
            int size = listsBeanList.size();
            ((TextView) getView().findViewById(R.id.tv_record_num)).setText(String.valueOf(size));

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext,listsBeanList,PECCANCY_RECORD_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        }
    }
}
