package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/28 11:49.
 * QQ : 971060378
 * Used as : 销售顾问的页面
 */
public class SalesConsultantFragment extends BaseFragment {

    private int userId;
    private int mPageNo;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sales_consultant;
    }

    @Override
    protected void initView(View view) {
        userId = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
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
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                //                                Gson gson = new Gson();
                                //                                salesConsultantBean = gson.fromJson(dataJsonObject.toString(), SalesConsultantBean.class);
                                //
                                //                                //准备好数据后
                                //                                recycler_view_sales_consultant.setAdapter(new MyRecyclerViewAdapterForSalesConsultant());
                                //                                recycler_view_sales_consultant.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
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
