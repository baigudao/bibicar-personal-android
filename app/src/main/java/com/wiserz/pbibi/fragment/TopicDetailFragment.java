package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import java.util.List;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/28 14:18.
 * QQ : 971060378
 * Used as : 话题详情的页面
 */
public class TopicDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private List<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

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
        btn_join_topic.setOnClickListener(this);

        mPage = 0;

        //初始化Fragment
        initFragment();
        RadioGroup mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_most_hot);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new MostNewTopicFragment());//最新话题
        mBaseFragment.add(new MostHotTopicFragment());//最热话题
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_join_topic:
                joinTopic();
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
                ((TextView) getView().findViewById(R.id.tv_join_num)).setText(total + "人参与");

                getView().findViewById(R.id.rl_topic_join).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (EmptyUtils.isNotEmpty(theme_id)){
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.THEME_ID,theme_id);
                            gotoPager(TopicMemberFragment.class, bundle);
                        }
                    }
                });

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

    private void joinTopic() {
        OkHttpUtils.post()
                .url(Constant.getJoinTopicUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.THEME_ID, String.valueOf(theme_id))
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
                                goBack();
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


    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("ThemeUserBean")) {
            ThemeUserBean themeUserBean = (ThemeUserBean) data;
            ToastUtils.showShort(themeUserBean.getNickname());
        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_most_hot://最热
                    position = 0;
                    break;
                case R.id.rb_most_new://最新
                    position = 1;
                    break;
                default:
                    position = 0;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment toFragment = getFragment();
            //切换Fragment
            switchFragment(fromFragment, toFragment);
        }
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_topic_content, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }
}
