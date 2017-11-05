package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ThemeInfoBean;
import com.wiserz.pbibi.bean.ThemeUserBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/14 17:00.
 * QQ : 971060378
 * Used as : 话题成员的页面
 */
public class TopicMemberFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int theme_id;
    private int mPage;

    private static final int TOPIC_MEMBER_DATA_TYPE = 32;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_member;
    }

    @Override
    protected void initView(View view) {
        theme_id = getArguments().getInt(Constant.THEME_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("话题成员");

        mPage = 0;
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
        if (EmptyUtils.isNotEmpty(theme_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getTopicMemberUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.THEME_ID, String.valueOf(theme_id))
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
                                handlerDataForThemeInfo(jsonObjectData.optJSONObject("theme_info"));
                                handlerDataForThemeUser(jsonObjectData.optJSONObject("theme_user"));
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

    private void handlerDataForThemeUser(JSONObject theme_user) {
        if (EmptyUtils.isNotEmpty(theme_user)) {
            JSONArray jsonArray = theme_user.optJSONArray("users");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                Gson gson = new Gson();
                ArrayList<ThemeUserBean> themeUserBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ThemeUserBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(themeUserBeanArrayList) && getView() != null) {
                    RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, themeUserBeanArrayList, TOPIC_MEMBER_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    private void handlerDataForThemeInfo(JSONObject theme_info) {
        if (EmptyUtils.isNotEmpty(theme_info)) {
            Gson gson = new Gson();
            final ThemeInfoBean themeInfoBean = gson.fromJson(theme_info.toString(), ThemeInfoBean.class);

            if (EmptyUtils.isNotEmpty(themeInfoBean) && getView() != null) {
                if (EmptyUtils.isNotEmpty(themeInfoBean.getUser_info())) {
                    Glide.with(mContext)
                            .load(themeInfoBean.getUser_info().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into((ImageView) getView().findViewById(R.id.iv_circle_image));
                    ((TextView) getView().findViewById(R.id.tv_name)).setText(themeInfoBean.getUser_info().getNickname());
                    ((TextView) getView().findViewById(R.id.tv_num)).setText("粉丝 " + themeInfoBean.getUser_info().getFans_num() + " | " + "关注 " + themeInfoBean.getUser_info().getFriend_num());//粉丝 145 丨 关注 12

                    getView().findViewById(R.id.include_topic_member).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CommonUtil.isHadLogin()) {
                                gotoPager(RegisterAndLoginActivity.class, null);
                                return;
                            }
                            int user_id = themeInfoBean.getUser_info().getUser_id();
                            if (user_id == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                                gotoPager(MyHomePageFragment.class, null);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constant.USER_ID, user_id);
                                gotoPager(OtherHomePageFragment.class, bundle);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        LogUtils.e(data.getClass().getSimpleName());
        if (data.getClass().getSimpleName().equals("ThemeUserBean")) {
            if(!CommonUtil.isHadLogin()) {
                gotoPager(RegisterAndLoginActivity.class, null);
                return;
            }
            ThemeUserBean themeUserBean = (ThemeUserBean) data;
            if (EmptyUtils.isNotEmpty(themeUserBean)) {
                int user_id = themeUserBean.getUser_id();
                if (user_id == SPUtils.getInstance().getInt(Constant.USER_ID)) {
                    gotoPager(MyHomePageFragment.class, null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.USER_ID, user_id);
                    gotoPager(OtherHomePageFragment.class, bundle);
                }
            }
        }
    }
}
