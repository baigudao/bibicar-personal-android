package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/1 17:07.
 * QQ : 971060378
 * Used as : 文章浏览历史
 */
public class ArticleSearchHistoryFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayout ll_search_history;
    private int mPage;

    private static final int ARTICLE_LIST_DATA_TYPE = 76;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article_search_history;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_search_history = (LinearLayout) view.findViewById(R.id.ll_search_history);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getArticleSearchHistoryUrl())
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
                                handlerDataForArticleSearchHistory(jsonObjectData);
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

    private void handlerDataForArticleSearchHistory(JSONObject jsonObjectData) {
        JSONArray jsonArray = jsonObjectData.optJSONArray("feed_list");
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            ArrayList<ArticleBean> articleBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ArticleBean>>() {
            }.getType());

            if (!CommonUtil.isListNullOrEmpty(articleBeanArrayList)) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, articleBeanArrayList, ARTICLE_LIST_DATA_TYPE);
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            ll_search_history.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("ArticleBean")) {
            ArticleBean articleBean = (ArticleBean) data;
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.FEED_ID, articleBean.getFeed_id());
            gotoPager(ArticleDetailFragment.class, bundle);
        }
    }
}
