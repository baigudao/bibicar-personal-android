package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.adapter.HeaderAndFooterWrapper;
import com.wiserz.pbibi.bean.LoveCarBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/29 9:45.
 * QQ : 971060378
 * Used as : 我的爱车的页面
 */
public class MyLoveCarFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int user_id;
    private int mPage;

    private RecyclerView recyclerView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LinearLayout llTop;
    private static final int LOVE_CAR_DATA_TYPE = 43;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_love_car;
    }

    @Override
    protected void initView(View view) {
        user_id = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(user_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getUserLoveCarUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                                //处理数据
                                handlerData(jsonObjectData);
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

    private void handlerData(JSONObject jsonObjectData) {
        ArrayList<LoveCarBean> loveCarBeanArrayList = null;

        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONObject("list").optJSONArray("car_list");
            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                loveCarBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<LoveCarBean>>() {
                }.getType());
            }
        }


            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, loveCarBeanArrayList, LOVE_CAR_DATA_TYPE);
            mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(baseRecyclerViewAdapter);
            llTop = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.car_center_topview, null);//作为头布局存放过滤条件
            mHeaderAndFooterWrapper.addHeaderView(llTop);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mHeaderAndFooterWrapper);
            baseRecyclerViewAdapter.setOnItemClickListener(this);

            setView(llTop, loveCarBeanArrayList == null || loveCarBeanArrayList.isEmpty());
    }

    private void setView(LinearLayout linearLayout, boolean isNoData) {
        linearLayout.removeAllViews();

        if (isNoData) {//没有符合条件的车辆
            View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_car, null);
            linearLayout.addView(noDataView);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("LoveCarBean")) {
            LoveCarBean loveCarBean = (LoveCarBean) data;
            if (EmptyUtils.isNotEmpty(loveCarBean)) {
                String car_id = loveCarBean.getCar_id();
                if (EmptyUtils.isNotEmpty(car_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CAR_ID, car_id);
                    gotoPager(CarDetailFragment.class, bundle);
                }
            }
        }
    }
}
