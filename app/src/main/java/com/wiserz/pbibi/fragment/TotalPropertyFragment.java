package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.RichListRecyclerViewAdapter;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 17:38.
 * QQ : 971060378
 * Used as : 总资产的页面
 */
public class TotalPropertyFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private RecyclerView recyclerView;

    private SmartRefreshLayout smartRefreshLayout;
    private int refresh_or_load;//0或1

    private int mPage;

    private RichListRecyclerViewAdapter richListRecyclerViewAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_total_property;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.tv_title).setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        mPage = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
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
                .url(Constant.getMyRichListUrl())
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
                                        handlerDataForTotalProperty(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerMoreDataForTotalProperty(jsonObjectData);
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

    private void handlerMoreDataForTotalProperty(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            richListRecyclerViewAdapter.addDatas(jsonObjectData);
        }
    }

    private void handlerDataForTotalProperty(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            richListRecyclerViewAdapter = new RichListRecyclerViewAdapter(mContext, jsonObjectData);
            recyclerView.setAdapter(richListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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
