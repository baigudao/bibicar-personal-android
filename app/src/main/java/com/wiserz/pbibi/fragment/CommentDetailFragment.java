package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.AllSecondCommentBean;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/25 14:18.
 * QQ : 971060378
 * Used as : 评论详情的页面
 */
public class CommentDetailFragment extends BaseFragment {

    private static final int COMMENT_DETAIL_DATA_TYPE = 20;
    private int mPage;
    private int feed_id;
    private int comment_id;

    private TextView tv_zan;
    private EditText et_input_comment;
    private Button btn_send;

    private ArticleCommentBean articleCommentBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_detail;
    }

    @Override
    protected void initView(View view) {
        feed_id = (int) DataManager.getInstance().getData1();
        comment_id = (int) DataManager.getInstance().getData2();
        articleCommentBean = (ArticleCommentBean) DataManager.getInstance().getData3();
        DataManager.getInstance().setData1(null);
        DataManager.getInstance().setData2(null);
        DataManager.getInstance().setData3(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("评论详情");

        tv_zan = (TextView) view.findViewById(R.id.tv_zan);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_input_comment = (EditText) view.findViewById(R.id.et_input_comment);

        mPage = 0;

        if (EmptyUtils.isNotEmpty(articleCommentBean)) {
            showTopView(articleCommentBean);
        }
    }

    private String getCommentContent() {
        return et_input_comment.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.ll_zan_or_no:
                zanOrNo();
                break;
            case R.id.btn_send:
                sendComment();
                break;
            default:
                break;
        }
    }

    private void sendComment() {
        if (EmptyUtils.isEmpty(getCommentContent())) {
            ToastUtils.showShort("请输入回复的内容");
            return;
        }
        OkHttpUtils.post()
                .url(Constant.getCreateCommentUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.CONTENT, getCommentContent())
                .addParams(Constant.REPLY_ID, String.valueOf(comment_id))
                .addParams(Constant.FATHER_ID, String.valueOf(comment_id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("评论失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                getDataFromNet();
                                ToastUtils.showShort("评论成功");
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
        et_input_comment.setText("");
        KeyboardUtils.hideSoftInput(getActivity());
        KeyboardUtils.clickBlankArea2HideSoftInput();
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(feed_id) && EmptyUtils.isNotEmpty(comment_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getAllSecondComment())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
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
                                //                                handlerDataForSecondCommentTop(jsonObjectData);
                                handlerDataForSecondCommentList(jsonObjectData);
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

    private void handlerDataForSecondCommentList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("comment_list");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                Gson gson = new Gson();
                ArrayList<AllSecondCommentBean> allSecondCommentBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AllSecondCommentBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(allSecondCommentBeanArrayList) && getView() != null) {
                    RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, allSecondCommentBeanArrayList, COMMENT_DETAIL_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    private void showTopView(final ArticleCommentBean articleCommentBean) {
        if (getView() != null) {
            ImageView iv_circle_image = (ImageView) getView().findViewById(R.id.iv_circle_image);
            TextView tv_comment_name = (TextView) getView().findViewById(R.id.tv_comment_name);
            TextView tv_comment_time = (TextView) getView().findViewById(R.id.tv_comment_time);
            TextView tv_comment_content = (TextView) getView().findViewById(R.id.tv_comment_content);

            final int user_id = articleCommentBean.getFrom_user().getUser_id();
            Glide.with(mContext)
                    .load(articleCommentBean.getFrom_user().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into(iv_circle_image);
            iv_circle_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CommonUtil.isHadLogin()) {
                        gotoPager(RegisterAndLoginActivity.class, null);
                        return;
                    }
                    if (EmptyUtils.isNotEmpty(user_id)) {
                        if (SPUtils.getInstance().getInt(Constant.USER_ID) == user_id) {
                            ((BaseActivity) mContext).gotoPager(MyHomePageFragment.class, null);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.USER_ID, user_id);
                            ((BaseActivity) mContext).gotoPager(OtherHomePageFragment.class, bundle);
                        }
                    }
                }
            });

            tv_comment_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CommonUtil.isHadLogin()) {
                        gotoPager(RegisterAndLoginActivity.class, null);
                        return;
                    }
                    if (EmptyUtils.isNotEmpty(user_id)) {
                        if (SPUtils.getInstance().getInt(Constant.USER_ID) == user_id) {
                            ((BaseActivity) mContext).gotoPager(MyHomePageFragment.class, null);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.USER_ID, user_id);
                            ((BaseActivity) mContext).gotoPager(OtherHomePageFragment.class, bundle);
                        }
                    }
                }
            });
            tv_comment_name.setText(articleCommentBean.getFrom_user().getNickname());

            tv_comment_time.setText(TimeUtils.date2String(new Date(Long.valueOf(articleCommentBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

            int is_like = articleCommentBean.getIs_like();
            if (EmptyUtils.isNotEmpty(is_like)) {
                switch (is_like) {
                    case 1:
                        zanView();
                        break;
                    case 2:
                        //没赞
                        cancelZanView();
                        break;
                    default:
                        break;
                }
            }
            getView().findViewById(R.id.ll_zan_or_no).setOnClickListener(this);
            tv_zan.setText(String.valueOf(articleCommentBean.getLike_num()));

            tv_comment_content.setText(articleCommentBean.getComment_content());//评论的内容
        }
    }

    private void zanOrNo() {
        //点赞
        OkHttpUtils.post()
                .url(Constant.getCommentLikeCreate())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("点赞失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                zanView();
                                int like_num = Integer.valueOf(tv_zan.getText().toString());
                                ++like_num;
                                tv_zan.setText(String.valueOf(like_num));
                                ToastUtils.showShort("点赞成功");
                            } else {
                                String code = jsonObject.optString("code");
                                if (code.equals("67000")) {
                                    cancelZan();
                                    return;
                                }
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void cancelZan() {
        //取消点赞
        OkHttpUtils.post()
                .url(Constant.getCommentLikeDelete())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("取消点赞失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                cancelZanView();
                                int like_num = Integer.valueOf(tv_zan.getText().toString());
                                --like_num;
                                tv_zan.setText(String.valueOf(like_num));
                                ToastUtils.showShort("取消点赞成功");
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

    private void cancelZanView() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.GONE);
            iv_zan_no.setVisibility(View.VISIBLE);
        }
    }

    private void zanView() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.VISIBLE);
            iv_zan_no.setVisibility(View.GONE);
        }
    }
}
