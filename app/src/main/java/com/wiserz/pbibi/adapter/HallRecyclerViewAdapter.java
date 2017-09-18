package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.bean.FeedBean;
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.fragment.AllTopicFragment;
import com.wiserz.pbibi.fragment.TopicDetailFragment;
import com.wiserz.pbibi.util.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/8/14 10:45.
 * QQ : 971060378
 * Used as : 大厅的适配器
 */
public class HallRecyclerViewAdapter extends RecyclerView.Adapter implements BaseRecyclerViewAdapter.OnItemClickListener {

    private Context mContext;
    private JSONObject jsonObjectData;

    private int currentType = RECOMMEND_TOPIC;

    private static final int MY_STATE_DATA_TYPE = 25;

    private static final int RECOMMEND_TOPIC = 0;//推荐的话题
    private static final int MY_TOPIC = 1;//我加入的话题
    private static final int HOT_WEEK = 2;//本周最热

    private static final int RECOMMEND_TOPIC_DATA_TYPE = 88;
    private static final int MY_TOPIC_DATA_TYPE = 98;

    public HallRecyclerViewAdapter(Context mContext, JSONObject jsonObjectData) {
        this.mContext = mContext;
        this.jsonObjectData = jsonObjectData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == RECOMMEND_TOPIC) {
            viewHolder = new RecommendTopicViewHolder(View.inflate(mContext, R.layout.item_recommend_topic, null));
        } else if (viewType == MY_TOPIC) {
            viewHolder = new MyJoinTopicViewHolder(View.inflate(mContext, R.layout.item_my_topic, null));
        } else if (viewType == HOT_WEEK) {
            viewHolder = new HotWeekViewHolder(View.inflate(mContext, R.layout.item_hot_week, null));
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (currentType == RECOMMEND_TOPIC) {
            RecommendTopicViewHolder recommendTopicViewHolder = (RecommendTopicViewHolder) holder;
            recommendTopicViewHolder.tv_check_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(AllTopicFragment.class, null);
                }
            });

            ArrayList<TopicInfoBean> topicInfoBeanArrayList = getRecommendTopicData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList) && topicInfoBeanArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, topicInfoBeanArrayList, RECOMMEND_TOPIC_DATA_TYPE);
                recommendTopicViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                recommendTopicViewHolder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        } else if (currentType == MY_TOPIC) {
            MyJoinTopicViewHolder myJoinTopicViewHolder = (MyJoinTopicViewHolder) holder;
            myJoinTopicViewHolder.tv_check_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(AllTopicFragment.class, null);
                }
            });

            ArrayList<TopicInfoBean> topicInfoBeanArrayList = getJoinTopicData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList) && topicInfoBeanArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, topicInfoBeanArrayList, MY_TOPIC_DATA_TYPE);
                myJoinTopicViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                myJoinTopicViewHolder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        } else if (currentType == HOT_WEEK) {
            HotWeekViewHolder hotWeekViewHolder = (HotWeekViewHolder) holder;

            ArrayList<FeedBean> feedBeanArrayList = getHotWeekData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(feedBeanArrayList) && feedBeanArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, feedBeanArrayList, MY_STATE_DATA_TYPE);
                hotWeekViewHolder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                hotWeekViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case RECOMMEND_TOPIC:
                currentType = RECOMMEND_TOPIC;
                break;
            case MY_TOPIC:
                currentType = MY_TOPIC;
                break;
            case HOT_WEEK:
                currentType = HOT_WEEK;
                break;
            default:
                break;
        }
        return currentType;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("TopicInfoBean")) {
            TopicInfoBean topicInfoBean = (TopicInfoBean) data;
            if (EmptyUtils.isNotEmpty(topicInfoBean)) {
                int theme_id = topicInfoBean.getId();
                if (EmptyUtils.isNotEmpty(theme_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.THEME_ID, theme_id);
                    ((BaseActivity) mContext).gotoPager(TopicDetailFragment.class, bundle);
                }
            }
        } else if (data.getClass().getSimpleName().equals("FeedBean")) {
            LogUtils.e("FeedBean");
        }
    }

    private class RecommendTopicViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_check_all;
        private RecyclerView recyclerView;

        RecommendTopicViewHolder(View itemView) {
            super(itemView);
            tv_check_all = (TextView) itemView.findViewById(R.id.tv_check_all);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        }
    }

    private class MyJoinTopicViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_check_all;
        private RecyclerView recyclerView;

        MyJoinTopicViewHolder(View itemView) {
            super(itemView);
            tv_check_all = (TextView) itemView.findViewById(R.id.tv_check_all);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        }
    }

    private class HotWeekViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;

        HotWeekViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        }
    }

    private ArrayList<TopicInfoBean> getRecommendTopicData(JSONObject jsonObjectData) {
        ArrayList<TopicInfoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONArray("theme_recommend");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TopicInfoBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<TopicInfoBean> getJoinTopicData(JSONObject jsonObjectData) {
        ArrayList<TopicInfoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONArray("theme_join");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TopicInfoBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<FeedBean> getHotWeekData(JSONObject jsonObjectData) {
        ArrayList<FeedBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONObject("feed_list").optJSONArray("feed_list");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FeedBean>>() {
            }.getType());
        }
        return list;
    }
}
