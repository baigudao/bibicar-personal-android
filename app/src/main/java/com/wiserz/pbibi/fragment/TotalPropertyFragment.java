package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.RichListRecyclerViewAdapter;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 17:38.
 * QQ : 971060378
 * Used as : 总资产的页面
 */
public class TotalPropertyFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_total_property;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.tv_title).setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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
                .url(Constant.getMyRichListUrl())
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
                                RichListRecyclerViewAdapter richListRecyclerViewAdapter = new RichListRecyclerViewAdapter(mContext, jsonObjectData);
                                recyclerView.setAdapter(richListRecyclerViewAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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
