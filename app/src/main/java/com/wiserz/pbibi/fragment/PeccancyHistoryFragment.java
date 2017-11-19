package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.PeccancyHistoryBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/15 11:45.
 * QQ : 971060378
 * Used as : 违章历史的页面
 */
public class PeccancyHistoryFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadmoreListener {

    private static final int HPHM_DATA_TYPE = 36;

    private RecyclerView recyclerView;
    private int mPage;

    private SmartRefreshLayout smartRefreshLayout;
    private int refresh_or_load;//0或1
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_peccancy_history;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        mPage = 0;
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
                .url(Constant.getPeccancyListUrl())
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
                                switch (refresh_or_load) {
                                    case 0:
                                        smartRefreshLayout.finishRefresh();
                                        handlerData(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerMoreData(jsonObjectData);
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

    private void handlerMoreData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                ArrayList<PeccancyHistoryBean> peccancyHistoryBeanArrayList = new ArrayList<>();
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    PeccancyHistoryBean peccancyHistoryBean = new PeccancyHistoryBean();
                    String hphm = jsonArray.optJSONObject(i).optString("hphm");
                    String created = jsonArray.optJSONObject(i).optString("created");
                    peccancyHistoryBean.setHphm(hphm);
                    peccancyHistoryBean.setCreated(created);
                    peccancyHistoryBeanArrayList.add(peccancyHistoryBean);
                }

                if (EmptyUtils.isNotEmpty(peccancyHistoryBeanArrayList) && peccancyHistoryBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter.addDatas(peccancyHistoryBeanArrayList);
                }
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }

    private void handlerData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                ArrayList<PeccancyHistoryBean> peccancyHistoryBeanArrayList = new ArrayList<>();
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    PeccancyHistoryBean peccancyHistoryBean = new PeccancyHistoryBean();
                    String hphm = jsonArray.optJSONObject(i).optString("hphm");
                    String created = jsonArray.optJSONObject(i).optString("created");
                    peccancyHistoryBean.setHphm(hphm);
                    peccancyHistoryBean.setCreated(created);
                    peccancyHistoryBeanArrayList.add(peccancyHistoryBean);
                }

                if (EmptyUtils.isNotEmpty(peccancyHistoryBeanArrayList) && peccancyHistoryBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, peccancyHistoryBeanArrayList, HPHM_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("PeccancyHistoryBean")) {
            LogUtils.e("点击事件");
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        refresh_or_load = 0;
        getDataFromNet();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        refresh_or_load = 1;
        getDataFromNet();
    }
}
