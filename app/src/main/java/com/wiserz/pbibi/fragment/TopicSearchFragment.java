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
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/18 10:49.
 * QQ : 971060378
 * Used as : 话题搜索的Fragment
 */
public class TopicSearchFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView topic_search_recycler_view;
    private int mPage;
    private static final int ALL_TOPIC_DATA_TYPE = 23;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_search;
    }

    @Override
    protected void initView(View view) {
        LogUtils.e("TopicSearchFragment");
        topic_search_recycler_view = (RecyclerView) view.findViewById(R.id.topic_search_recycler_view);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet("达晨");
    }

    private void getDataFromNet(String keyword) {
        OkHttpUtils.post()
                .url(Constant.getTopicSearchUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.KEY_WORD, keyword == null ? "" : keyword)
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
                                handlerDataForSearchTopic(jsonObjectData);
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

    private void handlerDataForSearchTopic(JSONObject jsonObjectData) {
        JSONArray jsonArray = jsonObjectData.optJSONArray("theme_list");
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            ArrayList<TopicInfoBean> topicInfoBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TopicInfoBean>>() {
            }.getType());
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, topicInfoBeanArrayList, ALL_TOPIC_DATA_TYPE);
            topic_search_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            topic_search_recycler_view.setAdapter(baseRecyclerViewAdapter);
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("TopicInfoBean")) {
            TopicInfoBean topicInfoBean = (TopicInfoBean) data;
            ToastUtils.showShort(topicInfoBean.getTheme());
        }
    }
}
