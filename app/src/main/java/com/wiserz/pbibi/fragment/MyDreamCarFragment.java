package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
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
                        LogUtils.e(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                handlerDreamCarData(jsonArray);
                            } else {
                                String code = jsonObject.optString("code");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerDreamCarData(JSONArray jsonArray) {
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            Gson gson = new Gson();
            ArrayList<DreamCarBean> dreamCarBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<DreamCarBean>>() {
            }.getType());
            if (EmptyUtils.isNotEmpty(dreamCarBeanArrayList) && getView() != null) {
                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, dreamCarBeanArrayList, DREAM_CAR_DATA_TYPE);
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
        } else {
            ToastUtils.showShort("你还没有梦想中的车型");
        }
    }
}
