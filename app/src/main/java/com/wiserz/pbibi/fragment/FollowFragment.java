package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.FollowInfoBean;
import com.wiserz.pbibi.bean.RecommendUserInfoBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

/**
 * Created by jackie on 2017/8/14 10:24.
 * QQ : 971060378
 * Used as : 关注的页面
 */
public class FollowFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadmoreListener {

    private LinearLayout ll_recommend_follow;
    private RecyclerView recommend_recyclerView;
    private RecyclerView my_follow_recyclerView;
    private int mPage;

    private int currentType;

    private static final int PUBLISH_CAR = 1;
    private static final int JOIN_TOPIC = 2;
    private static final int FOLLOW_USER = 3;
    private static final int ARTICLE_COMMENT = 4;
    private static final int VIDEO_COMMENT = 5;

    private SmartRefreshLayout smartRefreshLayout;
    private int refresh_or_load;//0或1

    private static final int RECOMMEND_USER_DATA_TYPE = 34;

    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void initView(View view) {
        ll_recommend_follow = (LinearLayout) view.findViewById(R.id.ll_recommend_follow);
        if (SPUtils.getInstance().getBoolean("is_close_recommend_follow")) {
            ll_recommend_follow.setVisibility(View.GONE);
        }
        recommend_recyclerView = (RecyclerView) view.findViewById(R.id.recommend_recyclerView);
        view.findViewById(R.id.iv_close).setOnClickListener(this);

        my_follow_recyclerView = (RecyclerView) view.findViewById(R.id.my_follow_recyclerView);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        mPage = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                SPUtils.getInstance().put("is_close_recommend_follow", true);
                ll_recommend_follow.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        if(!CommonUtil.isHadLogin()) {
            return;
        }
        OkHttpUtils.post()
                .url(Constant.getMyFocusUrl())
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
                                switch (refresh_or_load) {
                                    case 0:
                                        smartRefreshLayout.finishRefresh();
                                        handlerDataForRecommend(jsonObjectData);
                                        handlerDataForMyFollow(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerMoreDataForMyFollow(jsonObjectData);
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

    private void handlerDataForRecommend(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("recomment_user");

            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {

                ArrayList<RecommendUserInfoBean> recommendUserInfoBeanArrayList = new ArrayList<>();
                Gson gson = new Gson();
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i).optJSONObject("user_info");
                    if (EmptyUtils.isNotEmpty(jsonObject)) {
                        RecommendUserInfoBean recommendUserInfoBean = gson.fromJson(jsonObject.toString(), RecommendUserInfoBean.class);
                        recommendUserInfoBeanArrayList.add(recommendUserInfoBean);
                    }
                }

                if (EmptyUtils.isNotEmpty(recommendUserInfoBeanArrayList) && recommendUserInfoBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, recommendUserInfoBeanArrayList, RECOMMEND_USER_DATA_TYPE);
                    recommend_recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recommend_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("RecommendUserInfoBean")) {
            LogUtils.e("点击事件");
        }
    }

    private void handlerMoreDataForMyFollow(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");

            if (jsonArray.length() == 0) {
                ToastUtils.showShort("没有更多了");
                return;
            }

            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                ArrayList<FollowInfoBean> followInfoBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FollowInfoBean>>() {
                }.getType());
                myRecyclerViewAdapter.addDatas(followInfoBeanArrayList, jsonArray);
            }
        }
    }

    private void handlerDataForMyFollow(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");

            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                ArrayList<FollowInfoBean> followInfoBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FollowInfoBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(followInfoBeanArrayList) && followInfoBeanArrayList.size() != 0) {
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(mContext, followInfoBeanArrayList, jsonArray);

                    my_follow_recyclerView.setAdapter(myRecyclerViewAdapter);
                    my_follow_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    //适配器
    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<FollowInfoBean> followInfoBeanArrayList;
        private JSONArray jsonArray;

        public JSONArray getJsonArray() {
            return jsonArray;
        }

        public void setJsonArray(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public MyRecyclerViewAdapter(Context mContext, ArrayList<FollowInfoBean> followInfoBeanArrayList, JSONArray jsonArray) {
            this.mContext = mContext;
            this.followInfoBeanArrayList = followInfoBeanArrayList;
            this.jsonArray = jsonArray;
        }

        /**
         * 添加数据集合
         *
         * @param dataList
         */
        public void addDatas(ArrayList<FollowInfoBean> dataList, JSONArray jsonArray) {
            if (followInfoBeanArrayList == null) {
                followInfoBeanArrayList = new ArrayList<>();
            }
            followInfoBeanArrayList.addAll(dataList);

            String jsonArrayNew = getNewJsonArray(getJsonArray().toString(), jsonArray.toString());

            //            JSONArray JSONArrayNew = null;
            //            try {
            //                JSONArrayNew = new JSONArray(jsonArrayNew);
            //                setJsonArray(JSONArrayNew);
            //            } catch (JSONException e) {
            //                e.printStackTrace();
            //            }
            //            notifyDataSetChanged();
        }

        private String getNewJsonArray(String s, String s1) {
            return s.substring(0, s.lastIndexOf("]")) + s1.substring(1);
        }


        @Override
        public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            switch (viewType) {
                case PUBLISH_CAR:
                    itemView = View.inflate(mContext, R.layout.item_publish_car, null);//车辆发布
                    break;
                case JOIN_TOPIC:
                    itemView = View.inflate(mContext, R.layout.item_join_topic, null);//加入话题
                    break;
                case FOLLOW_USER:
                    itemView = View.inflate(mContext, R.layout.item_follow_user, null);//关注用户
                    break;
                case ARTICLE_COMMENT:
                    itemView = View.inflate(mContext, R.layout.item_article_comment_follow, null);//文章评论
                    break;
                case VIDEO_COMMENT:
                    itemView = View.inflate(mContext, R.layout.item_video_comment_follow, null);//视频评论
                    break;
                default:
                    break;
            }
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
            if (EmptyUtils.isNotEmpty(followInfoBeanArrayList) && followInfoBeanArrayList.size() != 0) {
                FollowInfoBean followInfoBean = followInfoBeanArrayList.get(position);
                if (EmptyUtils.isEmpty(jsonArray)) {
                    this.jsonArray = getJsonArray();
                }
                JSONObject jsonObject = this.jsonArray.optJSONObject(position).optJSONObject("type_info");
                if (EmptyUtils.isNotEmpty(followInfoBean)) {
                    Glide.with(mContext)
                            .load(followInfoBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(followInfoBean.getNickname());
                    holder.tv_item3.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.valueOf(followInfoBean.getCreated_at()) * 1000));
                    switch (currentType) {
                        case PUBLISH_CAR:
                            holder.tv_item2.setText("发布了售车");

                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                Glide.with(mContext)
                                        .load(jsonObject.optString("file_img"))
                                        .placeholder(R.drawable.default_bg_ratio_1)
                                        .error(R.drawable.default_bg_ratio_1)
                                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                                        .into(holder.iv_item2);
                                holder.tv_item4.setText(jsonObject.optString("car_intro"));
                                holder.tv_item5.setText(jsonObject.optString("car_name"));
                                holder.tv_item6.setText(jsonObject.optString("price") + "万");
                            }
                            break;
                        case JOIN_TOPIC:
                            holder.tv_item2.setText("加入了新话题");

                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                Glide.with(mContext)
                                        .load(jsonObject.optString("avatar"))
                                        .placeholder(R.drawable.default_bg_ratio_1)
                                        .error(R.drawable.default_bg_ratio_1)
                                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                                        .into(holder.iv_item2);
                                holder.tv_item4.setText(jsonObject.optString("theme"));
                                holder.tv_item5.setText(jsonObject.optString("user_num") + "人参与丨" + jsonObject.optString("feed_num") + "内容");//35人参与丨155内容
                            }
                            break;
                        case FOLLOW_USER:
                            holder.tv_item2.setText("关注了用户");

                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                Glide.with(mContext)
                                        .load(jsonObject.optString("avatar"))
                                        .placeholder(R.drawable.user_photo)
                                        .error(R.drawable.user_photo)
                                        .into(holder.iv_item2);
                                holder.tv_item4.setText(jsonObject.optString("nickname"));
                                holder.tv_item5.setText(jsonObject.optString("signature"));
                            }
                            break;
                        case ARTICLE_COMMENT:
                            holder.tv_item2.setText("评论了文章");

                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                Glide.with(mContext)
                                        .load(jsonObject.optString("image_url"))
                                        .placeholder(R.drawable.default_bg_ratio_1)
                                        .error(R.drawable.default_bg_ratio_1)
                                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                                        .into(holder.iv_item2);
                                holder.tv_item4.setText(jsonObject.optJSONObject("comment_info").optString("content"));
                                holder.tv_item5.setText(jsonObject.optString("post_content"));
                                //                                holder.tv_item6.setText(jsonObject.optString(""));
                            }
                            break;
                        case VIDEO_COMMENT:
                            holder.tv_item2.setText("评论了视频");

                            if (EmptyUtils.isNotEmpty(jsonObject)) {
                                Glide.with(mContext)
                                        .load(jsonObject.optString("image_url"))
                                        .placeholder(R.drawable.default_bg_ratio_1)
                                        .error(R.drawable.default_bg_ratio_1)
                                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                                        .into(holder.iv_item2);
                                holder.tv_item4.setText(jsonObject.optJSONObject("comment_info").optString("content"));
                                holder.tv_item5.setText(jsonObject.optString("title"));
                                holder.tv_item6.setText(jsonObject.optJSONObject("post_user_info").optJSONObject("profile").optString("nickname"));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return followInfoBeanArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            FollowInfoBean followInfoBean = followInfoBeanArrayList.get(position);
            int type = followInfoBean.getType();//类型 1:车辆发布 2:加入话题 3:关注用户 4:文章评论  5:视频评论
            if (EmptyUtils.isNotEmpty(type)) {
                switch (type) {
                    case 1:
                        currentType = PUBLISH_CAR;
                        break;
                    case 2:
                        currentType = JOIN_TOPIC;
                        break;
                    case 3:
                        currentType = FOLLOW_USER;
                        break;
                    case 4:
                        currentType = ARTICLE_COMMENT;
                        break;
                    case 5:
                        currentType = VIDEO_COMMENT;
                        break;
                    default:
                        break;
                }
            }
            return currentType;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iv_item1;
            private ImageView iv_item2;

            private TextView tv_item1;
            private TextView tv_item2;
            private TextView tv_item3;
            private TextView tv_item4;
            private TextView tv_item5;
            private TextView tv_item6;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_user_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_follow_type);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_time);
                switch (currentType) {
                    case PUBLISH_CAR:
                        tv_item4 = (TextView) itemView.findViewById(R.id.tv_post_content);
                        iv_item2 = (ImageView) itemView.findViewById(R.id.iv_car_image);
                        tv_item5 = (TextView) itemView.findViewById(R.id.tv_car_name);
                        tv_item6 = (TextView) itemView.findViewById(R.id.tv_car_price);
                        break;
                    case JOIN_TOPIC:
                        iv_item2 = (ImageView) itemView.findViewById(R.id.iv_image_topic);
                        tv_item4 = (TextView) itemView.findViewById(R.id.tv_topic_name);
                        tv_item5 = (TextView) itemView.findViewById(R.id.tv_user_and_content_num);
                        break;
                    case FOLLOW_USER:
                        iv_item2 = (ImageView) itemView.findViewById(R.id.iv_user_image);
                        tv_item4 = (TextView) itemView.findViewById(R.id.tv_user_name_inner);
                        tv_item5 = (TextView) itemView.findViewById(R.id.tv_user_content);
                        break;
                    case ARTICLE_COMMENT:
                        tv_item4 = (TextView) itemView.findViewById(R.id.tv_post_content);
                        iv_item2 = (ImageView) itemView.findViewById(R.id.iv_article_image);
                        tv_item5 = (TextView) itemView.findViewById(R.id.tv_article_name);
                        tv_item6 = (TextView) itemView.findViewById(R.id.tv_article_content);
                        break;
                    case VIDEO_COMMENT:
                        tv_item4 = (TextView) itemView.findViewById(R.id.tv_post_content);
                        iv_item2 = (ImageView) itemView.findViewById(R.id.iv_video_image);
                        tv_item5 = (TextView) itemView.findViewById(R.id.tv_video_name);
                        tv_item6 = (TextView) itemView.findViewById(R.id.tv_video_content);
                        break;
                    default:
                        break;
                }
            }
        }
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