package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.FeedInfoDetailBean;
import com.wiserz.pbibi.util.Constant;
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


/**
 * Created by jackie on 2017/9/13 11:47.
 * QQ : 971060378
 * Used as : 动态详情的页面
 */
public class StateDetailFragment extends BaseFragment {

    private int feed_id;

    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;

    private ImageView iv3;
    private TextView tv_like_num;

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
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setScaleType(ImageView.ScaleType.CENTER);
        ViewGroup.LayoutParams layoutParams = iv_image.getLayoutParams();
        layoutParams.width = 60;
        layoutParams.height = 20;
        iv_image.setLayoutParams(layoutParams);
        iv_image.setImageResource(R.drawable.report_selector);
        iv_image.setOnClickListener(this);

        iv3 = (ImageView) view.findViewById(R.id.iv3);
        tv_like_num = (TextView) view.findViewById(R.id.tv_like_num);

        view.findViewById(R.id.rl_share).setOnClickListener(this);
        view.findViewById(R.id.rl_comment).setOnClickListener(this);
        view.findViewById(R.id.rl_like).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                ToastUtils.showShort("举报or删除");
                break;
            case R.id.rl_share:
                showSharePlatformPopWindow();
                break;
            case R.id.rl_comment:
                ToastUtils.showShort("评论");
                break;
            case R.id.rl_like:
                if (EmptyUtils.isNotEmpty(feed_id)) {
                    likeOrNot();
                }
                break;
            default:
                break;
        }
    }

    private void likeOrNot() {
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
                                            ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
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
                        LogUtils.e(response);
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

    private void handlerDataForLikeList(JSONArray like_list) {
        if (EmptyUtils.isNotEmpty(like_list)) {
            int size = like_list.length();

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

                int user_id = feedInfoDetailBean.getPost_user_info().getUser_id();
                if (EmptyUtils.isNotEmpty(user_id) && user_id == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                    getView().findViewById(R.id.tv_follow).setVisibility(View.GONE);
                }
                getView().findViewById(R.id.tv_follow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("关注");
                    }
                });

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
