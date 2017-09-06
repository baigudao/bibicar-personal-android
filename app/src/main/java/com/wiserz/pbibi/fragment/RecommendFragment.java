package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.RecommendRecyclerViewAdapter;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/9 17:58.
 * QQ : 971060378
 * Used as : 推荐页面
 */
public class RecommendFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.tv_top_search).setOnClickListener(this);
        view.findViewById(R.id.iv_top_search_history).setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_top_search:
                gotoPager(SearchFragment.class, null);
                break;
            case R.id.iv_top_search_history:
                gotoPager(SearchHistoryFragment.class, null);
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
                .url(Constant.getNewHomeUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                RecommendRecyclerViewAdapter recommendRecyclerViewAdapter = new RecommendRecyclerViewAdapter(mContext, jsonObjectData);
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(recommendRecyclerViewAdapter);
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
}
