package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.ArticleDetailBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/17 16:48.
 * QQ : 971060378
 * Used as : 文章详情的页面
 */
public class ArticleDetailFragment extends BaseFragment {

    private int feed_id;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article_detail;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        feed_id = bundle.getInt(Constant.FEED_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("文章详情");
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
        if (EmptyUtils.isNotEmpty(feed_id)) {
            OkHttpUtils.post()
                    .url(Constant.getArticleIndexUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FEED_ID, String.valueOf(feed_id))
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
                                    handlerArticleDetailData(jsonObjectData);
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

    private void handlerArticleDetailData(JSONObject jsonObjectData) {
        JSONObject jsonObject = jsonObjectData.optJSONObject("feed_info");
        Gson gson = new Gson();
        ArticleDetailBean articleDetailBean = gson.fromJson(jsonObject.toString(), ArticleDetailBean.class);

        if (EmptyUtils.isNotEmpty(articleDetailBean) && getView() != null) {
            Glide.with(mContext)
                    .load(articleDetailBean.getImage_url().get(0))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .into((ImageView) getView().findViewById(R.id.iv_article_image));
            ((TextView) getView().findViewById(R.id.tv_big_title)).setText(articleDetailBean.getTitle());
            Glide.with(mContext)
                    .load(articleDetailBean.getPost_user_info().getProfile().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into((ImageView) getView().findViewById(R.id.iv_circle_image));
            ((TextView) getView().findViewById(R.id.tv_author_and_time)).setText(articleDetailBean.getFeed_from() + " · " + TimeUtils.date2String(new Date(Long.valueOf(articleDetailBean.getCreated()) * 1000), new SimpleDateFormat("yyyy/MM/dd")));
        }
    }
}
