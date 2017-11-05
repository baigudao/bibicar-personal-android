package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.bean.FeedInfoBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;

/**
 * Created by jackie on 2017/8/17 14:43.
 * QQ : 971060378
 * Used as : 视频详情的页面
 */
public class VideoDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadmoreListener {

    private String share_title;
    private String share_txt;
    private String share_url;
    private String share_img;

    private int feed_id;
    private int mPage;

    private EditText et_input_comment;
    private Button btn_send;
    private LinearLayout ll_share_comment;
    private ImageView iv_like;

    private static final int ARTICLE_COMMENT_LIST_DATA_TYPE = 18;
    private int is_collect;

    private SmartRefreshLayout smartRefreshLayout;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_detail;
    }

    @Override
    protected void initView(View view) {
        feed_id = (int) DataManager.getInstance().getData1();
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("视频详情");

        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        ll_share_comment = (LinearLayout) view.findViewById(R.id.ll_share_comment);
        et_input_comment = (EditText) view.findViewById(R.id.et_input_comment);
        et_input_comment.addTextChangedListener(new TextChangedListener());

        ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        iv_like = (ImageView) view.findViewById(R.id.iv_like);
        iv_like.setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        mPage = 0;
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
            case R.id.btn_send:
                sendComment();
                break;
            case R.id.iv_share:
                showSharePlatformPopWindow();
                break;
            case R.id.iv_like:
                if (EmptyUtils.isNotEmpty(feed_id) && EmptyUtils.isNotEmpty(is_collect)) {
                    collectOrNot(feed_id, is_collect);
                }
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

    private void sendComment() {
        OkHttpUtils.post()
                .url(Constant.getCreateCommentUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.CONTENT, getCommentContent())
                .addParams(Constant.REPLY_ID, String.valueOf(0))
                .addParams(Constant.FATHER_ID, String.valueOf(0))
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
                                getCommentListDataFromNet();
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
        btn_send.setVisibility(View.GONE);
        ll_share_comment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        //释放资源
        JCVideoPlayer.releaseAllVideos();
    }

    private void getCommentListDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getArticleCommentListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                handlerVideoDetailDataForComment(jsonObjectData);
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

    private void getDataFromNet() {
        if (EmptyUtils.isNotEmpty(feed_id)) {
            OkHttpUtils.post()
                    .url(Constant.getVideoDetailUrl())
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
                                    switch (refresh_or_load) {
                                        case 0:
                                            smartRefreshLayout.finishRefresh();
                                            share_title = jsonObjectData.optString("share_title");
                                            share_txt = jsonObjectData.optString("share_txt");
                                            share_url = jsonObjectData.optString("share_url");
                                            share_img = jsonObjectData.optString("share_img");
                                            handlerVideoDetailData(jsonObjectData);
                                            getCommentListDataFromNet();
                                            break;
                                        case 1:
                                            smartRefreshLayout.finishLoadmore();
                                            break;
                                        default:
                                            break;
                                    }
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

    private void handlerVideoDetailDataForComment(JSONObject jsonObjectData) {
        JSONArray jsonArrayForComment = jsonObjectData.optJSONArray("comment_list");
        if (EmptyUtils.isNotEmpty(jsonArrayForComment) && getView() != null) {
            Gson gson = new Gson();
            ArrayList<ArticleCommentBean> articleCommentBeanArrayList = gson.fromJson(jsonArrayForComment.toString(), new TypeToken<ArrayList<ArticleCommentBean>>() {
            }.getType());

            if (EmptyUtils.isNotEmpty(articleCommentBeanArrayList) && articleCommentBeanArrayList.size() != 0) {
                getView().findViewById(R.id.tv_comment_area).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, articleCommentBeanArrayList, ARTICLE_COMMENT_LIST_DATA_TYPE);
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                getView().findViewById(R.id.tv_comment_area).setVisibility(View.GONE);
                getView().findViewById(R.id.recyclerView).setVisibility(View.GONE);
            }
        }
    }

    private void handlerVideoDetailData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObject = jsonObjectData.optJSONObject("feed_info");
            Gson gson = new Gson();
            if (EmptyUtils.isNotEmpty(jsonObject)) {
                final FeedInfoBean feedInfoBean = gson.fromJson(jsonObject.toString(), FeedInfoBean.class);

                if (EmptyUtils.isNotEmpty(feedInfoBean) && getView() != null) {
                    JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) getView().findViewById(R.id.videoPlayer);
                    jcVideoPlayerStandard.setUp(feedInfoBean.getHtml_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, feedInfoBean.getTitle());
                    jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse(feedInfoBean.getImage_url()));

                    ((TextView) getView().findViewById(R.id.tv_video_name)).setText(feedInfoBean.getTitle());
                    ((TextView) getView().findViewById(R.id.tv_player_num)).setText(feedInfoBean.getVisit_num() + "");
                    ((TextView) getView().findViewById(R.id.tv_like_num)).setText(feedInfoBean.getLike_num() + "");

                    if (EmptyUtils.isNotEmpty(feedInfoBean.getPost_user_info()) && EmptyUtils.isNotEmpty(feedInfoBean.getPost_user_info().getProfile())) {
                        final int user_id = feedInfoBean.getPost_user_info().getUser_id();
                        Glide.with(mContext)
                                .load(feedInfoBean.getPost_user_info().getProfile().getAvatar())
                                .placeholder(R.drawable.user_photo)
                                .error(R.drawable.user_photo)
                                .into((ImageView) getView().findViewById(R.id.iv_circle_image_post));
                        getView().findViewById(R.id.iv_circle_image_post).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!CommonUtil.isHadLogin()) {
                                    gotoPager(RegisterAndLoginActivity.class, null);
                                    return;
                                }
                                int user_id = feedInfoBean.getPost_user_info().getUser_id();
                                if (EmptyUtils.isNotEmpty(user_id)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.USER_ID, user_id);
                                    gotoPager(OtherHomePageFragment.class, bundle);
                                }
                            }
                        });
                        ((TextView) getView().findViewById(R.id.tv_user_name_post)).setText(feedInfoBean.getPost_user_info().getProfile().getNickname());
                        getView().findViewById(R.id.tv_user_name_post).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!CommonUtil.isHadLogin()) {
                                    gotoPager(RegisterAndLoginActivity.class, null);
                                    return;
                                }
                                if (EmptyUtils.isNotEmpty(user_id)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.USER_ID, user_id);
                                    gotoPager(OtherHomePageFragment.class, bundle);
                                }
                            }
                        });

                        final ImageView iv_follow = (ImageView) getView().findViewById(R.id.iv_follow);
                        if (SPUtils.getInstance().getInt(Constant.USER_ID) != user_id) {
                            final int is_friend = feedInfoBean.getPost_user_info().getIs_friend();
                            resetFollowView(iv_follow, is_friend);

                            iv_follow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!CommonUtil.isHadLogin()) {
                                        gotoPager(RegisterAndLoginActivity.class, null);
                                        return;
                                    }
                                    int is_friend = feedInfoBean.getPost_user_info().getIs_friend();
                                    switch (is_friend) {
                                        case 1:
                                            //已经关注，取消关注
                                            OkHttpUtils.post()
                                                    .url(Constant.getDeleteFollowUrl())
                                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                    .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                                                                    feedInfoBean.getPost_user_info().setIs_friend(2);
                                                                    resetFollowView(iv_follow, 2);
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
                                            break;
                                        case 2:
                                            //已经取消关注，再关注
                                            OkHttpUtils.post()
                                                    .url(Constant.getCreateFollowUrl())
                                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                    .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                                                                    feedInfoBean.getPost_user_info().setIs_friend(1);
                                                                    resetFollowView(iv_follow, 1);
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
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        } else {
                            iv_follow.setVisibility(View.GONE);
                        }
                    }
                    ((TextView) getView().findViewById(R.id.tv_publish_time)).setText("发布于" + TimeUtils.date2String(new Date(Long.valueOf(feedInfoBean.getCreated()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));//发布于2017-07-16

                    is_collect = feedInfoBean.getIs_collect();
                    if (EmptyUtils.isNotEmpty(is_collect)) {
                        switch (is_collect) {
                            case 1:
                                iv_like.setImageResource(R.drawable.v1_like_selected3x);
                                break;
                            case 2:
                                iv_like.setImageResource(R.drawable.v1_like3x);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void resetFollowView(ImageView iv_follow, int is_friend) {
        switch (is_friend) {
            case 1:
                iv_follow.setImageResource(R.drawable.other_followed);
                break;
            case 2:
                iv_follow.setImageResource(R.drawable.other_follow);
                break;
            default:
                break;
        }
    }

    /**
     * 收藏or不收藏
     */
    private void collectOrNot(int feed_ids, final int is_collects) {
        if (is_collects == 1) {
            //为1已经收藏了 需要取消收藏
            OkHttpUtils.post()
                    .url(Constant.getArticleDeleteURl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FEED_ID, String.valueOf(feed_ids))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShort("取消收藏失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int status = jsonObject.optInt("status");
                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                if (status == 1) {
                                    iv_like.setImageResource(R.drawable.v1_like3x);
                                    is_collect = 2;
                                    ToastUtils.showShort("取消收藏成功");
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

        } else if (is_collects == 2) {
            //为2点赞  还没收藏需要收藏
            OkHttpUtils.post()
                    .url(Constant.getArticleCollectURl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FEED_ID, String.valueOf(feed_ids))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShort("收藏视频失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int status = jsonObject.optInt("status");
                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                if (status == 1) {
                                    iv_like.setImageResource(R.drawable.v1_like_selected3x);
                                    is_collect = 1;
                                    ToastUtils.showShort("收藏视频成功");
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

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("ArticleCommentBean")) {
            ArticleCommentBean articleCommentBean = (ArticleCommentBean) data;

            if (EmptyUtils.isNotEmpty(articleCommentBean)) {
                //查看更多回复
                int comment_id = articleCommentBean.getComment_id();
                int feed_id = articleCommentBean.getFeed_id();
                if (EmptyUtils.isNotEmpty(comment_id) && EmptyUtils.isNotEmpty(feed_id)) {
                    DataManager.getInstance().setData1(feed_id);
                    DataManager.getInstance().setData2(comment_id);
                    DataManager.getInstance().setData3(articleCommentBean);
                    ((BaseActivity) mContext).gotoPager(CommentDetailFragment.class, null);
                }
                //                flag = 1;
                //                comment_id = articleCommentBean.getComment_id();
                //                et_input_comment.setFocusable(true);
                //                et_input_comment.setFocusableInTouchMode(true);
                //                et_input_comment.requestFocus();
                //                KeyboardUtils.showSoftInput(getActivity());
                //                KeyboardUtils.clickBlankArea2HideSoftInput();
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (EmptyUtils.isNotEmpty(feed_id)) {
            mPage = 0;
            refresh_or_load = 0;
            getDataFromNet();
        } else {
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (EmptyUtils.isNotEmpty(feed_id)) {
            ++mPage;
            refresh_or_load = 1;
            getDataFromNet();
        } else {
            smartRefreshLayout.finishLoadmore();
        }
    }

    private class TextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean user = et_input_comment.getText().length() > 0;
            if (user) {
                ll_share_comment.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
                btn_send.setEnabled(true);
            } else {
                ll_share_comment.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.GONE);
                btn_send.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(getActivity(), new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onSinaWeiboClicked() {
                showShare(mContext, "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                showShare(mContext, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                showShare(mContext, "WechatMoments", true);
            }

            @Override
            public void onCancelBtnClicked() {

            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    private void showShare(Context context, String platformToShare, boolean showContentEdit) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        //        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();

        oks.setTitle(share_title);
        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
            oks.setText(share_txt + "\n" + share_url);
        } else {
            oks.setText(share_img);
            oks.setImageUrl(share_img);
            oks.setUrl(share_url);
        }

        // 启动分享
        oks.show(context);
    }
}
