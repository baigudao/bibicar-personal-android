package com.wiserz.pbibi.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;


/**
 * Created by jackie on 2017/8/28 10:03.
 * QQ : 971060378
 * Used as : 企业用户的个人中心
 */
public class MyFragmentForCompany extends BaseFragment {

    private int position;
    private Fragment fromFragment;
    private List<BaseFragment> mBaseFragment;

    private int mUserId;//自己的user_id
    private int flag;

    private ImageView ivSettings;
    private int fans_num;
    private int friend_num;
    private int user_id;//传过来的user_id

    private View view_bottom_line;
    private LinearLayout ll_follow_and_message;
    private RelativeLayout rl_follow;
    private RelativeLayout rl_message;
    private TextView tv_follow;
    private int is_friend;
    private LoginBean.UserInfoBean myInfos;

    public int getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_company;
    }

    @Override
    protected void initView(View view) {
        user_id = getArguments().getInt(Constant.USER_ID);
        ivSettings = (ImageView) view.findViewById(R.id.ivSettings);
        ivSettings.setOnClickListener(this);
        view.findViewById(R.id.ivMore).setOnClickListener(this);

        view_bottom_line = view.findViewById(R.id.view_bottom_line);
        ll_follow_and_message = (LinearLayout) view.findViewById(R.id.ll_follow_and_message);
        rl_follow = (RelativeLayout) view.findViewById(R.id.rl_follow);
        rl_message = (RelativeLayout) view.findViewById(R.id.rl_message);
        tv_follow = (TextView) view.findViewById(R.id.tv_follow);

        view.findViewById(R.id.tvFans).setOnClickListener(this);
        view.findViewById(R.id.tvFollow).setOnClickListener(this);

        RadioGroup rg_main = (RadioGroup) view.findViewById(R.id.rg_main);

        //初始化用户信息
        initUserInfo();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中的Fragment
        rg_main.check(R.id.rb_selling_car);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSettings:
                if (flag == 0) {
                    //自己 进入设置
                    gotoPager(SettingFragment.class, null);
                    break;
                } else if (flag == 1) {
                    //别人
                    goBack();
                    break;
                }
            case R.id.ivMore:
                ToastUtils.showShort("分享");
                break;
            case R.id.rl_message:
                if (myInfos == null || myInfos.getUser_id() == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                    return;
                }
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE,
                        String.valueOf(myInfos.getUser_id()), myInfos.getProfile().getNickname());
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                        return new UserInfo(String.valueOf(myInfos.getUser_id()), myInfos.getProfile().getNickname(),
                                Uri.parse(myInfos.getProfile().getAvatar())); //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                    }

                }, true);
                break;
            case R.id.rl_follow:
                if (EmptyUtils.isNotEmpty(getIs_friend())) {
                    followCode(getIs_friend());
                }
                break;
            case R.id.tvFollow:
            case R.id.tvFans:
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.USER_ID, user_id);
                gotoPager(FanAndFollowFragment.class, bundle);
                break;
            default:
                break;
        }
    }

    private void followCode(final int is_friend) {
        if (is_friend == 2) {
            //没关注，去关注
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
                                    setIs_friend(1);
                                    tv_follow.setText("已关注");
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
        } else if (is_friend == 1) {
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
                                    setIs_friend(2);
                                    tv_follow.setText("关注");
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

    protected void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new SellingCarFragment());//在售车辆
        mBaseFragment.add(new StateFragment());//动态
        mBaseFragment.add(new SalesConsultantFragment());//销售顾问
    }

    protected class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_state://动态
                    position = 1;
                    DataManager.getInstance().setData1(user_id);
                    break;
                case R.id.rb_selling_car://在售车辆
                    position = 0;
                    DataManager.getInstance().setData1(user_id);
                    break;
                case R.id.rb_sales_consultant://销售顾问
                    position = 2;
                    DataManager.getInstance().setData1(user_id);
                    break;
                default:
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment toFragment = getFragment();
            //切换Fragment
            switchFragment(fromFragment, toFragment);
        }
    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    protected BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    protected void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_my_company_content, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    protected void initUserInfo() {
        mUserId = SPUtils.getInstance().getInt(Constant.USER_ID);//456
        showPostNowViewByUserId();
        getHomePageInfo(user_id);
    }

    /**
     * 不同的用户显示不同的界面
     */
    protected void showPostNowViewByUserId() {
        if (getView() != null) {
            if (mUserId != user_id) {
                //他人
                ivSettings.setBackgroundResource(R.drawable.bibi_back_white);
                ViewGroup.LayoutParams layoutParams = ivSettings.getLayoutParams();
                layoutParams.width = SizeUtils.dp2px(25);
                layoutParams.height = SizeUtils.dp2px(20);
                ivSettings.setLayoutParams(layoutParams);
                view_bottom_line.setVisibility(View.VISIBLE);
                ll_follow_and_message.setVisibility(View.VISIBLE);
                rl_follow.setOnClickListener(this);
                rl_message.setOnClickListener(this);
                flag = 1;
            } else {
                //自己
                ivSettings.setBackgroundResource(R.drawable.self_set);
                view_bottom_line.setVisibility(View.GONE);
                ll_follow_and_message.setVisibility(View.GONE);
                flag = 0;
            }
        }
    }

    /**
     * 通过不同的userId得到相应用户的信息
     *
     * @param userId
     */
    protected void getHomePageInfo(final int userId) {
        OkHttpUtils.post()
                .url(Constant.getUserHomeUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(userId))
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
                                friend_num = jsonObjectData.optInt("friend_num");
                                int is_friends = jsonObjectData.optInt("is_friend");
                                setIs_friend(is_friends);
                                JSONObject jsonObjectForUserInfo = jsonObjectData.optJSONObject("user_info");
                                Gson gson = new Gson();
                                LoginBean.UserInfoBean userInfoBean = gson.fromJson(jsonObjectForUserInfo.toString(), LoginBean.UserInfoBean.class);
                                if (EmptyUtils.isNotEmpty(userInfoBean)) {
                                    initUserInfo(userInfoBean);
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

    protected void initUserInfo(LoginBean.UserInfoBean myInfo) {
        if (getView() == null) {
            return;
        }
        myInfos = myInfo;
        if (EmptyUtils.isNotEmpty(myInfo)) {
            ((TextView) getView().findViewById(R.id.tv_nickName)).setText(myInfo.getProfile().getNickname());

            ImageView ivAvater = (ImageView) getView().findViewById(R.id.ivAvater);

            Glide.with(mContext)
                    .load(myInfo.getProfile().getAvatar())
                    .error(R.drawable.user_photo)
                    .placeholder(R.drawable.user_photo)
                    .into((ImageView) getView().findViewById(R.id.ivAvater));

            ivAvater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_id == mUserId) {
                        gotoPager(EditUserProfileFragment.class, null);
                    }
                }
            });

            Glide.with(mContext)
                    .load(myInfo.getProfile().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .bitmapTransform(new BlurTransformation(mContext, 5))
                    .into(((ImageView) getView().findViewById(R.id.iv_image)));

            ((TextView) getView().findViewById(R.id.tvFollow)).setText(getString(R.string.follow_, String.valueOf(friend_num)));
            ((TextView) getView().findViewById(R.id.tvFans)).setText(getString(R.string.fans_, String.valueOf(fans_num)));

            if (EmptyUtils.isEmpty(myInfo.getProfile().getSignature())) {
                ((TextView) getView().findViewById(R.id.tv_signature)).setText(mUserId == SPUtils.getInstance().getInt(Constant.USER_ID) ? getString(R.string.have_no_bio) : getString(R.string.user_have_no_bio));
            } else {
                ((TextView) getView().findViewById(R.id.tv_signature)).setText(myInfo.getProfile().getSignature());
            }
            //            if (mUserId != SPUtils.getInstance().getInt(Constant.USER_ID)) {
            //                resetFollowStateView(is_friend);
            //            }
        }
    }

    //    protected void resetFollowStateView(int isFriend) {
    //        if (isFriend == 1 && getView() != null) {
    //            ((ImageView) getView().findViewById(R.id.iv_guan_zhu)).setImageResource(R.drawable.other_followed);
    //        } else {
    //            ((ImageView) getView().findViewById(R.id.iv_guan_zhu)).setImageResource(R.drawable.other_follow);
    //        }
    //    }
}
