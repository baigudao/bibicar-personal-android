package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.fragment.AllTopicFragment;

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
            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
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
                    ToastUtils.showShort("全部话题");
                }
            });

            //            ArrayList<FuLiBean> fuLiBeanArrayList = getMyJoinTopicData(jsonObjectData);
            //            ArrayList<FuLiBean> fuLiBeanArrayList1 = new ArrayList<>();
            //            fuLiBeanArrayList1.add(fuLiBeanArrayList.get(0));
            //            fuLiBeanArrayList1.add(fuLiBeanArrayList.get(1));
            //            fuLiBeanArrayList1.add(fuLiBeanArrayList.get(2));
            //            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
            //                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, fuLiBeanArrayList1, MY_TOPIC_DATA_TYPE);
            //                myJoinTopicViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            //                myJoinTopicViewHolder.recyclerView.setAdapter(baseRecyclerViewAdapter);
            //                baseRecyclerViewAdapter.setOnItemClickListener(this);
            //            }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
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
                currentType = RECOMMEND_TOPIC;
                break;
        }
        return currentType;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("TopicInfoBean")) {
            TopicInfoBean topicInfoBean = (TopicInfoBean) data;
            ToastUtils.showShort(topicInfoBean.getTheme());
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

    //    private ArrayList<FuLiBean> getMyJoinTopicData(JSONObject jsonObjectData) {
    //        ArrayList<FuLiBean> list = null;
    //        if (jsonObjectData == null) {
    //            return new ArrayList<>();
    //        } else {
    //            JSONArray jsonArray = jsonObjectData.optJSONArray("results");
    //            Gson gson = new Gson();
    //            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FuLiBean>>() {
    //            }.getType());
    //        }
    //        return list;
    //    }
}
