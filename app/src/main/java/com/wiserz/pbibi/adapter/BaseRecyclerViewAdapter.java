package com.wiserz.pbibi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.bean.AllSecondCommentBean;
import com.wiserz.pbibi.bean.ArticleBean;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.CarModelBean;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.bean.CarRentRecommendCarBean;
import com.wiserz.pbibi.bean.CarSeriesBean;
import com.wiserz.pbibi.bean.CheHangHomeBean;
import com.wiserz.pbibi.bean.CheHangUserListBean;
import com.wiserz.pbibi.bean.DreamCarBean;
import com.wiserz.pbibi.bean.FeedBean;
import com.wiserz.pbibi.bean.GuaranteeHistoryBean;
import com.wiserz.pbibi.bean.MyCarRentOrderBean;
import com.wiserz.pbibi.bean.PeccancyHistoryBean;
import com.wiserz.pbibi.bean.PeccancyRecordBean;
import com.wiserz.pbibi.bean.RecommendUserInfoBean;
import com.wiserz.pbibi.bean.ThemeUserBean;
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.bean.UserBean;
import com.wiserz.pbibi.bean.UserInfoForSalesConsultant;
import com.wiserz.pbibi.bean.VideoBean;
import com.wiserz.pbibi.fragment.CommentDetailFragment;
import com.wiserz.pbibi.fragment.OtherHomePageFragment;
import com.wiserz.pbibi.fragment.ShowAllImageFragment;
import com.wiserz.pbibi.fragment.StateDetailFragment;
import com.wiserz.pbibi.fragment.VideoDetailFragment;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

import static com.wiserz.pbibi.R.id.tvCarColor;

/**
 * Created by jackie on 2017/8/10 14:07.
 * QQ : 971060378
 * Used as : 基本的RecyclerView的适配器
 */
public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<T> mList;
    private int dataType;

    private static final int USER_SEARCH_DATA_TYPE = 12;

    private static final int MY_CAR_RENT_ORDER_DATA_TYPE = 13;

    private static final int CHE_HANG_LIST_DATA_TYPE = 14;

    private static final int CHE_HANG_LIST_ITEM_DATA_TYPE = 15;

    private static final int CAR_RENT_RECOMMEND_DATA_TYPE = 16;

    private static final int CAR_SURFACE_DATA_TYPE = 17;

    private static final int ARTICLE_COMMENT_LIST_DATA_TYPE = 18;

    private static final int ARTICLE_COMMENT_REPLY_DATA_TYPE = 19;

    private static final int COMMENT_DETAIL_DATA_TYPE = 20;

    private static final int SELLING_CAR_DATA_TYPE = 21;//车行中的在售和已售车辆列表

    private static final int THEME_USER_DATA_TYPE = 22;

    private static final int ALL_TOPIC_DATA_TYPE = 23;

    private static final int MY_CAR_REPERTORY_DATA_TYPE = 24;

    private static final int MY_STATE_DATA_TYPE = 25;

    private static final int DREAM_CAR_DATA_TYPE = 26;

    private static final int MY_STATE_INNER_DATA_TYPE = 27;

    private static final int SAME_STYLE_CAR_DATA_TYPE = 28;

    private static final int CAR_INSIDE_DATA_TYPE = 29;

    private static final int CAR_STRUCTURE_DATA_TYPE = 30;

    private static final int CAR_MORE_DETAIL_DATA_TYPE = 31;

    private static final int TOPIC_MEMBER_DATA_TYPE = 32;

    private static final int SELECT_CAR_COLOR_DATA_TYPE = 33;

    private static final int RECOMMEND_USER_DATA_TYPE = 34;

    private static final int PECCANCY_RECORD_DATA_TYPE = 35;

    private static final int HPHM_DATA_TYPE = 36;

    private static final int GUARANTEE_HISTORY_DATA_TYPE = 37;

    private static final int SELECT_TOPIC_DATA_TYPE = 38;

    private static final int SALES_CONSULTANT_DATA_TYPE = 39;

    private static final int CAR_SERIES_DATA_TYPE = 40;

    private static final int CAR_MODEL_DATA_TYPE = 41;

    private static final int NEW_CAR_DATA_TYPE = 55;

    private static final int CAR_VIDEO_DATA_TYPE = 65;

    private static final int CHE_HANG_HOME_DATA_TYPE = 75;

    private static final int ARTICLE_LIST_DATA_TYPE = 76;

    private static final int CAR_RENT_DATA_TYPE = 77;

    private static final int RECOMMEND_TOPIC_DATA_TYPE = 88;

    private static final int MY_TOPIC_DATA_TYPE = 98;

    private static final int VIDEO_LIST_DATA_TYPE = 99;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;//汽车中心的汽车列表

    private ArrayList<String> topic_string = new ArrayList<>();

    public BaseRecyclerViewAdapter(Context context, List<T> tList, int dataType) {
        this.mContext = context;
        this.mList = tList;
        this.dataType = dataType;
    }

    /**
     * 添加数据集合
     *
     * @param dataList
     */
    public void addDatas(List<T> dataList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (dataType == CAR_MODEL_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_series, null));
        } else if (dataType == CAR_SERIES_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_series, null));
        } else if (dataType == SALES_CONSULTANT_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_sales_consultant, null));
        } else if (dataType == SELECT_TOPIC_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_select_topic, null));
        } else if (dataType == GUARANTEE_HISTORY_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_guarantee_history, null));
        } else if (dataType == HPHM_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_hphm_image, null));
        } else if (dataType == PECCANCY_RECORD_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_peccancy_record, null));
        } else if (dataType == RECOMMEND_USER_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_recommend_user, null));
        } else if (dataType == SELECT_CAR_COLOR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_select_car_color, null));
        } else if (dataType == TOPIC_MEMBER_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_topic_member, null));
        } else if (dataType == MY_STATE_INNER_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_my_state_inner, null));
        } else if (dataType == CAR_SURFACE_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_all_image, null));
        } else if (dataType == CAR_INSIDE_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_all_image, null));
        } else if (dataType == CAR_MORE_DETAIL_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_all_image, null));
        } else if (dataType == CAR_STRUCTURE_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_all_image, null));
        } else if (dataType == SAME_STYLE_CAR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_same_style_car, null));
        } else if (dataType == DREAM_CAR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_dream_car, null));
        } else if (dataType == MY_STATE_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_my_state, null));
        } else if (dataType == MY_CAR_REPERTORY_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_my_car_repertory, null));
        } else if (dataType == THEME_USER_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_theme_user, null));
        } else if (dataType == SELLING_CAR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_selling_car, null));
        } else if (dataType == COMMENT_DETAIL_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_comment_detail, null));
        } else if (dataType == ARTICLE_COMMENT_REPLY_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_article_comment_reply, null));
        } else if (dataType == ARTICLE_COMMENT_LIST_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_article_comment, null));
        } else if (dataType == NEW_CAR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_new_car, null));
        } else if (dataType == CAR_VIDEO_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_video, null));
        } else if (dataType == CHE_HANG_HOME_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_che_hang, null));
        } else if (dataType == CAR_RENT_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_rent, null));
        } else if (dataType == RECOMMEND_TOPIC_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_recommend_topic_recycler_view, null));
        } else if (dataType == MY_TOPIC_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_my_topic_recycler_view, null));
        } else if (dataType == ALL_TOPIC_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_all_topic, null));
        } else if (dataType == ARTICLE_LIST_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_article_list, null));
        } else if (dataType == VIDEO_LIST_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_video_list, null));
        } else if (dataType == CHE_HANG_LIST_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_che_hang_list, null));
        } else if (dataType == CHE_HANG_LIST_ITEM_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_che_hang_list_recycler_view, null));
        } else if (dataType == CAR_LIST_FOR_CAR_CENTER) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_center_car_list, null));
        } else if (dataType == USER_SEARCH_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_user, null));
        } else if (dataType == MY_CAR_RENT_ORDER_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_my_car_rent_order, null));
        } else if (dataType == CAR_RENT_RECOMMEND_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_car_rent_recommend, null));
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (dataType == NEW_CAR_DATA_TYPE) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList)) {
                CarInfoBean carInfoBean = carInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles_img())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(carInfoBean.getCar_name());
                    holder.tv_item2.setText(String.valueOf(carInfoBean.getPrice()) + " 万");
                }
            }
        } else if (dataType == CAR_VIDEO_DATA_TYPE) {
            ArrayList<VideoBean> videoBeanArrayList = (ArrayList<VideoBean>) mList;
            if (EmptyUtils.isNotEmpty(videoBeanArrayList)) {
                final VideoBean videoBean = videoBeanArrayList.get(position);
                Glide.with(mContext)
                        .load(videoBean.getImage_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        //                        DataManger.getInstance().setData(videoBean);
                        //                        mContext.startActivity(intent);
                        if (EmptyUtils.isNotEmpty(videoBean)) {
                            int feed_id = videoBean.getFeed_id();
                            DataManager.getInstance().setData1(feed_id);
                            ((BaseActivity) mContext).gotoPager(VideoDetailFragment.class, null);
                        }
                    }
                });
                if (videoBean.getPost_content() != null) {
                    holder.tv_item1.setText(videoBean.getPost_content());
                }
            }
        } else if (dataType == CHE_HANG_HOME_DATA_TYPE) {
            ArrayList<CheHangHomeBean> cheHangHomeBeanArrayList = (ArrayList<CheHangHomeBean>) mList;

            if (EmptyUtils.isNotEmpty(cheHangHomeBeanArrayList)) {
                CheHangHomeBean cheHangHomeBean = cheHangHomeBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(cheHangHomeBean)) {
                    if (EmptyUtils.isNotEmpty(cheHangHomeBean.getAvatar())) {
                        Glide.with(mContext)
                                .load(cheHangHomeBean.getAvatar())
                                .placeholder(R.drawable.user_photo)
                                .error(R.drawable.user_photo)
                                .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                                .into(holder.iv_item1);
                    }
                    holder.tv_item1.setText(EmptyUtils.isEmpty(cheHangHomeBean.getNickname()) ? "" : cheHangHomeBean.getNickname());
                    holder.tv_item2.setText(EmptyUtils.isEmpty(cheHangHomeBean.getSignature()) ? "" : cheHangHomeBean.getSignature());
                    if (EmptyUtils.isNotEmpty(cheHangHomeBean.getSaling_num()) && EmptyUtils.isNotEmpty(cheHangHomeBean.getSold_num())) {
                        holder.tv_item3.setText("在售" + cheHangHomeBean.getSaling_num() + "辆丨已售" + cheHangHomeBean.getSold_num() + "辆");
                    }
                }
            }
        } else if (dataType == CAR_RENT_DATA_TYPE) {
            ArrayList<CarRentInfoBean> carRentInfoBeanArrayList = (ArrayList<CarRentInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carRentInfoBeanArrayList)) {
                CarRentInfoBean carRentInfoBean = carRentInfoBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(carRentInfoBean.getFile_img())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);

                int status = carRentInfoBean.getRental_info().getStatus(); //1:可租 2已租
                if (status == 2) {
                    holder.iv_item2.setVisibility(View.INVISIBLE);
                    holder.iv_item3.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_item2.setVisibility(View.VISIBLE);
                    holder.iv_item3.setVisibility(View.INVISIBLE);
                }

                holder.tv_item1.setText(carRentInfoBean.getCar_name());
                holder.tv_item2.setText(carRentInfoBean.getRental_info().getOne() + "/天");
            }
        } else if (dataType == RECOMMEND_TOPIC_DATA_TYPE) {
            ArrayList<TopicInfoBean> topicInfoBeanArrayList = (ArrayList<TopicInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
                TopicInfoBean topicInfoBean = topicInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(topicInfoBean)) {
                    Glide.with(mContext)
                            .load(topicInfoBean.getPost_file())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    //                holder.tv_item1.setText(topicInfoBean.getTheme());
                }
            }
        } else if (dataType == MY_TOPIC_DATA_TYPE) {
            ArrayList<TopicInfoBean> topicInfoBeanArrayList = (ArrayList<TopicInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
                TopicInfoBean topicInfoBean = topicInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(topicInfoBean)) {
                    Glide.with(mContext)
                            .load(topicInfoBean.getPost_file())
                            .placeholder(R.drawable.user_photo)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(topicInfoBean.getTheme());
                    holder.tv_item2.setText(topicInfoBean.getFeed_num() + "参与丨" + topicInfoBean.getIs_skip() + "内容");//35参与丨155内容
                    //                    holder.tv_item3.setText(fuLiBean.getDesc());//12条更新
                }
            }
        } else if (dataType == ALL_TOPIC_DATA_TYPE) {
            ArrayList<TopicInfoBean> topicInfoBeanArrayList = (ArrayList<TopicInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
                TopicInfoBean topicInfoBean = topicInfoBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(topicInfoBean.getPost_file())
                        .placeholder(R.drawable.user_photo)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(topicInfoBean.getTheme());
                holder.tv_item2.setText(topicInfoBean.getTitle());
                holder.tv_item3.setText(topicInfoBean.getFeed_num() + "参与丨" + topicInfoBean.getIs_skip() + "内容");
            }
        } else if (dataType == SELECT_TOPIC_DATA_TYPE) {
            ArrayList<TopicInfoBean> topicInfoBeanArrayList = (ArrayList<TopicInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(topicInfoBeanArrayList)) {
                final TopicInfoBean topicInfoBean = topicInfoBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(topicInfoBean.getPost_file())
                        .placeholder(R.drawable.user_photo)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(topicInfoBean.getTheme());
                holder.tv_item2.setText(topicInfoBean.getTitle());
                holder.tv_item3.setText(topicInfoBean.getFeed_num() + "参与丨" + topicInfoBean.getIs_skip() + "内容");

                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            topic_string.add(topicInfoBean.getTheme());
                        } else {
                            topic_string.remove(topicInfoBean.getTheme());
                        }
                    }
                });
            }
        } else if (dataType == ARTICLE_LIST_DATA_TYPE) {
            ArrayList<ArticleBean> articleBeanArrayList = (ArrayList<ArticleBean>) mList;

            if (EmptyUtils.isNotEmpty(articleBeanArrayList)) {
                ArticleBean articleBean = articleBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(articleBean.getImage_url().get(0))
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(articleBean.getPost_content());
                holder.tv_item2.setText(articleBean.getPost_user_info().getProfile().getNickname());
            }
        } else if (dataType == VIDEO_LIST_DATA_TYPE) {
            ArrayList<VideoBean> videoBeanArrayList = (ArrayList<VideoBean>) mList;

            if (EmptyUtils.isNotEmpty(videoBeanArrayList)) {
                VideoBean videoBean = videoBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(videoBean.getImage_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("开始播放");
                    }
                });
                holder.tv_item1.setText(videoBean.getPost_content());
                holder.tv_item2.setText(videoBean.getPost_user_info().getProfile().getNickname());
                holder.tv_item3.setText(TimeUtils.date2String(new Date(Long.valueOf(videoBean.getCreated()) * 1000), new SimpleDateFormat("yyyy-MM-dd")) + "更新");//time为秒，换算成毫秒
            }
        } else if (dataType == CHE_HANG_LIST_DATA_TYPE) {
            ArrayList<CheHangUserListBean> cheHangUserListBeanArrayList = (ArrayList<CheHangUserListBean>) mList;

            if (EmptyUtils.isNotEmpty(cheHangUserListBeanArrayList)) {
                final CheHangUserListBean cheHangUserListBean = cheHangUserListBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(cheHangUserListBean.getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(cheHangUserListBean.getNickname());
                holder.tv_item2.setText("在售" + cheHangUserListBean.getSaling_num() + "辆");
                holder.tv_item3.setText("已售" + cheHangUserListBean.getSold_num() + "辆");

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort(cheHangUserListBean.getNickname());
                    }
                });

                ArrayList<CheHangUserListBean.CarListBeanX.CarListBean> carListBeanArrayList = (ArrayList<CheHangUserListBean.CarListBeanX.CarListBean>) cheHangUserListBean.getCar_list().getCar_list();
                if (EmptyUtils.isNotEmpty(carListBeanArrayList)) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carListBeanArrayList, CHE_HANG_LIST_ITEM_DATA_TYPE);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    holder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                    baseRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object data, int position) {
                            if (data.getClass().getSimpleName().equals("CarListBean")) {
                                CheHangUserListBean.CarListBeanX.CarListBean carListBean = (CheHangUserListBean.CarListBeanX.CarListBean) data;
                                ToastUtils.showShort(carListBean.getCar_info().getCar_name());
                            }
                        }
                    });
                }

                holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("拨打电话");
                    }
                });
                holder.linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("查看位置");
                    }
                });
            }
        } else if (dataType == CHE_HANG_LIST_ITEM_DATA_TYPE) {
            ArrayList<CheHangUserListBean.CarListBeanX.CarListBean> carListBeanArrayList = (ArrayList<CheHangUserListBean.CarListBeanX.CarListBean>) mList;

            if (EmptyUtils.isNotEmpty(carListBeanArrayList)) {
                CheHangUserListBean.CarListBeanX.CarListBean carListBean = carListBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carListBean.getCar_info().getFiles())) {
                    Glide.with(mContext)
                            .load(carListBean.getCar_info().getFiles().get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                }
                holder.tv_item1.setText(String.valueOf("成交价：" + carListBean.getCar_info().getPrice() + "万"));
            }
        } else if (dataType == CAR_LIST_FOR_CAR_CENTER) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList)) {
                CarInfoBean carInfoBean = carInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles_img())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(carInfoBean.getCar_name());
                    //                    if (carInfoBean.getCar_type() == 0) {
                    holder.tv_item2.setText(mContext.getResources().getString(R.string.car_sales_volume, String.valueOf(carInfoBean.getSales_volume())));
                    //                    } else {
                    //                        holder.tv_item2.setText(mContext.getResources().getString(R.string.time_wan_kilo, carInfoBean.getBoard_time(), String.valueOf(carInfoBean.getMileage())));
                    //                    }
                    holder.tv_item3.setText(mContext.getResources().getString(R.string._wan, String.valueOf(carInfoBean.getPrice())));
                }
            }
        } else if (dataType == USER_SEARCH_DATA_TYPE) {
            ArrayList<UserBean> userBeanArrayList = (ArrayList<UserBean>) mList;

            if (EmptyUtils.isNotEmpty(userBeanArrayList)) {
                UserBean userBean = userBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(userBean.getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into(holder.iv_item1);
                holder.tv_item1.setText(userBean.getNickname());
                holder.tv_item2.setText("粉丝 " + userBean.getFans_num() + " | 关注" + userBean.getFriend_num());
            }
        } else if (dataType == MY_CAR_RENT_ORDER_DATA_TYPE) {
            ArrayList<MyCarRentOrderBean> myCarRentOrderBeanArrayList = (ArrayList<MyCarRentOrderBean>) mList;

            if (EmptyUtils.isNotEmpty(myCarRentOrderBeanArrayList)) {
                MyCarRentOrderBean myCarRentOrderBean = myCarRentOrderBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(myCarRentOrderBean.getCar_info().getFiles().get(0).getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                int status = myCarRentOrderBean.getStatus();//1:待支付 2:支付失败 3:支付成功（待提车） 4:订单失败 5:订单成功
                if (EmptyUtils.isNotEmpty(status)) {
                    switch (status) {
                        case 1:
                            holder.tv_item1.setText("待支付...");
                            break;
                        case 2:
                            holder.tv_item1.setText("支付失败");
                            break;
                        case 3:
                            holder.tv_item1.setText("支付成功（待提车）");
                            break;
                        case 4:
                            holder.tv_item1.setText("订单失败");
                            break;
                        case 5:
                            holder.tv_item1.setText("订单成功");
                            break;
                        default:
                            break;
                    }
                }
                holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(myCarRentOrderBean.getCreated_at()) * 1000), new SimpleDateFormat("yyyy.MM.dd")));//time为秒，换算成毫秒
                holder.tv_item3.setText(myCarRentOrderBean.getCar_info().getCar_name());
                holder.tv_item4.setText(TimeUtils.date2String(new Date(Long.valueOf(myCarRentOrderBean.getRental_time_start()) * 1000), new SimpleDateFormat("yyyy/MM/dd")) + "~" +
                        TimeUtils.date2String(new Date(Long.valueOf(myCarRentOrderBean.getRental_time_end()) * 1000), new SimpleDateFormat("yyyy/MM/dd")));//2017/07/13~2017/08/13
            }
        } else if (dataType == CAR_RENT_RECOMMEND_DATA_TYPE) {
            ArrayList<CarRentRecommendCarBean> carRentRecommendCarBeanArrayList = (ArrayList<CarRentRecommendCarBean>) mList;

            if (EmptyUtils.isNotEmpty(carRentRecommendCarBeanArrayList)) {
                CarRentRecommendCarBean carRentRecommendCarBean = carRentRecommendCarBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(carRentRecommendCarBean.getFiles().getType1().get(0).getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);

                int status = carRentRecommendCarBean.getRental_info().getStatus();//车辆出租状态 1:可租 2已租
                if (EmptyUtils.isNotEmpty(status)) {
                    switch (status) {
                        case 1:
                            holder.iv_item2.setImageResource(R.drawable.b_zu_status3x);
                            break;
                        case 2:
                            holder.iv_item2.setImageResource(R.drawable.g_zu_status3x);
                            break;
                        default:
                            break;
                    }
                }
                holder.tv_item1.setText(carRentRecommendCarBean.getCar_name());
                holder.tv_item2.setText("¥" + String.valueOf(carRentRecommendCarBean.getRental_info().getDeposit()) + "/日均");//¥468/日均
            }
        } else if (dataType == ARTICLE_COMMENT_LIST_DATA_TYPE) {
            ArrayList<ArticleCommentBean> articleCommentBeanArrayList = (ArrayList<ArticleCommentBean>) mList;

            if (EmptyUtils.isNotEmpty(articleCommentBeanArrayList)) {
                final ArticleCommentBean articleCommentBean = articleCommentBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(articleCommentBean)) {
                    Glide.with(mContext)
                            .load(articleCommentBean.getFrom_user().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(articleCommentBean.getFrom_user().getUser_id() + "");
                        }
                    });
                    holder.tv_item1.setText(articleCommentBean.getFrom_user().getNickname());
                    holder.tv_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(articleCommentBean.getFrom_user().getUser_id() + "");
                        }
                    });
                    holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(articleCommentBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

                    holder.tv_item3.setText(String.valueOf(articleCommentBean.getLike_num()));
                    int is_like = articleCommentBean.getIs_like();
                    if (EmptyUtils.isNotEmpty(is_like)) {
                        switch (is_like) {
                            case 1:
                                holder.iv_item2.setImageResource(R.drawable.v1_zan3x);
                                break;
                            case 2:
                                holder.iv_item2.setImageResource(R.drawable.v1_meizan3x);
                                break;
                            default:
                                break;
                        }
                    }
                    holder.linearLayout2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if (msg.what == 555) {
                                        //取消点赞
                                        OkHttpUtils.post()
                                                .url(Constant.getCommentLikeDelete())
                                                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                .addParams(Constant.FEED_ID, String.valueOf(articleCommentBean.getFeed_id()))
                                                .addParams(Constant.COMMENT_ID, String.valueOf(articleCommentBean.getComment_id()))
                                                .build()
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        ToastUtils.showShort("取消点赞失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = new JSONObject(response);
                                                            int status = jsonObject.optInt("status");
                                                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                            if (status == 1) {
                                                                holder.iv_item2.setImageResource(R.drawable.v1_meizan3x);
                                                                int like_num = Integer.valueOf(holder.tv_item3.getText().toString());
                                                                --like_num;
                                                                holder.tv_item3.setText(String.valueOf(like_num));
                                                                ToastUtils.showShort("取消点赞成功");
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
                            };
                            //点赞
                            OkHttpUtils.post()
                                    .url(Constant.getCommentLikeCreate())
                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                    .addParams(Constant.FEED_ID, String.valueOf(articleCommentBean.getFeed_id()))
                                    .addParams(Constant.COMMENT_ID, String.valueOf(articleCommentBean.getComment_id()))
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtils.showShort("点赞失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                int status = jsonObject.optInt("status");
                                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                if (status == 1) {
                                                    holder.iv_item2.setImageResource(R.drawable.v1_zan3x);
                                                    int like_num = Integer.valueOf(holder.tv_item3.getText().toString());
                                                    ++like_num;
                                                    holder.tv_item3.setText(String.valueOf(like_num));
                                                    ToastUtils.showShort("点赞成功");
                                                } else {
                                                    String code = jsonObject.optString("code");
                                                    if (code.equals("67000")) {
                                                        handler.sendEmptyMessage(555);
                                                        return;
                                                    }
                                                    String msg = jsonObjectData.optString("msg");
                                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    });

                    holder.iv_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    holder.tv_item4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    holder.tv_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });

                    if (EmptyUtils.isNotEmpty(articleCommentBean.getHot_list())) {
                        holder.tv_item4.setText(String.valueOf(articleCommentBean.getHot_list().getTotal()));//评论数
                    }

                    holder.tv_item5.setText(articleCommentBean.getComment_content());//评论的内容

                    ArrayList<ArticleCommentBean.HotListBean.ListBean> listBeanList = (ArrayList<ArticleCommentBean.HotListBean.ListBean>) articleCommentBean.getHot_list().getList();
                    if (EmptyUtils.isNotEmpty(listBeanList)) {
                        holder.linearLayout1.setVisibility(View.VISIBLE);
                        int reply_size = listBeanList.size();
                        if (reply_size < 4) {
                            //如果回复数小于4
                            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, listBeanList, ARTICLE_COMMENT_REPLY_DATA_TYPE);
                            holder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                            holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        }

                        holder.tv_item6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //查看更多回复
                                int comment_id = articleCommentBean.getComment_id();
                                int feed_id = articleCommentBean.getFeed_id();
                                if (EmptyUtils.isNotEmpty(comment_id) && EmptyUtils.isNotEmpty(feed_id)) {
                                    DataManager.getInstance().setData1(feed_id);
                                    DataManager.getInstance().setData2(comment_id);
                                    ((BaseActivity) mContext).gotoPager(CommentDetailFragment.class, null);
                                }
                            }
                        });
                    } else {
                        holder.linearLayout1.setVisibility(View.GONE);
                    }
                }
            }
        } else if (dataType == ARTICLE_COMMENT_REPLY_DATA_TYPE) {
            ArrayList<ArticleCommentBean.HotListBean.ListBean> listBeanList = (ArrayList<ArticleCommentBean.HotListBean.ListBean>) mList;

            if (EmptyUtils.isNotEmpty(listBeanList)) {
                ArticleCommentBean.HotListBean.ListBean listBean = listBeanList.get(position);

                holder.tv_item1.setText(listBean.getFrom_user().getNickname() + "：");
                holder.tv_item2.setText(listBean.getComment_content());
            }
        } else if (dataType == COMMENT_DETAIL_DATA_TYPE) {
            ArrayList<AllSecondCommentBean> allSecondCommentBeanArrayList = (ArrayList<AllSecondCommentBean>) mList;

            if (EmptyUtils.isNotEmpty(allSecondCommentBeanArrayList)) {
                final AllSecondCommentBean allSecondCommentBean = allSecondCommentBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(allSecondCommentBean)) {

                    Glide.with(mContext)
                            .load(allSecondCommentBean.getFrom_user().getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(allSecondCommentBean.getFrom_user().getUser_id() + "");
                        }
                    });
                    holder.tv_item1.setText(allSecondCommentBean.getFrom_user().getNickname());
                    holder.tv_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(allSecondCommentBean.getFrom_user().getUser_id() + "");
                        }
                    });
                    holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(allSecondCommentBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));
                    holder.tv_item3.setText(allSecondCommentBean.getComment_content());

                    holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if (msg.what == 555) {
                                        //取消点赞
                                        OkHttpUtils.post()
                                                .url(Constant.getCommentLikeDelete())
                                                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                .addParams(Constant.FEED_ID, String.valueOf(allSecondCommentBean.getFeed_id()))
                                                .addParams(Constant.COMMENT_ID, String.valueOf(allSecondCommentBean.getComment_id()))
                                                .build()
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        ToastUtils.showShort("取消点赞失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = new JSONObject(response);
                                                            int status = jsonObject.optInt("status");
                                                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                            if (status == 1) {
                                                                holder.iv_item2.setImageResource(R.drawable.v1_meizan3x);
                                                                int like_num = Integer.valueOf(holder.tv_item4.getText().toString());
                                                                --like_num;
                                                                holder.tv_item4.setText(String.valueOf(like_num));
                                                                ToastUtils.showShort("取消点赞成功");
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
                            };
                            //点赞
                            OkHttpUtils.post()
                                    .url(Constant.getCommentLikeCreate())
                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                    .addParams(Constant.FEED_ID, String.valueOf(allSecondCommentBean.getFeed_id()))
                                    .addParams(Constant.COMMENT_ID, String.valueOf(allSecondCommentBean.getComment_id()))
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtils.showShort("点赞失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                int status = jsonObject.optInt("status");
                                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                if (status == 1) {
                                                    holder.iv_item2.setImageResource(R.drawable.v1_zan3x);
                                                    int like_num = Integer.valueOf(holder.tv_item4.getText().toString());
                                                    ++like_num;
                                                    holder.tv_item4.setText(String.valueOf(like_num));
                                                    ToastUtils.showShort("点赞成功");
                                                } else {
                                                    String code = jsonObject.optString("code");
                                                    if (code.equals("67000")) {
                                                        handler.sendEmptyMessage(555);
                                                        return;
                                                    }
                                                    String msg = jsonObjectData.optString("msg");
                                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    });
                    int is_like = allSecondCommentBean.getIs_like();
                    if (EmptyUtils.isNotEmpty(is_like)) {
                        switch (is_like) {
                            case 1:
                                holder.iv_item2.setImageResource(R.drawable.v1_zan3x);
                                break;
                            case 2:
                                holder.iv_item2.setImageResource(R.drawable.v1_meizan3x);
                                break;
                            default:
                                break;
                        }
                    }
                    holder.tv_item4.setText(String.valueOf(allSecondCommentBean.getLike_num()));
                }
            }
        } else if (dataType == SELLING_CAR_DATA_TYPE) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList)) {
                CarInfoBean carInfoBean = carInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles_img())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(carInfoBean.getBrand_info().getBrand_name() + " " + carInfoBean.getSeries_info().getSeries_name() + " " + carInfoBean.getModel_info().getModel_name());
                    holder.tv_item3.setText(carInfoBean.getModel_info().getModel_year() + "年/排量" + carInfoBean.getModel_detail().getEngine_ExhaustForFloat());
                    holder.tv_item2.setText(mContext.getString(R.string._wan, String.format(Locale.CHINA, "%.2f", carInfoBean.getPrice())));
                }
            }
        } else if (dataType == THEME_USER_DATA_TYPE) {
            ArrayList<ThemeUserBean> themeUserBeanArrayList = (ArrayList<ThemeUserBean>) mList;

            if (EmptyUtils.isNotEmpty(themeUserBeanArrayList)) {
                ThemeUserBean themeUserBean = themeUserBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(themeUserBean)) {
                    Glide.with(mContext)
                            .load(themeUserBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .into(holder.iv_item1);
                }
            }
        } else if (dataType == DREAM_CAR_DATA_TYPE) {
            ArrayList<DreamCarBean> dreamCarBeanArrayList = (ArrayList<DreamCarBean>) mList;

            if (EmptyUtils.isNotEmpty(dreamCarBeanArrayList)) {
                DreamCarBean dreamCarBean = dreamCarBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(dreamCarBean)) {
                    Glide.with(mContext)
                            .load(dreamCarBean.getBrand_info().getBrand_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(dreamCarBean.getBrand_info().getBrand_name() + dreamCarBean.getSeries_info().getSeries_name());
                }
            }
        } else if (dataType == SAME_STYLE_CAR_DATA_TYPE) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList)) {
                CarInfoBean carInfoBean = carInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles_img())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(carInfoBean.getCar_name());
                    holder.tv_item2.setText(carInfoBean.getPrice() + "万");
                }
            }
        } else if (dataType == CAR_SURFACE_DATA_TYPE) {
            ArrayList<CarInfoBean.FilesBean.Type1Bean> type1BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type1Bean>) mList;

            if (EmptyUtils.isNotEmpty(type1BeanArrayList)) {
                CarInfoBean.FilesBean.Type1Bean type1Bean = type1BeanArrayList.get(position);

                Glide.with(mContext)
                        .load(type1Bean.getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
            }
        } else if (dataType == CAR_INSIDE_DATA_TYPE) {
            ArrayList<CarInfoBean.FilesBean.Type2Bean> type2BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type2Bean>) mList;

            if (EmptyUtils.isNotEmpty(type2BeanArrayList)) {
                CarInfoBean.FilesBean.Type2Bean type2Bean = type2BeanArrayList.get(position);

                Glide.with(mContext)
                        .load(type2Bean.getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
            }
        } else if (dataType == CAR_STRUCTURE_DATA_TYPE) {
            ArrayList<CarInfoBean.FilesBean.Type3Bean> type3BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type3Bean>) mList;

            if (EmptyUtils.isNotEmpty(type3BeanArrayList)) {
                CarInfoBean.FilesBean.Type3Bean type3Bean = type3BeanArrayList.get(position);

                Glide.with(mContext)
                        .load(type3Bean.getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
            }
        } else if (dataType == CAR_MORE_DETAIL_DATA_TYPE) {
            ArrayList<CarInfoBean.FilesBean.Type4Bean> type4BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type4Bean>) mList;

            if (EmptyUtils.isNotEmpty(type4BeanArrayList)) {
                CarInfoBean.FilesBean.Type4Bean type4Bean = type4BeanArrayList.get(position);

                Glide.with(mContext)
                        .load(type4Bean.getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
            }
        } else if (dataType == MY_STATE_DATA_TYPE) {
            final ArrayList<FeedBean> feedBeanArrayList = (ArrayList<FeedBean>) mList;

            if (EmptyUtils.isNotEmpty(feedBeanArrayList)) {
                final FeedBean feedBean = feedBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(feedBean)) {
                    if (EmptyUtils.isNotEmpty(feedBean.getPost_user_info()) && EmptyUtils.isNotEmpty(feedBean.getPost_user_info().getProfile())) {
                        Glide.with(mContext)
                                .load(feedBean.getPost_user_info().getProfile().getAvatar())
                                .placeholder(R.drawable.user_photo)
                                .error(R.drawable.user_photo)
                                .into(holder.iv_item1);

                        int user_id = feedBean.getPost_user_info().getUser_id();
                        if (EmptyUtils.isNotEmpty(user_id)) {
                            holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.USER_ID, feedBean.getPost_user_info().getUser_id());
                                    ((BaseActivity) mContext).gotoPager(OtherHomePageFragment.class, bundle);
                                }
                            });
                            holder.tv_item1.setText(feedBean.getPost_user_info().getProfile().getNickname() == null ? "" : feedBean.getPost_user_info().getProfile().getNickname());
                            holder.tv_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.USER_ID, feedBean.getPost_user_info().getUser_id());
                                    ((BaseActivity) mContext).gotoPager(OtherHomePageFragment.class, bundle);
                                }
                            });
                        }
                    }
                    holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(feedBean.getCreated()) * 1000), new SimpleDateFormat("yy-MM-dd HH:mm")));

                    if (SPUtils.getInstance().getInt(Constant.USER_ID) != feedBean.getPost_user_info().getUser_id()) {
                        final int is_friend = feedBean.getPost_user_info().getIs_friend();
                        resetFollowView(holder, is_friend);

                        holder.iv_item6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int is_friend = feedBean.getPost_user_info().getIs_friend();
                                switch (is_friend) {
                                    case 1:
                                        //已经关注，取消关注
                                        OkHttpUtils.post()
                                                .url(Constant.getDeleteFollowUrl())
                                                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                .addParams(Constant.USER_ID, String.valueOf(feedBean.getPost_user_info().getUser_id()))
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
                                                                feedBean.getPost_user_info().setIs_friend(2);
                                                                resetFollowView(holder, 2);
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
                                        break;
                                    case 2:
                                        //已经取消关注，再关注
                                        OkHttpUtils.post()
                                                .url(Constant.getCreateFollowUrl())
                                                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                .addParams(Constant.USER_ID, String.valueOf(feedBean.getPost_user_info().getUser_id()))
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
                                                                feedBean.getPost_user_info().setIs_friend(1);
                                                                resetFollowView(holder, 1);
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
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    } else {
                        holder.iv_item6.setVisibility(View.GONE);
                    }

                    ArrayList<FeedBean.PostFilesBeanX> postFilesBeanXArrayList = (ArrayList<FeedBean.PostFilesBeanX>) feedBean.getPost_files();
                    if (EmptyUtils.isNotEmpty(postFilesBeanXArrayList) && postFilesBeanXArrayList.size() != 0) {
                        int size = postFilesBeanXArrayList.size();
                        if (size == 1) {
                            holder.iv_item7.setVisibility(View.VISIBLE);
                            holder.recyclerView.setVisibility(View.GONE);
                            Glide.with(mContext)
                                    .load(postFilesBeanXArrayList.get(0).getFile_url())
                                    .placeholder(R.drawable.default_bg_ratio_1)
                                    .error(R.drawable.default_bg_ratio_1)
                                    .into(holder.iv_item7);
                        }else {
                            holder.recyclerView.setVisibility(View.VISIBLE);
                            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, postFilesBeanXArrayList, MY_STATE_INNER_DATA_TYPE);
                            holder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                            holder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                        }
                    }

                    holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (EmptyUtils.isNotEmpty(mOnShareToPlatform)) {
                                mOnShareToPlatform.share();
                            }
                        }
                    });
                    holder.iv_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.FEED_ID, feedBean.getFeed_id());
                            ((BaseActivity) mContext).gotoPager(StateDetailFragment.class, bundle);//动态详情
                        }
                    });

                    final String is_like = feedBean.getIs_like();
                    if (EmptyUtils.isNotEmpty(is_like)) {
                        switch (is_like) {
                            case "1":
                                holder.iv_item4.setImageResource(R.drawable.v1_like_selected3x);
                                break;
                            case "2":
                                holder.iv_item4.setImageResource(R.drawable.v1_like3x);
                                break;
                            default:
                                break;
                        }
                    }
                    holder.iv_item4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if (msg.what == 555) {
                                        //取消点赞
                                        OkHttpUtils.post()
                                                .url(Constant.getLikeDeleteURl())
                                                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                                .addParams(Constant.FEED_ID, String.valueOf(feedBean.getFeed_id()))
                                                .build()
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        ToastUtils.showShort("取消点赞失败");
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = new JSONObject(response);
                                                            int status = jsonObject.optInt("status");
                                                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                            if (status == 1) {
                                                                holder.iv_item4.setImageResource(R.drawable.v1_like3x);
                                                                int like_num = Integer.valueOf(holder.tv_item6.getText().toString());
                                                                --like_num;
                                                                holder.tv_item6.setText(String.valueOf(like_num));
                                                                ToastUtils.showShort("取消点赞成功");
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
                            };
                            //点赞
                            OkHttpUtils.post()
                                    .url(Constant.getLikeCreateURl())
                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                    .addParams(Constant.FEED_ID, String.valueOf(feedBean.getFeed_id()))
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtils.showShort("点赞失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                int status = jsonObject.optInt("status");
                                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                                if (status == 1) {
                                                    holder.iv_item4.setImageResource(R.drawable.v1_like_selected3x);
                                                    int like_num = Integer.valueOf(holder.tv_item6.getText().toString());
                                                    ++like_num;
                                                    holder.tv_item6.setText(String.valueOf(like_num));
                                                    ToastUtils.showShort("点赞成功");
                                                } else {
                                                    String code = jsonObject.optString("code");
                                                    if (code.equals("67000")) {
                                                        handler.sendEmptyMessage(555);
                                                        return;
                                                    }
                                                    String msg = jsonObjectData.optString("msg");
                                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    });

                    holder.iv_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(v, feedBean, feedBeanArrayList);
                        }
                    });

                    holder.tv_item4.setText(String.valueOf(feedBean.getForward_num()));
                    holder.tv_item5.setText(String.valueOf(feedBean.getComment_num()));
                    holder.tv_item6.setText(String.valueOf(feedBean.getLike_num()));

                    String post_content = feedBean.getPost_content();
                    if (EmptyUtils.isNotEmpty(post_content)) {
                        if (post_content.contains("#")) {
                            int start_index = post_content.indexOf("#");
                            int last_index = post_content.lastIndexOf("#");

                            holder.tv_item7.setText(post_content.substring(start_index, (last_index + 1)));
                            holder.tv_item8.setText(post_content.substring((last_index + 1)));
                        } else {
                            holder.tv_item8.setText(post_content);
                        }
                    } else {
                        holder.tv_item7.setVisibility(View.GONE);
                        holder.tv_item8.setVisibility(View.GONE);
                    }
                }
            }
        } else if (dataType == MY_STATE_INNER_DATA_TYPE) {
            ArrayList<FeedBean.PostFilesBeanX> postFilesBeanXArrayList = (ArrayList<FeedBean.PostFilesBeanX>) mList;

            if (EmptyUtils.isNotEmpty(postFilesBeanXArrayList)) {
                final FeedBean.PostFilesBeanX postFilesBeanX = postFilesBeanXArrayList.get(position);

                if (EmptyUtils.isNotEmpty(postFilesBeanX)) {
                    Glide.with(mContext)
                            .load(postFilesBeanX.getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);

                    if (postFilesBeanXArrayList.size() != 0) {
                        final ArrayList<String> stringImageUrl = new ArrayList<>();
                        for (int i = 0; i < postFilesBeanXArrayList.size(); i++) {
                            stringImageUrl.add(postFilesBeanXArrayList.get(i).getFile_url());
                        }
                        holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DataManager.getInstance().setData1(stringImageUrl);
                                ((BaseActivity) mContext).gotoPager(ShowAllImageFragment.class, null);
                            }
                        });
                    }
                }
            }
        } else if (dataType == TOPIC_MEMBER_DATA_TYPE) {
            ArrayList<ThemeUserBean> themeUserBeanArrayList = (ArrayList<ThemeUserBean>) mList;

            if (EmptyUtils.isNotEmpty(themeUserBeanArrayList)) {
                ThemeUserBean themeUserBean = themeUserBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(themeUserBean)) {
                    Glide.with(mContext)
                            .load(themeUserBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(themeUserBean.getNickname());
                    holder.tv_item2.setText("粉丝 " + themeUserBean.getFans_num() + " | " + "关注 " + themeUserBean.getFriend_num());////粉丝 145 丨 关注 12

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.e("hehheheh");
                        }
                    });
                }
            }
        } else if (dataType == MY_CAR_REPERTORY_DATA_TYPE) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList)) {
                CarInfoBean carInfoBean = carInfoBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles_img())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(EmptyUtils.isEmpty(carInfoBean.getCar_name()) ? "" : carInfoBean.getCar_name());
                }
            }
        } else if (dataType == SELECT_CAR_COLOR_DATA_TYPE) {
            Resources resources = mContext.getResources();
            String text = resources.getString(resources.getIdentifier("car_color_" + (position + 1), "string", mContext.getPackageName()));
            int color = resources.getColor(resources.getIdentifier("car_color_" + (position + 1), "color", mContext.getPackageName()));

            GradientDrawable background = (GradientDrawable) holder.tv_item1.getBackground();

            if (position == 6 || position == 13) {
                holder.tv_item1.setTextColor(resources.getColor(R.color.seventh_background_color));
                background.setColor(Color.TRANSPARENT);
                background.setStroke(SizeUtils.dp2px(1), resources.getColor(R.color.seventh_background_color));
            } else {
                holder.tv_item1.setTextColor(Color.WHITE);
                background.setColor(color);
                background.setStroke(SizeUtils.dp2px(1), color);
            }
            holder.tv_item1.setText(text);
        } else if (dataType == RECOMMEND_USER_DATA_TYPE) {
            ArrayList<RecommendUserInfoBean> recommendUserInfoBeanArrayList = (ArrayList<RecommendUserInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(recommendUserInfoBeanArrayList)) {
                final RecommendUserInfoBean recommendUserInfoBean = recommendUserInfoBeanArrayList.get(position);
                if (EmptyUtils.isNotEmpty(recommendUserInfoBean)) {
                    Glide.with(mContext)
                            .load(recommendUserInfoBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(recommendUserInfoBean.getNickname() == null ? "" : recommendUserInfoBean.getNickname());
                    holder.tv_item2.setText(recommendUserInfoBean.getSignature() == null ? "" : recommendUserInfoBean.getSignature());

                    final int user_id = recommendUserInfoBean.getUser_id();
                    if (EmptyUtils.isNotEmpty(user_id)) {
                        holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OkHttpUtils.post()
                                        .url(Constant.getCreateFollowUrl())
                                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                        .addParams(Constant.USER_ID, String.valueOf(user_id))
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
                                                        ToastUtils.showShort("关注成功！");
                                                        holder.iv_item2.setImageResource(R.drawable.other_followed);
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
                        });
                    }
                }
            }
        } else if (dataType == PECCANCY_RECORD_DATA_TYPE) {
            List<PeccancyRecordBean.ListsBean> listsBeanList = (List<PeccancyRecordBean.ListsBean>) mList;

            if (EmptyUtils.isNotEmpty(listsBeanList) && listsBeanList.size() != 0) {
                PeccancyRecordBean.ListsBean listsBean = listsBeanList.get(position);
                if (EmptyUtils.isNotEmpty(listsBean)) {
                    String handled = listsBean.getHandled();
                    if (EmptyUtils.isNotEmpty(handled)) {
                        switch (handled) {
                            case "0"://未处理
                                holder.iv_item1.setImageResource(R.drawable.peccancy_record_red3x);
                                holder.tv_item1.setText(listsBean.getDate());
                                holder.tv_item1.setTextColor(mContext.getResources().getColor(R.color.warning_color));
                                holder.tv_item2.setText("未处理");
                                holder.tv_item2.setTextColor(mContext.getResources().getColor(R.color.warning_color));
                                break;
                            case "1":
                                holder.tv_item1.setText(listsBean.getDate());
                                break;
                            default:
                                break;
                        }
                    }
                    holder.tv_item3.setText(listsBean.getAct());
                }
            }
        } else if (dataType == HPHM_DATA_TYPE) {
            ArrayList<PeccancyHistoryBean> peccancyHistoryBeanArrayList = (ArrayList<PeccancyHistoryBean>) mList;

            if (EmptyUtils.isNotEmpty(peccancyHistoryBeanArrayList)) {
                PeccancyHistoryBean peccancyHistoryBean = peccancyHistoryBeanArrayList.get(position);
                if (EmptyUtils.isNotEmpty(peccancyHistoryBean)) {
                    holder.tv_item1.setText(peccancyHistoryBean.getHphm());
                    holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(peccancyHistoryBean.getCreated()) * 1000), new SimpleDateFormat("yyyy.MM.dd")));
                }
            }
        } else if (dataType == GUARANTEE_HISTORY_DATA_TYPE) {
            ArrayList<GuaranteeHistoryBean> guaranteeHistoryBeanArrayList = (ArrayList<GuaranteeHistoryBean>) mList;

            if (EmptyUtils.isNotEmpty(guaranteeHistoryBeanArrayList)) {
                GuaranteeHistoryBean guaranteeHistoryBean = guaranteeHistoryBeanArrayList.get(position);
                if (EmptyUtils.isNotEmpty(guaranteeHistoryBean)) {
                    int status = guaranteeHistoryBean.getStatus();//1:待支付 2:支付成功(报告生成中) 3:支付失败 4:报告已生成 5:报告异常
                    if (EmptyUtils.isNotEmpty(status)) {
                        switch (status) {
                            case 1:
                                holder.tv_item1.setText("待支付");
                                break;
                            case 2:
                                holder.tv_item1.setText("报告生成中");
                                break;
                            case 3:
                                holder.tv_item1.setText("支付失败");
                                holder.tv_item1.setTextColor(mContext.getResources().getColor(R.color.warning_color));
                                holder.iv_item2.setBackgroundResource(R.drawable.double_oval_warning);
                                break;
                            case 4:
                                holder.tv_item1.setText("报告已生成");
                                break;
                            case 5:
                                holder.tv_item1.setText("报告异常");
                                holder.tv_item1.setTextColor(mContext.getResources().getColor(R.color.warning_color));
                                holder.iv_item2.setBackgroundResource(R.drawable.double_oval_warning);
                                break;
                            default:
                                break;
                        }
                    }
                    String created_at = guaranteeHistoryBean.getCreated_at();
                    holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(created_at) * 1000), new SimpleDateFormat("yyyy.MM.dd")));
                    Glide.with(mContext)
                            .load(guaranteeHistoryBean.getBrand_logo())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item3.setText(guaranteeHistoryBean.getBrand_name());
                    holder.tv_item4.setText("VIN：" + guaranteeHistoryBean.getVin());//VIN：WPGAFG343TMJOTH
                }
            }
        } else if (dataType == SALES_CONSULTANT_DATA_TYPE) {
            ArrayList<UserInfoForSalesConsultant> userInfoForSalesConsultantArrayList = (ArrayList<UserInfoForSalesConsultant>) mList;
            if (EmptyUtils.isNotEmpty(userInfoForSalesConsultantArrayList)) {
                UserInfoForSalesConsultant userInfoForSalesConsultant = userInfoForSalesConsultantArrayList.get(position);
                if (EmptyUtils.isNotEmpty(userInfoForSalesConsultant)) {
                    Glide.with(mContext)
                            .load(userInfoForSalesConsultant.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(userInfoForSalesConsultant.getNickname());
                    holder.tv_item2.setText(userInfoForSalesConsultant.getIntro());
                }
            }
        } else if (dataType == CAR_SERIES_DATA_TYPE) {
            ArrayList<CarSeriesBean> carSeriesBeanArrayList = (ArrayList<CarSeriesBean>) mList;

            if (EmptyUtils.isNotEmpty(carSeriesBeanArrayList)) {
                CarSeriesBean carSeriesBean = carSeriesBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carSeriesBean)) {
                    holder.tv_item1.setText(carSeriesBean.getSeries_name());
                }
            }
        } else if (dataType == CAR_MODEL_DATA_TYPE) {
            ArrayList<CarModelBean> carModelBeanArrayList = (ArrayList<CarModelBean>) mList;

            if (EmptyUtils.isNotEmpty(carModelBeanArrayList)) {
                CarModelBean carModelBean = carModelBeanArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carModelBean)) {
                    holder.tv_item1.setText(carModelBean.getModel_name());
                }
            }
        }
    }

    private void resetFollowView(ViewHolder holder, int is_friend) {
        switch (is_friend) {
            case 1:
                holder.iv_item6.setImageResource(R.drawable.other_followed);
                break;
            case 2:
                holder.iv_item6.setImageResource(R.drawable.other_follow);
                break;
            default:
                break;
        }
    }

    private void showDialog(View v, final FeedBean feedBean, final ArrayList<FeedBean> feedBeanArrayList) {
        //        先判断该动态是否是自己的
        String[] items1 = {"删除"};
        String[] items2 = {"举报"};
        if (SPUtils.getInstance().getInt(Constant.USER_ID) == feedBean.getPost_user_info().getUser_id()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            //builder.setMessage("是否确认退出?"); //设置内容
            builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
            //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
            builder.setItems(items1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
                    mDialog.setTitle("警告");
                    mDialog.setMessage("是否要删除此动态?");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            feedBeanArrayList.remove(feedBean);
                            notifyDataSetChanged();
                            OkHttpUtils.post()
                                    .url(Constant.getDeleteCommentUrl())
                                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                                    .addParams(Constant.FEED_ID, String.valueOf(feedBean.getFeed_id()))
                                    .addParams(Constant.COMMENT_ID, "")
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
                                                    ToastUtils.showShort("删除成功!");
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
                    });
                    mDialog.setNegativeButton("取消", null);
                    mDialog.create().show();
                }
            });
            builder.create().show();
        } else {
            //别人发的
            //dialog参数设置
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            //builder.setMessage("是否确认退出?"); //设置内容
            builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
            //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
            builder.setItems(items2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //--------------------------延迟一秒---------------------------------
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //-----------------------------------------------------------
                    ToastUtils.showLong("^_^ 我们已收到您的举报，会尽快处理！\n" +
                            "                    感谢您的支持！");
                }
            });
            builder.create().show();
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private ImageView iv_item1;
        private ImageView iv_item2;
        private ImageView iv_item3;
        private ImageView iv_item4;
        private ImageView iv_item5;
        private ImageView iv_item6;
        private ImageView iv_item7;
        private ImageView iv_item8;
        private ImageView iv_item9;
        private ImageView iv_item10;

        private TextView tv_item1;
        private TextView tv_item2;
        private TextView tv_item3;
        private TextView tv_item4;
        private TextView tv_item5;
        private TextView tv_item6;
        private TextView tv_item7;
        private TextView tv_item8;

        private RecyclerView recyclerView;

        private LinearLayout linearLayout1;
        private LinearLayout linearLayout2;

        private RelativeLayout relativeLayout;

        private CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            if (dataType == NEW_CAR_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_most_new_car);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_most_new_car_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_most_new_car_price);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_VIDEO_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_video_play);
                iv_item2 = (ImageButton) itemView.findViewById(R.id.image_btn_play);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CHE_HANG_HOME_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_title);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_info);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_sold_and_selling);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_RENT_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_rent_able);
                iv_item3 = (ImageView) itemView.findViewById(R.id.iv_renting);

                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_money);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == RECOMMEND_TOPIC_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_recommend_item);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_recommend_item);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == MY_TOPIC_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_num);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_update);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == ALL_TOPIC_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_content);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_num);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == SELECT_TOPIC_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_content);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_num);

                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == ARTICLE_LIST_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_che_hang);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == VIDEO_LIST_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_video_play);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_che_hang);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_update_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CHE_HANG_LIST_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_che_hang);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_che_hang_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_selling_num);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_sold_num);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
                linearLayout1 = (LinearLayout) itemView.findViewById(R.id.ll_phone);
                linearLayout2 = (LinearLayout) itemView.findViewById(R.id.ll_location);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_che_hang);
            } else if (dataType == CHE_HANG_LIST_ITEM_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_price);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_LIST_FOR_CAR_CENTER) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.ivCarIcon);
                tv_item1 = (TextView) itemView.findViewById(R.id.tvCarName);
                tv_item2 = (TextView) itemView.findViewById(R.id.tvCarDistance);
                tv_item3 = (TextView) itemView.findViewById(R.id.tvPrice);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == USER_SEARCH_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_fan_and_follow);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == MY_CAR_RENT_ORDER_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_car_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_car_rent_status);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_car_rent_time);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_car_rent_name);
                tv_item4 = (TextView) itemView.findViewById(R.id.tv_car_rent_between);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_RENT_RECOMMEND_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_car_rent_status);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_price);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == ARTICLE_COMMENT_LIST_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_comment_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_comment_time);

                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_zan);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_zan);
                iv_item3 = (ImageView) itemView.findViewById(R.id.iv_comment_item);
                tv_item4 = (TextView) itemView.findViewById(R.id.tv_comment_num_item);

                tv_item5 = (TextView) itemView.findViewById(R.id.tv_comment_content);//评论的内容

                linearLayout1 = (LinearLayout) itemView.findViewById(R.id.ll_comment_reply);
                linearLayout2 = (LinearLayout) itemView.findViewById(R.id.ll_zan_or_no);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

                tv_item6 = (TextView) itemView.findViewById(R.id.tv_check_all_comment_reply);
            } else if (dataType == ARTICLE_COMMENT_REPLY_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_comment_reply_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_comment_reply_content);
            } else if (dataType == COMMENT_DETAIL_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_comment_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_comment_time);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_comment_content);

                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_zan);
                tv_item4 = (TextView) itemView.findViewById(R.id.tv_zan);

                linearLayout1 = (LinearLayout) itemView.findViewById(R.id.ll_zan_or_no);
            } else if (dataType == SELLING_CAR_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.ivCarIcon);
                tv_item1 = (TextView) itemView.findViewById(R.id.tvCarName);
                tv_item2 = (TextView) itemView.findViewById(R.id.tvPrice);
                tv_item3 = (TextView) itemView.findViewById(R.id.tvCarDistance);
                tv_item4 = (TextView) itemView.findViewById(R.id.iv_edit);

                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_like);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == THEME_USER_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == DREAM_CAR_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
            } else if (dataType == SAME_STYLE_CAR_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_most_new_car);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_most_new_car_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_most_new_car_price);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_SURFACE_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image_all);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_INSIDE_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image_all);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_STRUCTURE_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image_all);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_MORE_DETAIL_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image_all);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == MY_STATE_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_state_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_state_time);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_state_share);
                iv_item3 = (ImageView) itemView.findViewById(R.id.iv_state_comment);
                iv_item4 = (ImageView) itemView.findViewById(R.id.iv_state_like);
                iv_item5 = (ImageView) itemView.findViewById(R.id.iv_state_report);
                iv_item6 = (ImageView) itemView.findViewById(R.id.iv_follow);

                iv_item7 = (ImageView) itemView.findViewById(R.id.iv_image_1);

                tv_item4 = (TextView) itemView.findViewById(R.id.tv_state_share_num);
                tv_item5 = (TextView) itemView.findViewById(R.id.tv_state_comment_num);
                tv_item6 = (TextView) itemView.findViewById(R.id.tv_state_like_num);

                tv_item7 = (TextView) itemView.findViewById(R.id.tv_state_content1);
                tv_item8 = (TextView) itemView.findViewById(R.id.tv_state_content2);

                recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == MY_STATE_INNER_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_image_state_inner);
            } else if (dataType == TOPIC_MEMBER_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_num);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == MY_CAR_REPERTORY_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_car_image);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_car_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == SELECT_CAR_COLOR_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(tvCarColor);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == RECOMMEND_USER_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_circle_image);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv_follow);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_user_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_user_profile);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == PECCANCY_RECORD_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv1);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_peccancy_time);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_handle_or_not);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_peccancy_content);
            } else if (dataType == HPHM_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_car_hphm);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_record_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == GUARANTEE_HISTORY_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_record_status);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_record_time);

                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_car_image);
                iv_item2 = (ImageView) itemView.findViewById(R.id.iv1);
                tv_item3 = (TextView) itemView.findViewById(R.id.tv_car_name);
                tv_item4 = (TextView) itemView.findViewById(R.id.tv_car_vin);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == SALES_CONSULTANT_DATA_TYPE) {
                iv_item1 = (ImageView) itemView.findViewById(R.id.iv_picture);
                tv_item1 = (TextView) itemView.findViewById(R.id.tv_name);
                tv_item2 = (TextView) itemView.findViewById(R.id.tv_brief);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_SERIES_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(R.id.tvSeriesName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            } else if (dataType == CAR_MODEL_DATA_TYPE) {
                tv_item1 = (TextView) itemView.findViewById(R.id.tvSeriesName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = getLayoutPosition();
                            mOnItemClickListener.onItemClick(mList.get(position), position);
                        }
                    }
                });
            }
        }
    }

    /**
     * 接口定义三步曲
     */
    public interface OnItemClickListener {
        void onItemClick(Object data, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnShareToPlatform {
        void share();
    }

    public OnShareToPlatform mOnShareToPlatform;

    public void setOnShareToPlatform(OnShareToPlatform mOnShareToPlatform) {
        this.mOnShareToPlatform = mOnShareToPlatform;
    }

    public ArrayList<String> getTopic_string() {
        return topic_string;
    }
}
