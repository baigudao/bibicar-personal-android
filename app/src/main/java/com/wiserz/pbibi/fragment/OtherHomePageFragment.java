package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.FeedBean;
import com.wiserz.pbibi.bean.UserInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import okhttp3.Call;

/**
 * Created by jackie on 2017/9/22 14:26.
 * QQ : 971060378
 * Used as : 其他用户的个人主页
 */
public class OtherHomePageFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, BaseRecyclerViewAdapter.OnShareToPlatform {

    private int user_id;

    private static final int MY_CAR_REPERTORY_DATA_TYPE = 24;

    private static final int MY_STATE_DATA_TYPE = 25;

    private int fans_num;
    private int friend_num;
    private int mPage;
    private int is_friend;
    private int feed_num;

    private LinearLayout ll_follow_and_message;
    private View view_bottom_line;

    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;
    private int user_id_from_net;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_home_page;
    }

    @Override
    protected void initView(View view) {
        user_id = getArguments().getInt(Constant.USER_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人主页");
        view.findViewById(R.id.rl_my_car_repertory).setOnClickListener(this);
        view.findViewById(R.id.tv_edit).setVisibility(View.GONE);

        view_bottom_line = view.findViewById(R.id.view_bottom_line);
        view_bottom_line.setVisibility(View.GONE);
        ll_follow_and_message = (LinearLayout) view.findViewById(R.id.ll_follow_and_message);
        ll_follow_and_message.setVisibility(View.GONE);
        view.findViewById(R.id.rl_follow).setOnClickListener(this);
        view.findViewById(R.id.rl_message).setOnClickListener(this);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tv_edit:
                gotoPager(EditUserProfileFragment.class, null);
                break;
            case R.id.rl_my_car_repertory:
                gotoPager(MyCarRepertoryFragment.class, null);
                break;
            case R.id.rl_follow://这个个人数据是变化的
                ToastUtils.showShort("关注");
                break;
            case R.id.rl_message://这个个人数据是变化的
                //                if (mMyUserInfo == null) {
                //                    return;
                //                }
                //                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE,
                //                        String.valueOf(mMyUserInfo.getUser_info().getUser_id()), mMyUserInfo.getUser_info().getProfile().getNickname());
                //                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                //                    @Override
                //                    public UserInfo getUserInfo(String userId) {
                //                        return new UserInfo(String.valueOf(mMyUserInfo.getUser_info().getUser_id()), mMyUserInfo.getUser_info().getProfile().getNickname(),
                //                                Uri.parse(mMyUserInfo.getUser_info().getProfile().getAvatar())); //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                //                    }
                //
                //                }, true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(user_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getMyUserPageUrl())
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
                                fans_num = jsonObjectData.optInt("fans_num");
                                feed_num = jsonObjectData.optInt("feed_num");
                                is_friend = jsonObjectData.optInt("is_friend");
                                friend_num = jsonObjectData.optInt("friend_num");
                                handlerUserInfoData(jsonObjectData);
                                handlerCarInfoData(jsonObjectData);
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
        OkHttpUtils.post()
                .url(Constant.getMyFriendsUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                                share_img = jsonObjectData.optString("share_img");
                                share_title = jsonObjectData.optString("share_title");
                                share_txt = jsonObjectData.optString("share_txt");
                                share_url = jsonObjectData.optString("share_url");
                                int total = jsonObjectData.optInt("total");
                                if (getView() != null && EmptyUtils.isNotEmpty(total)) {
                                    ((TextView) getView().findViewById(R.id.tv_state_num)).setText("动态 (" + total + ")");//动态 （0）
                                }
                                handlerDataForFeedList(jsonObjectData);
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

    private void handlerCarInfoData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObjectForCarInfo = jsonObjectData.optJSONObject("car_info");
            int total = jsonObjectForCarInfo.optInt("total");
            double total_price = jsonObjectForCarInfo.optDouble("total_price");

            if (getView() != null && EmptyUtils.isNotEmpty(total) && EmptyUtils.isNotEmpty(total_price)) {
                ((TextView) getView().findViewById(R.id.tv_total_car)).setText(" (拥有" + total + "辆,总值" + total_price + "万) ");//（拥有13辆，总值134万）
            }

            JSONArray jsonArray = jsonObjectForCarInfo.optJSONArray("car_list");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                Gson gson = new Gson();

                ArrayList<CarInfoBean> carInfoBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CarInfoBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0 && getView() != null) {
                    RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, MY_CAR_REPERTORY_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    private void handlerDataForFeedList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("feed_list");
            Gson gson = new Gson();
            if (EmptyUtils.isNotEmpty(jsonArray) && getView() != null) {
                ArrayList<FeedBean> feedBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FeedBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(feedBeanArrayList) && feedBeanArrayList.size() != 0) {
                    RecyclerView state_recyclerView = (RecyclerView) getView().findViewById(R.id.state_recyclerView);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, feedBeanArrayList, MY_STATE_DATA_TYPE);
                    state_recyclerView.setAdapter(baseRecyclerViewAdapter);
                    state_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                    baseRecyclerViewAdapter.setOnShareToPlatform(this);
                }
            }
        }
    }

    private void handlerUserInfoData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObject = jsonObjectData.optJSONObject("user_info");
            Gson gson = new Gson();
            UserInfoBean userInfoBean = gson.fromJson(jsonObject.toString(), UserInfoBean.class);

            if (EmptyUtils.isNotEmpty(userInfoBean) && getView() != null) {
                if (EmptyUtils.isNotEmpty(userInfoBean.getProfile())) {
                    user_id_from_net = userInfoBean.getUser_id();
                    ((TextView) getView().findViewById(R.id.tv_title)).setText(userInfoBean.getProfile().getNickname() + "的主页");
                    Glide.with(mContext)
                            .load(userInfoBean.getProfile().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                    ((TextView) getView().findViewById(R.id.tv_user_name)).setText(EmptyUtils.isEmpty(userInfoBean.getProfile().getNickname()) ? " " : userInfoBean.getProfile().getNickname());
                    //关注 12 丨 粉丝 145 丨BiBi号 12456
                    ((TextView) getView().findViewById(R.id.tv_follow_and_fan)).setText("关注 " + friend_num + " | " + "粉丝 " + fans_num + " | " + "BiBi号 " + userInfoBean.getProfile().getBibi_no());
                    ((TextView) getView().findViewById(R.id.tv_sign)).setText(EmptyUtils.isEmpty(userInfoBean.getProfile().getSignature()) ? "这位车主非常有个性，因为她没有个性签名。" : userInfoBean.getProfile().getSignature());
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
            CarInfoBean carInfoBean = (CarInfoBean) data;
            if (EmptyUtils.isNotEmpty(carInfoBean)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBean.getCar_id());
                gotoPager(CarDetailFragment.class, bundle);
            }
        } else if (data.getClass().getSimpleName().equals("FeedBean")) {
            FeedBean feedBean = (FeedBean) data;
            if (EmptyUtils.isNotEmpty(feedBean)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.FEED_ID, feedBean.getFeed_id());
                gotoPager(StateDetailFragment.class, bundle);//动态详情
            }
        }
    }

    @Override
    public void share() {
        showSharePlatformPopWindow();
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
