package com.wiserz.pbibi.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by huangzhifeng on 2017/11/27.
 */

public class FeedbackFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_back;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("意见反馈");
        view.findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tv_submit:
                String feedBack=((EditText) getView().findViewById(R.id.etFeedback)).getText().toString();
                if(TextUtils.isEmpty(feedBack)){
                    ToastUtils.showShort("请输入您的问题或建议");
                    return;
                }
                submitFeedBack(feedBack);
                break;
        }
    }

    private void submitFeedBack(String description){
        OkHttpUtils.post()
                .url(Constant.getSuggestUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.DESCRIPTION, description)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaaaaaaa","response: "+response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONArray jsonObjectData = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                ToastUtils.showShort("反馈成功");
                                goBack();
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
