package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.FuLiBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/14 14:44.
 * QQ : 971060378
 * Used as : 全部话题
 */
public class AllTopicFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private static final int ALL_TOPIC_DATA_TYPE = 23;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_topic;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("话题");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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
        OkHttpUtils.get()
                .url("http://gank.io/api/data/福利/10/1")
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
                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                handlerData(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerData(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("results");
        Gson gson = new Gson();
        ArrayList<FuLiBean> fuLiBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FuLiBean>>() {
        }.getType());
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, fuLiBeanArrayList, ALL_TOPIC_DATA_TYPE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("FuLiBean")) {
            FuLiBean fuLiBean = (FuLiBean) data;
            ToastUtils.showShort(fuLiBean.getWho());
        }
    }
}
