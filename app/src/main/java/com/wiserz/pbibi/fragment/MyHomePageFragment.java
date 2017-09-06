package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.FeedBean;
import com.wiserz.pbibi.bean.UserInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 16:39.
 * QQ : 971060378
 * Used as : 我的主页的页面
 */
public class MyHomePageFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private static final int MY_CAR_REPERTORY_DATA_TYPE = 24;

    private static final int MY_STATE_DATA_TYPE = 25;
    private int fans_num;
    private int friend_num;
    private int mPage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_home_page;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人主页");
        view.findViewById(R.id.tv_edit).setOnClickListener(this);

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
        OkHttpUtils.post()
                .url(Constant.getMyUserPageUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                handlerUserInfoData(jsonObjectData);
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
                                String share_img = jsonObjectData.optString("share_img");
                                String share_title = jsonObjectData.optString("share_title");
                                String share_txt = jsonObjectData.optString("share_txt");
                                String share_url = jsonObjectData.optString("share_url");
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
        if (getView() != null) {
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                strings.add("hehe");
            }
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, MY_CAR_REPERTORY_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    private void handlerDataForFeedList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)){
            JSONArray jsonArray = jsonObjectData.optJSONArray("feed_list");
            Gson gson = new Gson();
            if (EmptyUtils.isNotEmpty(jsonArray)&&getView()!=null){
                ArrayList<FeedBean> feedBeanArrayList = gson.fromJson(jsonArray.toString(),new TypeToken<ArrayList<FeedBean>>(){}.getType());

                if (EmptyUtils.isNotEmpty(feedBeanArrayList)){
                    RecyclerView state_recyclerView = (RecyclerView) getView().findViewById(R.id.state_recyclerView);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, feedBeanArrayList, MY_STATE_DATA_TYPE);
                    state_recyclerView.setAdapter(baseRecyclerViewAdapter);
                    state_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
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
                    Glide.with(mContext)
                            .load(userInfoBean.getProfile().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                    ((TextView) getView().findViewById(R.id.tv_user_name)).setText(userInfoBean.getProfile().getNickname());
                    //关注 12 丨 粉丝 145 丨BiBi号 12456
                    ((TextView) getView().findViewById(R.id.tv_follow_and_fan)).setText("关注 " + friend_num + " | " + "粉丝 " + fans_num + " | " + "BiBi号 " + userInfoBean.getProfile().getBibi_no());
                    ((TextView) getView().findViewById(R.id.tv_sign)).setText(userInfoBean.getProfile().getSignature());
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {

    }
}
