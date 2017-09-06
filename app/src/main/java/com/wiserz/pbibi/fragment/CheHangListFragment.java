package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CheHangUserListBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/15 13:21.
 * QQ : 971060378
 * Used as : 车行列表的页面
 */
public class CheHangListFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private static final int CHE_HANG_LIST_DATA_TYPE = 14;
    private int mPage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_che_hang;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("车行列表");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

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
        OkHttpUtils.post()
                .url(Constant.getCheHangListUrl())
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
                                handlerCheHangListData(jsonObjectData);
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

    private void handlerCheHangListData(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONObject("list").optJSONArray("user_list");
        ArrayList<CheHangUserListBean> cheHangUserListBeanArrayList = new ArrayList<>();
        Gson gson = new Gson();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectForUserList = jsonArray.optJSONObject(i);
                CheHangUserListBean cheHangUserListBean = gson.fromJson(jsonObjectForUserList.toString(), CheHangUserListBean.class);
                cheHangUserListBeanArrayList.add(cheHangUserListBean);
            }
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, cheHangUserListBeanArrayList, CHE_HANG_LIST_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CheHangUserListBean")) {
            CheHangUserListBean cheHangUserListBean = (CheHangUserListBean) data;
            ToastUtils.showShort(cheHangUserListBean.getNickname());
        }
    }
}
