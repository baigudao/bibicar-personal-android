package com.wiserz.pbibi.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/18 10:49.
 * QQ : 971060378
 * Used as : 话题搜索的Fragment
 */
public class TopicSearchFragment extends BaseFragment {

    private RecyclerView topic_search_recycler_view;
    private int mPage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_search;
    }

    @Override
    protected void initView(View view) {
        topic_search_recycler_view = (RecyclerView) view.findViewById(R.id.topic_search_recycler_view);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet("摄影");
    }

    private void getDataFromNet(String keyword) {
        OkHttpUtils.post()
                .url(Constant.getTopicSearchUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, "85e8c1b3a7e2b3a64296892bf56b3b42")
                .addParams(Constant.SESSION_ID, "session578614120f571")
                .addParams(Constant.KEY_WORD, keyword == null ? "" : keyword)
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                    }
                });
    }
}
