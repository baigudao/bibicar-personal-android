package com.wiserz.pbibi.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.UserInfoForSalesConsultant;
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
 * Created by jackie on 2017/8/28 11:49.
 * QQ : 971060378
 * Used as : 销售顾问的页面
 */
public class SalesConsultantFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int userId;
    private int mPageNo;

    private RecyclerView recycler_view_sales_consultant;

    private static final int SALES_CONSULTANT_DATA_TYPE = 39;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sales_consultant;
    }

    @Override
    protected void initView(View view) {
        userId = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        recycler_view_sales_consultant = (RecyclerView) view.findViewById(R.id.recycler_view_sales_consultant);

        mPageNo = 0;
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
                .url(Constant.getSalesConsultantUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.COMPANY_ID, String.valueOf(userId))
                .addParams(Constant.PAGE, String.valueOf(mPageNo))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
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
                                JSONArray jsonArray = jsonObjectData.optJSONObject("list").optJSONArray("user_list");
                                handlerData(jsonArray);
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

    private void handlerData(JSONArray jsonArray) {
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            Gson gson = new Gson();
            ArrayList<UserInfoForSalesConsultant> userInfoForSalesConsultantArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<UserInfoForSalesConsultant>>() {
            }.getType());
            if (EmptyUtils.isNotEmpty(userInfoForSalesConsultantArrayList) && userInfoForSalesConsultantArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext,userInfoForSalesConsultantArrayList,SALES_CONSULTANT_DATA_TYPE);
                recycler_view_sales_consultant.setAdapter(baseRecyclerViewAdapter);
                recycler_view_sales_consultant.setLayoutManager(new GridLayoutManager(mContext,2, LinearLayoutManager.VERTICAL,false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("UserInfoForSalesConsultant")){
            LogUtils.e("点击事件");
        }
    }
}
