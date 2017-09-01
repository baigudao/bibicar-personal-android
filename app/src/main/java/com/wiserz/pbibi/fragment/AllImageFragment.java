package com.wiserz.pbibi.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/8/30 17:19.
 * QQ : 971060378
 * Used as : 全部图片的页面
 */
public class AllImageFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private CarInfoBean.FilesBean filesBean;

    private TextView tv_car_surface_num;
    private RecyclerView recyclerView_car_surface;

    private TextView tv_car_inside;
    private RecyclerView recyclerView_car_inside;

    private TextView tv_car_structure;
    private RecyclerView recyclerView_car_structure;

    private TextView tv_car_more_detail;
    private RecyclerView recyclerView_car_more_detail;

    private static final int CAR_SURFACE_DATA_TYPE = 17;

    private static final int CAR_INSIDE_DATA_TYPE = 29;

    private static final int CAR_STRUCTURE_DATA_TYPE = 30;

    private static final int CAR_MORE_DETAIL_DATA_TYPE = 31;

    private ArrayList<String> stringImageUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_image;
    }

    @Override
    protected void initView(View view) {
        filesBean = (CarInfoBean.FilesBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("全部图片");

        tv_car_surface_num = (TextView) view.findViewById(R.id.tv_car_surface_num);
        recyclerView_car_surface = (RecyclerView) view.findViewById(R.id.recyclerView_car_surface);

        tv_car_inside = (TextView) view.findViewById(R.id.tv_car_inside);
        recyclerView_car_inside = (RecyclerView) view.findViewById(R.id.recyclerView_car_inside);

        tv_car_structure = (TextView) view.findViewById(R.id.tv_car_structure);
        recyclerView_car_structure = (RecyclerView) view.findViewById(R.id.recyclerView_car_structure);

        tv_car_more_detail = (TextView) view.findViewById(R.id.tv_car_more_detail);
        recyclerView_car_more_detail = (RecyclerView) view.findViewById(R.id.recyclerView_car_more_detail);
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
        if (EmptyUtils.isNotEmpty(filesBean)) {
            showData();
        }
    }

    private void showData() {
        stringImageUrl = new ArrayList<>();
        ArrayList<CarInfoBean.FilesBean.Type1Bean> type1BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type1Bean>) filesBean.getType1();
        if (EmptyUtils.isNotEmpty(type1BeanArrayList) && type1BeanArrayList.size() != 0) {
            tv_car_surface_num.setVisibility(View.VISIBLE);
            recyclerView_car_surface.setVisibility(View.VISIBLE);
            tv_car_surface_num.setText(" (" + type1BeanArrayList.size() + ")");

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, type1BeanArrayList, CAR_SURFACE_DATA_TYPE);
            recyclerView_car_surface.setAdapter(baseRecyclerViewAdapter);
            recyclerView_car_surface.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);

            for (int i = 0; i < type1BeanArrayList.size(); i++) {
                stringImageUrl.add(type1BeanArrayList.get(i).getFile_url());
            }
        } else {
            tv_car_surface_num.setVisibility(View.GONE);
            recyclerView_car_surface.setVisibility(View.GONE);
        }

        ArrayList<CarInfoBean.FilesBean.Type2Bean> type2BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type2Bean>) filesBean.getType2();
        if (EmptyUtils.isNotEmpty(type2BeanArrayList) && type2BeanArrayList.size() != 0) {
            tv_car_inside.setVisibility(View.VISIBLE);
            recyclerView_car_inside.setVisibility(View.VISIBLE);
            tv_car_inside.setText(" (" + type2BeanArrayList.size() + ")");

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, type2BeanArrayList, CAR_INSIDE_DATA_TYPE);
            recyclerView_car_inside.setAdapter(baseRecyclerViewAdapter);
            recyclerView_car_inside.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);

            for (int i = 0; i < type2BeanArrayList.size(); i++) {
                stringImageUrl.add(type2BeanArrayList.get(i).getFile_url());
            }
        } else {
            tv_car_inside.setVisibility(View.GONE);
            recyclerView_car_inside.setVisibility(View.GONE);
        }

        ArrayList<CarInfoBean.FilesBean.Type3Bean> type3BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type3Bean>) filesBean.getType3();
        if (EmptyUtils.isNotEmpty(type3BeanArrayList) && type3BeanArrayList.size() != 0) {
            tv_car_structure.setVisibility(View.VISIBLE);
            recyclerView_car_structure.setVisibility(View.VISIBLE);
            tv_car_structure.setText(" (" + type3BeanArrayList.size() + ")");

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, type3BeanArrayList, CAR_STRUCTURE_DATA_TYPE);
            recyclerView_car_structure.setAdapter(baseRecyclerViewAdapter);
            recyclerView_car_structure.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);

            for (int i = 0; i < type3BeanArrayList.size(); i++) {
                stringImageUrl.add(type3BeanArrayList.get(i).getFile_url());
            }
        } else {
            tv_car_structure.setVisibility(View.GONE);
            recyclerView_car_structure.setVisibility(View.GONE);
        }

        ArrayList<CarInfoBean.FilesBean.Type4Bean> type4BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type4Bean>) filesBean.getType4();
        if (EmptyUtils.isNotEmpty(type4BeanArrayList) && type4BeanArrayList.size() != 0) {
            tv_car_more_detail.setVisibility(View.VISIBLE);
            recyclerView_car_more_detail.setVisibility(View.VISIBLE);
            tv_car_more_detail.setText(" (" + type4BeanArrayList.size() + ")");

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, type4BeanArrayList, CAR_MORE_DETAIL_DATA_TYPE);
            recyclerView_car_more_detail.setAdapter(baseRecyclerViewAdapter);
            recyclerView_car_more_detail.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);

            for (int i = 0; i < type4BeanArrayList.size(); i++) {
                stringImageUrl.add(type4BeanArrayList.get(i).getFile_url());
            }
        } else {
            tv_car_more_detail.setVisibility(View.GONE);
            recyclerView_car_more_detail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("Type1Bean")) {
            CarInfoBean.FilesBean.Type1Bean type1Bean = (CarInfoBean.FilesBean.Type1Bean) data;
            if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
                DataManager.getInstance().setData1(stringImageUrl);
                gotoPager(ShowAllImageFragment.class, null);
            }
        }
        if (data.getClass().getSimpleName().equals("Type2Bean")) {
            CarInfoBean.FilesBean.Type2Bean type2Bean = (CarInfoBean.FilesBean.Type2Bean) data;
            if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
                DataManager.getInstance().setData1(stringImageUrl);
                gotoPager(ShowAllImageFragment.class, null);
            }
        }
        if (data.getClass().getSimpleName().equals("Type3Bean")) {
            CarInfoBean.FilesBean.Type3Bean type3Bean = (CarInfoBean.FilesBean.Type3Bean) data;
            if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
                DataManager.getInstance().setData1(stringImageUrl);
                gotoPager(ShowAllImageFragment.class, null);
            }
        }
        if (data.getClass().getSimpleName().equals("Type4Bean")) {
            CarInfoBean.FilesBean.Type4Bean type4Bean = (CarInfoBean.FilesBean.Type4Bean) data;
            if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
                DataManager.getInstance().setData1(stringImageUrl);
                gotoPager(ShowAllImageFragment.class, null);
            }
        }
    }
}
