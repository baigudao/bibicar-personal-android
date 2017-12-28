package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.adapter.HeaderAndFooterWrapper;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.MorePopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/29 9:46.
 * QQ : 971060378
 * Used as : 我的售车的页面
 */
public class MySellingCarFragment extends BaseFragment implements OnLoadmoreListener, OnRefreshListener {

    private int user_id;

    private int mPage;

    private RecyclerView recyclerView;
    private static final int MY_SELLING_CAR_DATA_TYPE = 44;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LinearLayout llTop;
    private LinearLayout llNoData;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_selling_car;
    }

    @Override
    protected void initView(View view) {
        user_id = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        llNoData = (LinearLayout) view.findViewById(R.id.llNoData);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        view.findViewById(R.id.btn_post_car).setOnClickListener(this);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post_car:
                showPostCarWindow();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(user_id)) {
            getDataFromNet();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            mPage = 0;
            if (EmptyUtils.isNotEmpty(user_id)) {
                getDataFromNet();
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        getDataFromNet();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getMySellingCarUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(user_id))
                .addParams(Constant.TYPE, "4")
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                if (mPage == 0) {
                                    smartRefreshLayout.finishRefresh();
                                    handlerSellingCarData(jsonObjectData);
                                } else {
                                    smartRefreshLayout.finishLoadmore();
                                    handlerSellingCarMoreData(jsonObjectData);
                                }
                            } else {
                                if (mPage > 0) {
                                    --mPage;
                                }
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            if (mPage > 0) {
                                --mPage;
                            }
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerSellingCarMoreData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
                baseRecyclerViewAdapter.addDatas(carInfoBeanArrayList);
                baseRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }


    private void handlerSellingCarData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);

            if (baseRecyclerViewAdapter == null) {
                baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, MY_SELLING_CAR_DATA_TYPE);
//                mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(baseRecyclerViewAdapter);
//                llTop = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.car_center_topview, null);//作为头布局存放过滤条件
//                mHeaderAndFooterWrapper.addHeaderView(llTop);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(baseRecyclerViewAdapter);

            } else {
                baseRecyclerViewAdapter.setDatas(carInfoBeanArrayList);
                baseRecyclerViewAdapter.notifyDataSetChanged();
            }

//        setView(llTop, carInfoBeanArrayList == null || carInfoBeanArrayList.isEmpty());
        if(EmptyUtils.isEmpty(carInfoBeanArrayList)){
                llNoData.setVisibility(View.VISIBLE);
        }else{
            llNoData.setVisibility(View.GONE);
        }
    }

    private void setView(LinearLayout linearLayout, boolean isNoData) {
        if (mHeaderAndFooterWrapper == null) {
            return;
        }
        linearLayout.removeAllViews();

        if (isNoData) {//没有符合条件的车辆
            View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_car, null);
            linearLayout.addView(noDataView);
        }
    }

    //将jsonobject用gson解析成CarInfoBean类
    private ArrayList<CarInfoBean> getCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            list = new ArrayList<>();
            JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCarInfo = jsonArray.optJSONObject(i).optJSONObject("car_info");
                CarInfoBean carInfoBean = gson.fromJson(jsonObjectCarInfo.toString(), CarInfoBean.class);
                list.add(carInfoBean);
            }
        }
        return list;
    }

//    @Override
//    public void onItemClick(Object data, int position) {
//        Log.i("TESTLOG","selling onitem click");
//        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
//            Log.i("TESTLOG","selling onitem click CarInfoBean");
//            CarInfoBean mySellingCarBean = (CarInfoBean) data;
//            if (EmptyUtils.isNotEmpty(mySellingCarBean)) {
//                String car_id = mySellingCarBean.getCar_id();
//                if (EmptyUtils.isNotEmpty(car_id)) {
//                    Log.i("TESTLOG","selling onitem click car_id");
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constant.CAR_ID, car_id);
//                    gotoPager(CarDetailFragment.class, bundle);
//                }
//            }
//        }
//    }

    private void showPostCarWindow() {
        MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(), new MorePopupWindow.MorePopupWindowClickListener() {
            @Override
            public void onFirstBtnClicked() {
                //上传新车
                Bundle bundle = new Bundle();
                bundle.putString("from_class", getClass().getName());
                bundle.putBoolean("is_post_new_car", true);
                DataManager.getInstance().setObject(null);
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onSecondBtnClicked() {
                //上传二手车
                Bundle bundle = new Bundle();
                bundle.putString("from_class", getClass().getName());
                bundle.putBoolean("is_post_new_car", false);
                DataManager.getInstance().setObject(null);
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onThirdBtnClicked() {

            }

            @Override
            public void onFourthBtnClicked() {

            }

            @Override
            public void onCancelBtnClicked() {

            }
        }, MorePopupWindow.MORE_POPUP_WINDOW_TYPE.TYPE_SALE_CAR);
        morePopupWindow.initView();
        morePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
