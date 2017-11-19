package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleBean;
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
 * Created by jackie on 2017/8/18 10:46.
 * QQ : 971060378
 * Used as : 文章搜索的Fragment
 */
public class ArticleSearchFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView article_search_recycler_view;
    private LinearLayout ll_search_history;
    private int mPage;

    private static final int ARTICLE_LIST_DATA_TYPE = 76;

    private String keyword;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article_search;
    }

    @Override
    protected void initView(View view) {
        keyword = (String) DataManager.getInstance().getData1();
        LogUtils.e("ArticleSearchFragment keyword为：" + keyword);
        DataManager.getInstance().setData1(null);
        article_search_recycler_view = (RecyclerView) view.findViewById(R.id.article_search_recycler_view);
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
            article_search_recycler_view.setVisibility(View.VISIBLE);
            ll_search_history.setVisibility(View.GONE);
            getDataFromNet(keyword);
        } else {
            //当没有keyword时
            ll_search_history.setVisibility(View.VISIBLE);
            article_search_recycler_view.setVisibility(View.GONE);
        }
    }

    private void getDataFromNet(String keyword) {
        OkHttpUtils.post()
                .url(Constant.getArticleSearchUrl())
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
                                handlerArticleListData(jsonObjectData);
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

    private void handlerArticleListData(JSONObject jsonObject) {
        ArrayList<ArticleBean> articleBeanArrayList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("list");
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectForArticleList = jsonArray.optJSONObject(i);
                ArticleBean articleBean = gson.fromJson(jsonObjectForArticleList.toString(), ArticleBean.class);
                articleBeanArrayList.add(articleBean);
            }
        }

        if (EmptyUtils.isNotEmpty(articleBeanArrayList)) {
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, articleBeanArrayList, ARTICLE_LIST_DATA_TYPE);
            article_search_recycler_view.setAdapter(baseRecyclerViewAdapter);
            article_search_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.ll_custom_made).setVisibility(View.VISIBLE);
                article_search_recycler_view.setVisibility(View.GONE);
                ll_search_history.setVisibility(View.GONE);
            }
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
