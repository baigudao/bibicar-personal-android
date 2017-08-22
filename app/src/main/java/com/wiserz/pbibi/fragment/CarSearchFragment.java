package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBeanForCarCenter;
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
 * Created by jackie on 2017/8/18 10:43.
 * QQ : 971060378
 * Used as : 车辆搜索的Fragment
 */
public class CarSearchFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView car_search_recycler_view;
    private LinearLayout ll_search_history;
    private int mPage;
    private String keyword;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_search;
    }

    @Override
    protected void initView(View view) {
        keyword = (String) DataManager.getInstance().getData1();
        LogUtils.e("keyword为：" + keyword);
        DataManager.getInstance().setData1(null);
        car_search_recycler_view = (RecyclerView) view.findViewById(R.id.car_search_recycler_view);
        ll_search_history = (LinearLayout) view.findViewById(R.id.ll_search_history);
        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
//        if (EmptyUtils.isNotEmpty(keyword)) {
        //当传入keyword时
        car_search_recycler_view.setVisibility(View.VISIBLE);
        ll_search_history.setVisibility(View.GONE);
        LogUtils.e("有数据");
        getCarListDataFromNet(keyword, null, null, null, null, null, null, null, null, null, null, null, null);
//        } else {
//            //当没有keyword时
//            ll_search_history.setVisibility(View.VISIBLE);
//            car_search_recycler_view.setVisibility(View.GONE);
//            LogUtils.e("为空");
//        }
    }

    private void getCarListDataFromNet(String keyword, String order_id, String brand_id, String series_id,
                                       String min_price, String max_price, String min_mileage, String max_mileage,
                                       String min_board_time, String max_board_time, String has_vr, String old, String source) {
        OkHttpUtils.post()
                .url(Constant.getCarListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.KEY_WORD, keyword == null ? "" : keyword)//关键字
                .addParams(Constant.ORDER_ID, order_id == null ? "" : order_id)//排序id
                .addParams(Constant.BRAND_ID, brand_id == null ? "" : brand_id)//车品牌id
                .addParams(Constant.SERIES_ID, series_id == null ? "" : series_id)//车系列id
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
                .addParams(Constant.MIN_MILEAGE, min_mileage == null ? "" : min_mileage)//最低里程
                .addParams(Constant.MAX_MILEAGE, max_mileage == null ? "" : max_mileage)//最高里程
                .addParams(Constant.MIN_BOARD_TIME, min_board_time == null ? "" : min_board_time)//最短上牌时间
                .addParams(Constant.MAX_BOARD_TIME, max_board_time == null ? "" : max_board_time)//最长上牌时间
                .addParams(Constant.HAS_VR, has_vr == null ? "" : has_vr)//是否有VR看车功能  1:是
                .addParams(Constant.OLD, old == null ? "" : old)//是否新车二手车 1:新车 2 二手车
                .addParams(Constant.SOURCE, source == null ? "" : source)//车辆来源 1:个人 2 商家
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
        ArrayList<CarInfoBeanForCarCenter> carInfoBeanForCarCenterArrayList = getCarListData(jsonObjectData);
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanForCarCenterArrayList, CAR_LIST_FOR_CAR_CENTER);
        car_search_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        car_search_recycler_view.setAdapter(baseRecyclerViewAdapter);
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    private ArrayList<CarInfoBeanForCarCenter> getCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBeanForCarCenter> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            list = new ArrayList<>();
            JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCarInfo = jsonArray.optJSONObject(i).optJSONObject("car_info");
                CarInfoBeanForCarCenter carInfoBeanForCarCenter = gson.fromJson(jsonObjectCarInfo.toString(), CarInfoBeanForCarCenter.class);
                list.add(carInfoBeanForCarCenter);
            }
        }
        return list;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBeanForCarCenter")) {
            CarInfoBeanForCarCenter carInfoBeanForCarCenter = (CarInfoBeanForCarCenter) data;
            if (EmptyUtils.isNotEmpty(carInfoBeanForCarCenter)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBeanForCarCenter.getCar_id());
                gotoPager(CarDetailFragment.class, bundle);
            }
        }
    }
}
