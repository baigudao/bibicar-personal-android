package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/11 14:08.
 * QQ : 971060378
 * Used as : 我喜欢的车辆
 */
public class MyLikeCarFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int mPage;
    private RecyclerView recyclerView;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_like_car;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getLikeCarFromNet();
    }

    private void getLikeCarFromNet() {
        OkHttpUtils.post()
                .url(Constant.getLikeCarListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                handlerCarListData(jsonObjectData);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerCarListData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, CAR_LIST_FOR_CAR_CENTER);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                if (getView() != null) {
                    getView().findViewById(R.id.ll_no_car).setVisibility(View.VISIBLE);
                }
            }
        }
    }

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

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
            CarInfoBean carInfoBean = (CarInfoBean) data;
            if (EmptyUtils.isNotEmpty(carInfoBean)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBean.getCar_id());
                gotoPager(CarDetailFragment.class, bundle);
            }
        }
    }
}
