package com.wiserz.pbibi.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/11 14:08.
 * QQ : 971060378
 * Used as : 我喜欢的车辆
 */
public class MyLikeCarFragment extends BaseFragment {

    private int mPage;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_like_car;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPage = 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        getLikeCarFromNet();
    }

    private void getLikeCarFromNet() {
        OkHttpUtils.post()
                .url(Constant.getLikeCarListUrl())
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
                        LogUtils.e(response);
                    }
                });
    }
}
