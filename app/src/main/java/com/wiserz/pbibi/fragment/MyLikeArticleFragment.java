package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/11 14:09.
 * QQ : 971060378
 * Used as : 我喜欢的文章
 */
public class MyLikeArticleFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int mPage;
    private RecyclerView recyclerView;
    private static final int ARTICLE_LIST_DATA_TYPE = 76;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_like_article;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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
                .url(Constant.getArticleCollectListURl())
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
                                handlerArticleListData(jsonObjectData);
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

    private void handlerArticleListData(JSONObject jsonObject) {
        ArrayList<ArticleBean> articleBeanArrayList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("feed_list");
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            articleBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ArticleBean>>() {
            }.getType());
        }

        if (EmptyUtils.isNotEmpty(articleBeanArrayList) && articleBeanArrayList.size() != 0) {
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, articleBeanArrayList, ARTICLE_LIST_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.ll_no_article).setVisibility(View.VISIBLE);
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
