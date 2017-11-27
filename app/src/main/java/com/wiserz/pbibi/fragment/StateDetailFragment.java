package com.wiserz.pbibi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.bean.FeedBean;
import com.wiserz.pbibi.bean.FeedInfoDetailBean;
import com.wiserz.pbibi.bean.LikeListBean;
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
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

import static com.wiserz.pbibi.R.id.comment_recyclerView;


/**
 * Created by jackie on 2017/9/13 11:47.
 * QQ : 971060378
 * Used as : 动态详情的页面
 */
public class StateDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int feed_id;

    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;

    private ImageView iv3;
    private TextView tv_like_num;
    private int user_id;
    private int mPage;

    private static final int ARTICLE_COMMENT_LIST_DATA_TYPE = 18;
    private static final int LIKE_LIST_DATA_TYPE = 42;

    private LinearLayout ll_three_point;
    private LinearLayout ll_input_view;
    private EditText et_input_comment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_state_detail;
    }

    @Override
    protected void initView(View view) {
        feed_id = getArguments().getInt(Constant.FEED_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("动态详情");
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.GONE);//去掉删除入口
        iv_image.setScaleType(ImageView.ScaleType.CENTER);
        ViewGroup.LayoutParams layoutParams = iv_image.getLayoutParams();
        layoutParams.width = 60;
        layoutParams.height = 20;
        iv_image.setLayoutParams(layoutParams);
        iv_image.setImageResource(R.drawable.report_selector);
        iv_image.setOnClickListener(this);

        view.findViewById(R.id.iv_circle_image).setOnClickListener(this);
        view.findViewById(R.id.tv_user_name).setOnClickListener(this);

        ll_three_point = (LinearLayout) getView().findViewById(R.id.ll_three_point);
        ll_input_view = (LinearLayout) getView().findViewById(R.id.ll_input_view);
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        et_input_comment = (EditText) view.findViewById(R.id.et_input_comment);

        iv3 = (ImageView) view.findViewById(R.id.iv3);
        tv_like_num = (TextView) view.findViewById(R.id.tv_like_num);

        view.findViewById(R.id.rl_share).setOnClickListener(this);
        view.findViewById(R.id.rl_comment).setOnClickListener(this);
        view.findViewById(R.id.rl_like).setOnClickListener(this);

        mPage = 0;
    }

    private String getInputContent() {
        return et_input_comment.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                if(!CommonUtil.isHadLogin()) {
                    ((BaseActivity) mContext).gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                if (EmptyUtils.isNotEmpty(feed_id) && EmptyUtils.isNotEmpty(user_id)) {
                    showDialog(v, user_id, null);
                }
                break;
            case R.id.rl_share:
                if(!CommonUtil.isHadLogin()) {
                    ((BaseActivity) mContext).gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showSharePlatformPopWindow();
                break;
            case R.id.rl_comment:
                if(!CommonUtil.isHadLogin()) {
                    ((BaseActivity) mContext).gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                resetCommentView();
                KeyboardUtils.showSoftInput(ll_input_view);
                break;
            case R.id.rl_like:
                if(!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                if (EmptyUtils.isNotEmpty(feed_id)) {
                    likeOrNot();
                }
                break;
            case R.id.btn_send:
                if(!CommonUtil.isHadLogin()) {
                    ((BaseActivity) mContext).gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                if (EmptyUtils.isEmpty(getInputContent())) {
                    ToastUtils.showShort("请输入评论的内容");
                    return;
                }
                OkHttpUtils.post()
                        .url(Constant.getCreateCommentUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                        .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                        .addParams(Constant.CONTENT, getInputContent())
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
                                        getCommentListData();
                                        ToastUtils.showShort("评论成功");
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
                et_input_comment.setText("");
                KeyboardUtils.hideSoftInput(ll_input_view);
                ll_three_point.setVisibility(View.VISIBLE);
                ll_input_view.setVisibility(View.GONE);
                break;
            case R.id.tv_user_name:
            case R.id.iv_circle_image:
                if(!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                if (user_id == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                    gotoPager(MyHomePageFragment.class, null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.USER_ID, user_id);
                    gotoPager(OtherHomePageFragment.class, bundle);
                }
                break;
            default:
                break;
        }
    }

    private void resetCommentView() {
        ll_three_point.setVisibility(View.GONE);
        ll_input_view.setVisibility(View.VISIBLE);
    }

    private void showDialog(View v, int user_id, final ArrayList<FeedBean> feedBeanArrayList) {
        //        先判断该动态是否是自己的
        String[] items1 = {"删除"};
        String[] items2 = {"举报"};
        if (SPUtils.getInstance().getInt(Constant.USER_ID) == user_id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            //builder.setMessage("是否确认退出?"); //设置内容
            builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
            //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
            builder.setItems(items1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
                    mDialog.setTitle("警告");
                    mDialog.setMessage("是否要删除此动态?");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //                            feedBeanArrayList.remove(feedBean);
                            //                            notifyDataSetChanged();
                            OkHttpUtils.post()
                                    .url(Constant.getDeleteCommentUrl())
                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                    .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                                    .addParams(Constant.COMMENT_ID, "")
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
                                                    ToastUtils.showShort("删除成功!");
                                                    goBack();
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
                    });
                    mDialog.setNegativeButton("取消", null);
                    mDialog.create().show();
                }
            });
            builder.create().show();
        } else {
            //别人发的
            //dialog参数设置
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            //builder.setMessage("是否确认退出?"); //设置内容
            builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
            //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
            builder.setItems(items2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //--------------------------延迟一秒---------------------------------
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //-----------------------------------------------------------
                    ToastUtils.showLong("^_^ 我们已收到您的举报，会尽快处理！\n" +
                            "                    感谢您的支持！");
                }
            });
            builder.create().show();
        }
    }

    private void likeOrNot() {
        if(!CommonUtil.isHadLogin()) {
            gotoPager(RegisterAndLoginActivity.class, null);
            return;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 555) {
                    //取消点赞
                    OkHttpUtils.post()
                            .url(Constant.getLikeDeleteURl())
                            .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                            .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                            .addParams(Constant.FEED_ID, String.valueOf(feed_id))
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
                                            iv3.setImageResource(R.drawable.v1_like3x);
                                            int like_num = Integer.valueOf(tv_like_num.getText().toString());
                                            --like_num;
                                            tv_like_num.setText(String.valueOf(like_num));
                                            ToastUtils.showShort("取消点赞成功");
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
            }
        };
        //点赞
        OkHttpUtils.post()
                .url(Constant.getLikeCreateURl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
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
                                iv3.setImageResource(R.drawable.v1_like_selected3x);
                                int like_num = Integer.valueOf(tv_like_num.getText().toString());
                                ++like_num;
                                tv_like_num.setText(String.valueOf(like_num));
                                ToastUtils.showShort("点赞成功");
                            } else {
                                String code = jsonObject.optString("code");
                                if (code.equals("67000")) {
                                    handler.sendEmptyMessage(555);
                                    return;
                                }
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(feed_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getPostDetailUrl())
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
                                share_img = jsonObjectData.optString("share_img");
                                share_title = jsonObjectData.optString("share_title");
                                share_txt = jsonObjectData.optString("share_txt");
                                share_url = jsonObjectData.optString("share_url");
                                handlerDataForFeedInfo(jsonObjectData.optJSONObject("feed_info"));
                                handlerDataForLikeList(jsonObjectData.optJSONArray("like_list"));
                                getCommentListData();//得到评论列表的数据
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
                                handlerCommentData(jsonObjectData);
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

    private void handlerCommentData(JSONObject jsonObjectData) {
        JSONArray jsonArrayForComment = jsonObjectData.optJSONArray("comment_list");
        if (EmptyUtils.isNotEmpty(jsonArrayForComment) && getView() != null) {
            Gson gson = new Gson();
            ArrayList<ArticleCommentBean> articleCommentBeanArrayList = gson.fromJson(jsonArrayForComment.toString(), new TypeToken<ArrayList<ArticleCommentBean>>() {
            }.getType());

            if (EmptyUtils.isNotEmpty(articleCommentBeanArrayList) && articleCommentBeanArrayList.size() != 0) {
                getView().findViewById(R.id.ll_comment_area).setVisibility(View.VISIBLE);
                getView().findViewById(comment_recyclerView).setVisibility(View.VISIBLE);

                RecyclerView comment_recyclerView = (RecyclerView) getView().findViewById(R.id.comment_recyclerView);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, articleCommentBeanArrayList, ARTICLE_COMMENT_LIST_DATA_TYPE);
                comment_recyclerView.setAdapter(baseRecyclerViewAdapter);
                comment_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                getView().findViewById(R.id.ll_comment_area).setVisibility(View.GONE);
                getView().findViewById(comment_recyclerView).setVisibility(View.GONE);
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
            }
        } else if (data.getClass().getSimpleName().equals("LikeListBean")) {
            if(!CommonUtil.isHadLogin()) {
                gotoPager(RegisterAndLoginActivity.class, null);
                return;
            }
            LikeListBean likeListBean = (LikeListBean) data;
            int userID = likeListBean.getUser_id();
            if (userID == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                gotoPager(MyHomePageFragment.class, null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.USER_ID, userID);
                gotoPager(OtherHomePageFragment.class, bundle);
            }
        }
    }

    private void handlerDataForLikeList(JSONArray like_list) {
        if (EmptyUtils.isNotEmpty(like_list)) {
            Gson gson = new Gson();
            ArrayList<LikeListBean> likeListBeanArrayList = gson.fromJson(like_list.toString(), new TypeToken<ArrayList<LikeListBean>>() {
            }.getType());

            if (EmptyUtils.isNotEmpty(likeListBeanArrayList) && likeListBeanArrayList.size() != 0) {
                ((TextView) getView().findViewById(R.id.tv_zan_num)).setText(likeListBeanArrayList.size() + "人赞过");

                RecyclerView zan_recyclerView = (RecyclerView) getView().findViewById(R.id.zan_recyclerView);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, likeListBeanArrayList, LIKE_LIST_DATA_TYPE);
                zan_recyclerView.setAdapter(baseRecyclerViewAdapter);
                zan_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    private void handlerDataForFeedInfo(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            Gson gson = new Gson();
            final FeedInfoDetailBean feedInfoDetailBean = gson.fromJson(jsonObjectData.toString(), FeedInfoDetailBean.class);
            if (EmptyUtils.isNotEmpty(feedInfoDetailBean) && getView() != null) {
                Glide.with(mContext)
                        .load(feedInfoDetailBean.getPost_user_info().getProfile().getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                ((TextView) getView().findViewById(R.id.tv_user_name)).setText(feedInfoDetailBean.getPost_user_info().getProfile().getNickname());
                ((TextView) getView().findViewById(R.id.tv_user_time)).setText(TimeUtils.date2String(new Date(Long.valueOf(feedInfoDetailBean.getCreated()) * 1000), new SimpleDateFormat("yy-MM-dd HH:mm")));

                user_id = feedInfoDetailBean.getPost_user_info().getUser_id();

                final ImageView iv_follow = (ImageView) getView().findViewById(R.id.iv_follow);
                if (SPUtils.getInstance().getInt(Constant.USER_ID) != user_id) {
                    final int is_friend = feedInfoDetailBean.getPost_user_info().getIs_friend();
                    resetFollowView(iv_follow, is_friend);

                    iv_follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CommonUtil.isHadLogin()) {
                                gotoPager(RegisterAndLoginActivity.class, null);
                                return;
                            }
                            int is_friend = feedInfoDetailBean.getPost_user_info().getIs_friend();
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
                                                            feedInfoDetailBean.getPost_user_info().setIs_friend(2);
                                                            resetFollowView(iv_follow, 2);
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
                                                            feedInfoDetailBean.getPost_user_info().setIs_friend(1);
                                                            resetFollowView(iv_follow, 1);
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
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                } else {
                    iv_follow.setVisibility(View.GONE);
                }

                ((TextView) getView().findViewById(R.id.tv_post_content)).setText(feedInfoDetailBean.getPost_content());

                ((TextView) getView().findViewById(R.id.tv_share_num)).setText(String.valueOf(feedInfoDetailBean.getCollect_num()));
                ((TextView) getView().findViewById(R.id.tv_comment_num)).setText(String.valueOf(feedInfoDetailBean.getComment_num()));
                ((TextView) getView().findViewById(R.id.tv_like_num)).setText(String.valueOf(feedInfoDetailBean.getLike_num()));

                int is_collect = feedInfoDetailBean.getIs_collect();
                if (EmptyUtils.isNotEmpty(is_collect)) {
                    switch (is_collect) {
                        case 1:
                            iv3.setBackgroundResource(R.drawable.v1_like_selected3x);
                            break;
                        case 2:
                            iv3.setBackgroundResource(R.drawable.v1_like3x);
                            break;
                        default:
                            break;
                    }
                }

                //添加图片内容
                addImageContent(feedInfoDetailBean.getPost_files());
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

    private void addImageContent(List<FeedInfoDetailBean.PostFilesBean> post_files) {
        if (EmptyUtils.isNotEmpty(post_files) && getView() != null) {
            ArrayList<FeedInfoDetailBean.PostFilesBean> postFilesBeanArrayList = (ArrayList<FeedInfoDetailBean.PostFilesBean>) post_files;
            LinearLayout ll_image_container = (LinearLayout) getView().findViewById(R.id.ll_image_container);

            if (EmptyUtils.isNotEmpty(postFilesBeanArrayList) && postFilesBeanArrayList.size() != 0) {

                int content_size = postFilesBeanArrayList.size();
                if (ll_image_container.getChildCount() != content_size) {
                    ll_image_container.removeAllViews();
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    for (int i = 0; i < content_size; i++) {
                        ll_image_container.addView(layoutInflater.inflate(R.layout.show_state_image_content, null));
                    }
                }
                View content_view;
                for (int i = 0; i < content_size; i++) {
                    content_view = ll_image_container.getChildAt(i);
                    final ImageView iv_show_state_content = (ImageView) content_view.findViewById(R.id.iv_show_state_content);
                    //图片
                    iv_show_state_content.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(postFilesBeanArrayList.get(i).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(iv_show_state_content);
                }
            }
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
            public void onCreatQr() {

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
