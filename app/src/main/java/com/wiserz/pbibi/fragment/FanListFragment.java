package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.FriendshipBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/10/12 10:10.
 * QQ : 971060378
 * Used as : 粉丝列表的页面
 */
public class FanListFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnItemClickListener {

    private int mPage;
    private int user_id;

    private RecyclerView recyclerView;

    private static final int FOLLOW_AND_FAN_DATA_TYPE = 45;

    private SmartRefreshLayout smartRefreshLayout;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fan_list;
    }

    @Override
    protected void initView(View view) {
        user_id = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getFriendShipListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(user_id))
                .addParams(Constant.ACTION, "2")//1 : 为关注列表 2: 粉丝列表
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
                                switch (refresh_or_load) {
                                    case 0:
                                        smartRefreshLayout.finishRefresh();
                                        handlerDataForFollowList(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerMoreDataForFollowList(jsonObjectData);
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

    private void handlerMoreDataForFollowList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArrayFriendship = jsonObjectData.optJSONArray("friendship_list");
            if (EmptyUtils.isNotEmpty(jsonArrayFriendship)) {
                Gson gson = new Gson();
                ArrayList<FriendshipBean> friendshipBeanArrayList = gson.fromJson(jsonArrayFriendship.toString(), new TypeToken<ArrayList<FriendshipBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(friendshipBeanArrayList) && friendshipBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter.addDatas(friendshipBeanArrayList);
                } else {
                    ToastUtils.showShort("没有更多了");
                }
            }
        }
    }

    private void handlerDataForFollowList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArrayFriendship = jsonObjectData.optJSONArray("friendship_list");
            if (EmptyUtils.isNotEmpty(jsonArrayFriendship)) {
                Gson gson = new Gson();
                ArrayList<FriendshipBean> friendshipBeanArrayList = gson.fromJson(jsonArrayFriendship.toString(), new TypeToken<ArrayList<FriendshipBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(friendshipBeanArrayList) && friendshipBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, friendshipBeanArrayList, FOLLOW_AND_FAN_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {

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
}
