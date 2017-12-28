package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.bean.UserInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/9 18:02.
 * QQ : 971060378
 * Used as : 个人中心页面
 */
public class MyFragment extends BaseFragment implements OnRefreshListener {

    private int fans_num;
    private int friend_num;
    private int user_id;

    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        user_id = SPUtils.getInstance().getInt(Constant.USER_ID);
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText("我的");
        view.findViewById(R.id.rl_user).setOnClickListener(this);
        view.findViewById(R.id.rl_setting).setOnClickListener(this);
        view.findViewById(R.id.rl_my_wallet).setOnClickListener(this);
        view.findViewById(R.id.rl_total_property).setOnClickListener(this);
        view.findViewById(R.id.rl_my_car_repertory).setOnClickListener(this);
        view.findViewById(R.id.rl_like).setOnClickListener(this);
        view.findViewById(R.id.rl_my_order).setOnClickListener(this);
        view.findViewById(R.id.rl_search_history).setOnClickListener(this);
        view.findViewById(R.id.rl_car_service).setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(this);
    }

    public void onResume(){
        super.onResume();
        if (EmptyUtils.isNotEmpty(user_id)) {
            getDataFromNet();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user:
                gotoPager(MyHomePageFragment.class, null);//我的主页
                break;
            case R.id.rl_setting:
                gotoPager(SettingFragment.class, null);//设置
                break;
            case R.id.rl_my_wallet:
                gotoPager(MyWalletFragment.class, null);//我的钱包
                break;
            case R.id.rl_total_property:
                gotoPager(TotalPropertyFragment.class, null);//总资产
                break;
            case R.id.rl_my_car_repertory:
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.USER_ID,user_id);
                gotoPager(MyCarRepertoryFragment.class, bundle);//我的车库
                break;
            case R.id.rl_like:
                gotoPager(MyLikeFragment.class, null);//喜欢的
                break;
            case R.id.rl_my_order:
                gotoPager(MyOrderFragment.class, null);//我的订单
                break;
            case R.id.rl_car_service:
                gotoPager(CarServiceFragment.class, null);//汽车服务
                break;
            case R.id.rl_search_history:
                gotoPager(CheckHistoryFragment.class, null);//汽车服务
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
                .url(Constant.getMyHomePageUrl())
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
                                smartRefreshLayout.finishRefresh();
                                fans_num = jsonObjectData.optInt("fans_num");
                                friend_num = jsonObjectData.optInt("friend_num");
                                JSONObject jsonObjectUserInfo = jsonObjectData.optJSONObject("user_info");
                                handlerDataForUserInfo(jsonObjectUserInfo);
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

    public void resetView(){
        LoginBean.UserInfoBean userInfoBean=DataManager.getInstance().getUserInfo();
        if (EmptyUtils.isNotEmpty(userInfoBean) && getView() != null) {
            if (EmptyUtils.isNotEmpty(userInfoBean.getProfile())) {
                Log.e("aaaaaaaaaa",user_id+", "+SPUtils.getInstance().getInt(Constant.USER_ID));
                if(user_id != SPUtils.getInstance().getInt(Constant.USER_ID)){
                    user_id=SPUtils.getInstance().getInt(Constant.USER_ID);
                    initData();
                }
                Glide.with(mContext)
                        .load(userInfoBean.getProfile().getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                ((TextView) getView().findViewById(R.id.tv_user_name)).setText(userInfoBean.getProfile().getNickname() == null ? "" : userInfoBean.getProfile().getNickname());
                //关注 4 丨 粉丝 12 丨 BiBi号 13456
                ((TextView) getView().findViewById(R.id.tv_num)).setText("关注 " + friend_num + " | " + "粉丝 " + fans_num + " | " + "BiBi号 " + userInfoBean.getProfile().getBibi_no());
                ((TextView) getView().findViewById(R.id.tv_car_price)).setText("0.0");
                ((TextView) getView().findViewById(R.id.tv_car_num)).setText("0");
            }
        }
    }

    private void handlerDataForUserInfo(JSONObject jsonObjectUserInfo) {
        if (EmptyUtils.isNotEmpty(jsonObjectUserInfo)) {
            Gson gson = new Gson();
            UserInfoBean userInfoBean = gson.fromJson(jsonObjectUserInfo.toString(), UserInfoBean.class);
            if (EmptyUtils.isNotEmpty(userInfoBean) && getView() != null) {
                if (EmptyUtils.isNotEmpty(userInfoBean.getProfile())) {
                    Glide.with(mContext)
                            .load(userInfoBean.getProfile().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                    ((TextView) getView().findViewById(R.id.tv_user_name)).setText(userInfoBean.getProfile().getNickname() == null ? "" : userInfoBean.getProfile().getNickname());
                    //关注 4 丨 粉丝 12 丨 BiBi号 13456
                    ((TextView) getView().findViewById(R.id.tv_num)).setText("关注 " + friend_num + " | " + "粉丝 " + fans_num + " | " + "BiBi号 " + userInfoBean.getProfile().getBibi_no());
                    ((TextView) getView().findViewById(R.id.tv_car_price)).setText(String.valueOf(userInfoBean.getTotal_money()));
                    ((TextView) getView().findViewById(R.id.tv_car_num)).setText(String.valueOf(userInfoBean.getTotal_car()));
                }
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (EmptyUtils.isNotEmpty(user_id)) {
            getDataFromNet();
        } else {
            smartRefreshLayout.finishRefresh();
        }
    }
}
