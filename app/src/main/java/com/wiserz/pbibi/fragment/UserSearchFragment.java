package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.UserBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/18 10:47.
 * QQ : 971060378
 * Used as : 用户搜索的Fragment
 */
public class UserSearchFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView user_search_recycler_view;
    private int mPage;

    private static final int USER_SEARCH_DATA_TYPE = 12;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_search;
    }

    @Override
    protected void initView(View view) {
        user_search_recycler_view = (RecyclerView) view.findViewById(R.id.user_search_recycler_view);
        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet("峰");
    }

    private void getDataFromNet(String keyword) {
        OkHttpUtils.post()
                .url(Constant.getUserSearchUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.NICKNAME, keyword == null ? "" : keyword)
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
                                handlerUserData(jsonObjectData);
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

    private void handlerUserData(JSONObject jsonObjectData) {
        ArrayList<UserBean> userBeanArrayList = new ArrayList<>();
        JSONArray jsonArray = jsonObjectData.optJSONArray("list");
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                UserBean userBean = gson.fromJson(jsonObject.toString(), UserBean.class);
                userBeanArrayList.add(userBean);
            }

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, userBeanArrayList, USER_SEARCH_DATA_TYPE);
            user_search_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            user_search_recycler_view.setAdapter(baseRecyclerViewAdapter);
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("UserBean")) {
            UserBean userBean = (UserBean) data;
            ToastUtils.showShort(userBean.getNickname());
        }
    }
}
