package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
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

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_selling_car;
    }

    @Override
    protected void initView(View view) {
        userId = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        mPageNo = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getSellingCarUrl())
                .addParams(Constant.PAGE, String.valueOf(mPageNo))
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.TYPE, String.valueOf(3))//1：已售出 2:在售 ,3：在售最新＋已出售；
                .addParams(Constant.USER_ID, String.valueOf(userId))
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
        if (EmptyUtils.isNotEmpty(dataJsonObject)) {
            JSONObject jsonObjectForSelling = dataJsonObject.optJSONObject("saleing");

            int total = jsonObjectForSelling.optInt("total");//在售车辆总数
            if (getView() != null) {
                ((TextView) getView().findViewById(R.id.tv_selling_car)).setText("在售车辆" + total + "辆");

                if (total == 0) {
                    getView().findViewById(R.id.tv_total).setVisibility(View.INVISIBLE);
                }

                JSONArray car_list = jsonObjectForSelling.optJSONArray("car_list");
                Gson gson = new Gson();
                ArrayList<CarInfoBean> carInfoBeanArrayList = gson.fromJson(car_list.toString(), new TypeToken<ArrayList<CarInfoBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
                    RecyclerView recycler_view_selling_car = (RecyclerView) getView().findViewById(R.id.recycler_view_selling_car);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, CAR_LIST_FOR_CAR_CENTER);
                    recycler_view_selling_car.setAdapter(baseRecyclerViewAdapter);
                    recycler_view_selling_car.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    private void parseDataForSoldCar(JSONObject dataJsonObject) {
        if (EmptyUtils.isNotEmpty(dataJsonObject)) {
            JSONObject jsonObjectForSelling = dataJsonObject.optJSONObject("sold");
            int total = jsonObjectForSelling.optInt("total");//已售车辆总数
            if (getView() != null) {
                ((TextView) getView().findViewById(R.id.tv_sold_car)).setText("已售车辆" + total + "辆");
                if (total == 0) {
                    getView().findViewById(R.id.tv_sold_car).setVisibility(View.INVISIBLE);
                }
                JSONArray car_list = jsonObjectForSelling.optJSONArray("car_list");
                Gson gson = new Gson();

                ArrayList<CarInfoBean> carInfoBeanArrayList = gson.fromJson(car_list.toString(), new TypeToken<ArrayList<CarInfoBean>>() {
                }.getType());

                RecyclerView recycler_view_sold_car = (RecyclerView) getView().findViewById(R.id.recycler_view_sold_car);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, CAR_LIST_FOR_CAR_CENTER);
                recycler_view_sold_car.setAdapter(baseRecyclerViewAdapter);
                recycler_view_sold_car.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
            CarInfoBean carInfoBean = (CarInfoBean) data;
            if (EmptyUtils.isNotEmpty(carInfoBean)) {
                LogUtils.e(carInfoBean.getCar_name());
            }
        }
    }
}
