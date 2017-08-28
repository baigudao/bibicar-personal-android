package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.wiserz.pbibi.bean.ThemeUserBean;
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/28 14:18.
 * QQ : 971060378
 * Used as : 话题详情的页面
 */
public class TopicDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private int mPage;
    private int theme_id;

    private Button btn_join_topic;

    private static final int THEME_USER_DATA_TYPE = 22;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_detail;
    }

    @Override
    protected void initView(View view) {
        theme_id = getArguments().getInt(Constant.THEME_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("话题详情");

        btn_join_topic = (Button) view.findViewById(R.id.btn_join_topic);

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
                .url(Constant.getTopicDetailUrl())
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
                                int is_join = jsonObjectData.optInt("is_join");
                                resetButtonView(is_join);
                                handlerThemeInfoData(jsonObjectData);
                                handlerThemeUserData(jsonObjectData);
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

    private void resetButtonView(int is_join) {//是否加入(1:已加入话题 0:未加入)
        if (EmptyUtils.isNotEmpty(is_join)) {
            switch (is_join) {
                case 0:
                    btn_join_topic.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    btn_join_topic.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    private void handlerThemeInfoData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            Gson gson = new Gson();
            JSONObject jsonObjectForThemeInfo = jsonObjectData.optJSONObject("theme_info");
            TopicInfoBean topicInfoBean = gson.fromJson(jsonObjectForThemeInfo.toString(), TopicInfoBean.class);

            if (EmptyUtils.isNotEmpty(topicInfoBean) && getView() != null) {
                Glide.with(mContext)
                        .load(topicInfoBean.getPost_file())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .into((ImageView) getView().findViewById(R.id.iv_topic_image));
                ((TextView) getView().findViewById(R.id.tv_topic_info)).setText(topicInfoBean.getTitle());
            }
        }
    }

    private void handlerThemeUserData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObjectForThemeUser = jsonObjectData.optJSONObject("theme_user");
            if (EmptyUtils.isNotEmpty(jsonObjectForThemeUser) && getView() != null) {
                int total = jsonObjectForThemeUser.optInt("total");
                ((TextView) getView().findViewById(R.id.tv_join_num)).setText(total + "参与");

                JSONArray jsonArrayForThemeUser = jsonObjectForThemeUser.optJSONArray("users");
                Gson gson = new Gson();
                ArrayList<ThemeUserBean> themeUserBeanArrayList = gson.fromJson(jsonArrayForThemeUser.toString(), new TypeToken<ArrayList<ThemeUserBean>>() {
                }.getType());

                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, themeUserBeanArrayList, THEME_USER_DATA_TYPE);//不能超过5个用户，要不就挤爆了。
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("ThemeUserBean")) {
            ThemeUserBean themeUserBean = (ThemeUserBean) data;
            ToastUtils.showShort(themeUserBean.getNickname());
        }
    }
}
