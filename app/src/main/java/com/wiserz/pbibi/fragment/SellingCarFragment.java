package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.SellingAndSoldCarListBean;
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
 * Created by jackie on 2017/8/28 11:47.
 * QQ : 971060378
 * Used as : 在售车辆的页面
 */
public class SellingCarFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int mPageNo;
    private int userId;

    private static final int SELLING_CAR_DATA_TYPE = 21;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_selling_car;
    }

    @Override
    protected void initView(View view) {
        userId = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        view.findViewById(R.id.tv_total).setOnClickListener(this);
        view.findViewById(R.id.tv_total_sold).setOnClickListener(this);

        mPageNo = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_total:
                ToastUtils.showShort("全部在售");
                break;
            case R.id.tv_total_sold:
                ToastUtils.showShort("全部已售");
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
                .url(Constant.getSellingCarUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.TYPE, String.valueOf(3))//1：已售出 2:在售 ,3：在售最新＋已出售；
                .addParams(Constant.USER_ID, String.valueOf(userId))
                .addParams(Constant.PAGE, String.valueOf(mPageNo))
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
                                //解析在售车辆的数据
                                parseDataForSellingCar(jsonObjectData);
                                //解析已售车辆的数据
                                parseDataForSoldCar(jsonObjectData);
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

    private void parseDataForSellingCar(JSONObject dataJsonObject) {
        if (EmptyUtils.isNotEmpty(dataJsonObject) && getView() != null) {
            JSONObject jsonObjectForSelling = dataJsonObject.optJSONObject("saleing");

            if (EmptyUtils.isNotEmpty(jsonObjectForSelling)) {
                getView().findViewById(R.id.rl_selling).setVisibility(View.VISIBLE);
                int total = jsonObjectForSelling.optInt("total");//在售车辆总数

                ((TextView) getView().findViewById(R.id.tv_selling_car)).setText("在售车辆" + total + "辆");

                JSONArray car_list = jsonObjectForSelling.optJSONArray("car_list");
                if (EmptyUtils.isNotEmpty(car_list)) {
                    Gson gson = new Gson();
                    ArrayList<SellingAndSoldCarListBean> sellingAndSoldCarListBeanArrayList = gson.fromJson(car_list.toString(), new TypeToken<ArrayList<SellingAndSoldCarListBean>>() {
                    }.getType());

                    if (EmptyUtils.isNotEmpty(sellingAndSoldCarListBeanArrayList) && sellingAndSoldCarListBeanArrayList.size() != 0) {
                        RecyclerView recycler_view_selling_car = (RecyclerView) getView().findViewById(R.id.recycler_view_selling_car);
                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, sellingAndSoldCarListBeanArrayList, SELLING_CAR_DATA_TYPE);
                        recycler_view_selling_car.setAdapter(baseRecyclerViewAdapter);
                        recycler_view_selling_car.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        baseRecyclerViewAdapter.setOnItemClickListener(this);
                    } else {
                        getView().findViewById(R.id.rl_selling).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void parseDataForSoldCar(JSONObject dataJsonObject) {
        if (EmptyUtils.isNotEmpty(dataJsonObject) && getView() != null) {
            JSONObject jsonObjectForSold = dataJsonObject.optJSONObject("sold");

            if (EmptyUtils.isNotEmpty(jsonObjectForSold)) {
                getView().findViewById(R.id.rl_sold).setVisibility(View.VISIBLE);
                int total = jsonObjectForSold.optInt("total");//已售车辆总数
                ((TextView) getView().findViewById(R.id.tv_sold_car)).setText("已售车辆" + total + "辆");

                JSONArray car_list = jsonObjectForSold.optJSONArray("car_list");

                if (EmptyUtils.isNotEmpty(car_list)) {
                    Gson gson = new Gson();
                    ArrayList<SellingAndSoldCarListBean> sellingAndSoldCarListBeanArrayList = gson.fromJson(car_list.toString(), new TypeToken<ArrayList<SellingAndSoldCarListBean>>() {
                    }.getType());

                    if (EmptyUtils.isNotEmpty(sellingAndSoldCarListBeanArrayList) && sellingAndSoldCarListBeanArrayList.size() != 0) {
                        RecyclerView recycler_view_sold_car = (RecyclerView) getView().findViewById(R.id.recycler_view_sold_car);
                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, sellingAndSoldCarListBeanArrayList, SELLING_CAR_DATA_TYPE);
                        recycler_view_sold_car.setAdapter(baseRecyclerViewAdapter);
                        recycler_view_sold_car.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        baseRecyclerViewAdapter.setOnItemClickListener(this);
                    } else {
                        getView().findViewById(R.id.rl_sold).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("SellingAndSoldCarListBean")) {
            SellingAndSoldCarListBean sellingAndSoldCarListBean = (SellingAndSoldCarListBean) data;
            if (EmptyUtils.isNotEmpty(sellingAndSoldCarListBean)) {
                SellingAndSoldCarListBean.CarInfoBean carInfoBean = sellingAndSoldCarListBean.getCar_info();
                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    String car_id = carInfoBean.getCar_id();
                    if (EmptyUtils.isNotEmpty(car_id)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.CAR_ID, car_id);
                        gotoPager(CarDetailFragment.class, bundle);
                    }
                }
            }
        }
    }
}
