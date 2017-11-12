package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.MySellingCarBean;
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
public class MySellingCarFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int user_id;

    private int mPage;

    private RecyclerView recyclerView;
    private static final int MY_SELLING_CAR_DATA_TYPE = 44;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_selling_car;
    }

    @Override
    protected void initView(View view) {
        user_id = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getMySellingCarUrl())
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
                                handlerSellingCarData(jsonObjectData);
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

    private void handlerSellingCarData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");

            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                ArrayList<MySellingCarBean> mySellingCarBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MySellingCarBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(mySellingCarBeanArrayList) && mySellingCarBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, mySellingCarBeanArrayList, MY_SELLING_CAR_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            } else {
                ToastUtils.showShort("你没有可售车辆");
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("MySellingCarBean")) {
            MySellingCarBean mySellingCarBean = (MySellingCarBean) data;
            if (EmptyUtils.isNotEmpty(mySellingCarBean)) {
                String car_id = mySellingCarBean.getCar_info().getCar_id();
                if (EmptyUtils.isNotEmpty(car_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CAR_ID, car_id);
                    gotoPager(CarDetailFragment.class, bundle);
                }
            }
        }
    }

    private void showPostCarWindow() {
        MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(), new MorePopupWindow.MorePopupWindowClickListener() {
            @Override
            public void onFirstBtnClicked() {
                //上传新车
                DataManager.getInstance().setObject(null);
                gotoPager(PostNewCarFragment.class, null);
            }

            @Override
            public void onSecondBtnClicked() {
                //上传二手车
                DataManager.getInstance().setObject(null);
                gotoPager(PostSecondHandCarFragment.class, null);
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
