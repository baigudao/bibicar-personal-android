package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.bean.ArticleDetailBean;
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
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import okhttp3.Call;

import static com.wiserz.pbibi.R.id.rl_share_wechat;
import static com.wiserz.pbibi.R.id.rl_share_wechatmoments;

/**
 * Created by jackie on 2017/8/17 16:48.
 * QQ : 971060378
 * Used as : 文章详情的页面
 */
public class ArticleDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadmoreListener {

    private int feed_id;
    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;
    private int mPage;

    private ImageView iv_like;

    private EditText et_input_comment;
    private Button btn_send;
    private LinearLayout ll_share_comment;

    private static final int ARTICLE_COMMENT_LIST_DATA_TYPE = 18;
    private int is_collect;

    private SmartRefreshLayout smartRefreshLayout;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

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

        view.findViewById(rl_share_wechat).setOnClickListener(this);
        view.findViewById(rl_share_wechatmoments).setOnClickListener(this);
        view.findViewById(R.id.rl_share_weibo).setOnClickListener(this);

        view.findViewById(R.id.iv_share).setOnClickListener(this);
        iv_like = (ImageView) view.findViewById(R.id.iv_like);
        iv_like.setOnClickListener(this);

        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        ll_share_comment = (LinearLayout) view.findViewById(R.id.ll_share_comment);
        et_input_comment = (EditText) view.findViewById(R.id.et_input_comment);
        et_input_comment.addTextChangedListener(new TextChangedListener());

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
            case R.id.rl_share_weibo:
                showShare(mContext, "SinaWeibo", true);
                break;
            case R.id.rl_share_wechat:
                showShare(mContext, "Wechat", true);
                break;
            case R.id.rl_share_wechatmoments:
                showShare(mContext, "WechatMoments", true);
                break;
            case R.id.iv_share:
                showSharePlatformPopWindow();
                break;
            case R.id.iv_like:
                if (EmptyUtils.isNotEmpty(feed_id) && EmptyUtils.isNotEmpty(is_collect)) {
                    collectOrNot(feed_id, is_collect);
                }
                break;
            case R.id.btn_send:
                sendComment();
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

    private void getDataFromNet() {
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
                                    switch (refresh_or_load) {
                                        case 0:
                                            smartRefreshLayout.finishRefresh();
                                            share_img = jsonObjectData.optString("share_img");
                                            share_title = jsonObjectData.optString("share_title");
                                            share_txt = jsonObjectData.optString("share_txt");
                                            share_url = jsonObjectData.optString("share_url");
                                            handlerArticleDetailData(jsonObjectData);
                                            getCommentListData();//得到评论列表的数据
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

    private void getCommentListData() {
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
                                handlerArticleCommentData(jsonObjectData);
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

    private void handlerArticleCommentData(JSONObject jsonObjectData) {
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
            ((TextView) getView().findViewById(R.id.tv_author_and_time)).setText(articleDetailBean.getPost_user_info().getProfile().getNickname() + " · "
                    + TimeUtils.date2String(new Date(Long.valueOf(articleDetailBean.getCreated()) * 1000), new SimpleDateFormat("yyyy/MM/dd")));

            is_collect = articleDetailBean.getIs_collect();
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

            //添加文章内容
            addArticleContent(articleDetailBean.getContent_info());
        }
    }

    private void addArticleContent(List<ArticleDetailBean.ContentInfoBean> content_info) {
        if (EmptyUtils.isNotEmpty(content_info) && getView() != null) {
            ArrayList<ArticleDetailBean.ContentInfoBean> contentInfoBeen = (ArrayList<ArticleDetailBean.ContentInfoBean>) content_info;
            LinearLayout ll_article_content = (LinearLayout) getView().findViewById(R.id.ll_article_content);

            int content_size = contentInfoBeen.size();
            if (ll_article_content.getChildCount() != content_size) {
                ll_article_content.removeAllViews();
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                for (int i = 0; i < content_size; i++) {
                    ll_article_content.addView(layoutInflater.inflate(R.layout.show_article_content, null));
                }
            }

            View content_view;
            for (int i = 0; i < content_size; i++) {
                content_view = ll_article_content.getChildAt(i);
                final ImageView iv_show_article_content = (ImageView) content_view.findViewById(R.id.iv_show_article_content);
                TextView tv_show_article_content = (TextView) content_view.findViewById(R.id.tv_show_article_content);
                int content_type = contentInfoBeen.get(i).getType();
                //type 文字1 图片2 视频3
                if (content_type == 1) {
                    //文字
                    iv_show_article_content.setVisibility(View.GONE);
                    tv_show_article_content.setVisibility(View.VISIBLE);
                    tv_show_article_content.setText(contentInfoBeen.get(i).getContent());
                } else if (content_type == 2) {
                    //图片
                    iv_show_article_content.setVisibility(View.VISIBLE);
                    tv_show_article_content.setVisibility(View.GONE);
                    Glide.with(mContext)
                            .load(contentInfoBeen.get(i).getContent())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Matrix matrix = new Matrix();
                                    matrix.setScale(0.5f, 0.5f);
                                    Bitmap newBit = resource.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
                                    //得到图片的宽高
                                    //                        int widthPic = resource.getWidth();
                                    //                        int height = resource.getHeight();
                                    //                        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                                    //                        Toast.makeText(getActivity(), "图片宽:" + widthPic + "图片高：" + height + "和屏幕宽" + screenWidth, Toast.LENGTH_SHORT).show();
                                    iv_show_article_content.setImageBitmap(newBit);
                                }
                            }); //方法中设置asBitmap可以设置回调类型
                }
            }
        }
    }

    /**
     * 收藏or不收藏
     */
    private void collectOrNot(int feed_ids, int is_collects) {
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
                            ToastUtils.showShort("收藏文章失败");
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
                                    ToastUtils.showShort("收藏文章成功");
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

    private void sendComment() {
        OkHttpUtils.post()
                .url(Constant.getCreateCommentUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.CONTENT, getCommentContent())
                //                .addParams(Constant.REPLY_ID, flag == 1 ? String.valueOf(comment_id) : String.valueOf(0))//flag =1为二级评论
                .addParams(Constant.REPLY_ID, String.valueOf(0))//flag =1为二级评论
                //                .addParams(Constant.FATHER_ID, flag == 1 ? String.valueOf(comment_id) : String.valueOf(0))
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
                                getCommentListData();
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
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        refresh_or_load = 0;
        getDataFromNet();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        refresh_or_load = 1;
        getDataFromNet();
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
}
