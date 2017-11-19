package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/11 10:55.
 * QQ : 971060378
 * Used as : 豪车租赁的页面
 */
public class CarRentFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadmoreListener {

    private static final int CAR_RENT_DATA_TYPE = 77;
    private RecyclerView recyclerView;
    private int page;

    private SmartRefreshLayout smartRefreshLayout;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_rent;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("豪车租赁");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("我的租车");
        btn_register.setTextSize(15);
        btn_register.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        page = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                gotoPager(MyCarRentFragment.class, null);//我的租车
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
        OkHttpUtils.post()
                .url(Constant.getCarRentListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.PAGE, String.valueOf(page))
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
                                switch (refresh_or_load) {
                                    case 0:
                                        smartRefreshLayout.finishRefresh();
                                        handlerDataForCarRent(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerMoreDataForCarRent(jsonObjectData);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerMoreDataForCarRent(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarRentInfoBean> carRentInfoBeanArrayList = getCarRentData(jsonObjectData);

            if (carRentInfoBeanArrayList.size() == 0) {
                ToastUtils.showShort("没有更多了");
                return;
            }
            if (EmptyUtils.isNotEmpty(carRentInfoBeanArrayList)) {
                baseRecyclerViewAdapter.addDatas(carRentInfoBeanArrayList);
            }
        }
    }

    private void handlerDataForCarRent(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarRentInfoBean> carRentInfoBeanArrayList = getCarRentData(jsonObjectData);

            if (EmptyUtils.isNotEmpty(carRentInfoBeanArrayList)) {
                baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carRentInfoBeanArrayList, CAR_RENT_DATA_TYPE);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    private ArrayList<CarRentInfoBean> getCarRentData(JSONObject jsonObjectData) {
        ArrayList<CarRentInfoBean> list = new ArrayList<>();
        JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");
        Gson gson = new Gson();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i).optJSONObject("car_info");
            CarRentInfoBean carRentInfoBean = gson.fromJson(jsonObject.toString(), CarRentInfoBean.class);
            list.add(carRentInfoBean);
        }
        return list;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarRentInfoBean")) {
            CarRentInfoBean carRentInfoBean = (CarRentInfoBean) data;
            Bundle bundle = new Bundle();
            bundle.putString(Constant.CAR_ID, carRentInfoBean.getCar_id());
            gotoPager(CarRentDetailFragment.class, bundle);//汽车租赁详情
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        refresh_or_load = 0;
        getDataFromNet();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++page;
        refresh_or_load = 1;
        getDataFromNet();
    }
}
