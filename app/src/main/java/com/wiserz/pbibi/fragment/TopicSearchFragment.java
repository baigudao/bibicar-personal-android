package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

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
import com.wiserz.pbibi.util.DataManager;
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
    private LinearLayout ll_search_history;
    private int mPage;
    private static final int ALL_TOPIC_DATA_TYPE = 23;
    private String keyword;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_search;
    }

    @Override
    protected void initView(View view) {
        keyword = (String) DataManager.getInstance().getData1();
        LogUtils.e("TopicSearchFragment keyword为：" + keyword);
        DataManager.getInstance().setData1(null);
        topic_search_recycler_view = (RecyclerView) view.findViewById(R.id.topic_search_recycler_view);
        ll_search_history = (LinearLayout) view.findViewById(R.id.ll_search_history);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(keyword)) {
            //当传入keyword时
            topic_search_recycler_view.setVisibility(View.VISIBLE);
            ll_search_history.setVisibility(View.GONE);
            getDataFromNet(keyword);
        } else {
            //当没有keyword时
            ll_search_history.setVisibility(View.VISIBLE);
            topic_search_recycler_view.setVisibility(View.GONE);
        }
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
                                ToastUtils.showShort("" + msg);
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
            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, topicInfoBeanArrayList, ALL_TOPIC_DATA_TYPE);
                topic_search_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                topic_search_recycler_view.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                if (getView() != null) {
                    getView().findViewById(R.id.ll_custom_made).setVisibility(View.VISIBLE);
                    topic_search_recycler_view.setVisibility(View.GONE);
                    ll_search_history.setVisibility(View.GONE);
                }
            }
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
