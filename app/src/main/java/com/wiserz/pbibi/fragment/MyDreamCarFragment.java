package com.wiserz.pbibi.fragment;

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
import com.wiserz.pbibi.bean.DreamCarBean;
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
 * Created by jackie on 2017/8/29 9:47.
 * QQ : 971060378
 * Used as : 我的梦想车
 */
public class MyDreamCarFragment extends BaseFragment {

    private static final int DREAM_CAR_DATA_TYPE = 26;
    private int user_id;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LinearLayout llTop;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_dream_car;
    }

    @Override
    protected void initView(View view) {
        user_id = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
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
                .url(Constant.getDreamCarUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                handlerDreamCarData(jsonArray);
                            } else {
                                String code = jsonObject.optString("code");
                                ToastUtils.showShort(""+code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerDreamCarData(JSONArray jsonArray) {
        ArrayList<DreamCarBean> dreamCarBeanArrayList = null;
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            Gson gson = new Gson();
            dreamCarBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<DreamCarBean>>() {
            }.getType());
        }

            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, dreamCarBeanArrayList, DREAM_CAR_DATA_TYPE);
            llTop = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.car_center_topview, null);//作为头布局存放过滤条件
            mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(baseRecyclerViewAdapter);
            mHeaderAndFooterWrapper.addHeaderView(llTop);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mHeaderAndFooterWrapper);

        setView(llTop, dreamCarBeanArrayList == null || dreamCarBeanArrayList.isEmpty());
    }

    private void setView(LinearLayout linearLayout, boolean isNoData) {
        linearLayout.removeAllViews();

        if (isNoData) {//没有符合条件的车辆
            View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_car, null);
            linearLayout.addView(noDataView);
        }
    }
}
