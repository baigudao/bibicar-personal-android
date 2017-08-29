package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.bean.ArticleBean;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.CarInfoBeanForCarCenter;
import com.wiserz.pbibi.bean.CarInfoBeanForCarDetail;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.bean.CarRentRecommendCarBean;
import com.wiserz.pbibi.bean.CheHangBean;
import com.wiserz.pbibi.bean.CheHangUserListBean;
import com.wiserz.pbibi.bean.DreamCarBean;
import com.wiserz.pbibi.bean.FuLiBean;
import com.wiserz.pbibi.bean.MyCarRentOrderBean;
import com.wiserz.pbibi.bean.ThemeUserBean;
import com.wiserz.pbibi.bean.TopicInfoBean;
import com.wiserz.pbibi.bean.UserBean;
import com.wiserz.pbibi.bean.VideoBean;
import com.wiserz.pbibi.fragment.CommentDetailFragment;
import com.wiserz.pbibi.fragment.VideoDetailFragment;
import com.wiserz.pbibi.util.DataManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jackie on 2017/8/10 14:07.
 * QQ : 971060378
 * Used as : 基本的RecyclerView的适配器
 */
public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<T> mList;
    private int dataType;

    private static final int NEW_CAR_DATA_TYPE = 55;
    private static final int CAR_VIDEO_DATA_TYPE = 65;
    private static final int CHE_HANG_DATA_TYPE = 75;

    private static final int CAR_RENT_DATA_TYPE = 77;

    private static final int RECOMMEND_TOPIC_DATA_TYPE = 88;
    private static final int MY_TOPIC_DATA_TYPE = 98;

    private static final int ALL_TOPIC_DATA_TYPE = 23;

    private static final int ARTICLE_LIST_DATA_TYPE = 76;

    private static final int VIDEO_LIST_DATA_TYPE = 99;

    private static final int CHE_HANG_LIST_DATA_TYPE = 14;
    private static final int CHE_HANG_LIST_ITEM_DATA_TYPE = 15;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;//汽车中心的汽车列表

    private static final int USER_SEARCH_DATA_TYPE = 12;

    private static final int MY_CAR_RENT_ORDER_DATA_TYPE = 13;

    private static final int CAR_RENT_RECOMMEND_DATA_TYPE = 16;

    private static final int ARTICLE_COMMENT_LIST_DATA_TYPE = 18;

    private static final int ARTICLE_COMMENT_REPLY_DATA_TYPE = 19;

    private static final int COMMENT_DETAIL_DATA_TYPE = 20;

    private static final int SELLING_CAR_DATA_TYPE = 21;

    private static final int THEME_USER_DATA_TYPE = 22;

    private static final int MY_CAR_REPERTORY_DATA_TYPE = 24;

    private static final int MY_STATE_DATA_TYPE = 25;

    private static final int DREAM_CAR_DATA_TYPE = 26;

    private static final int TOTAL_PROPERTY_DATA_TYPE = 27;

    private static final int SAME_STYLE_CAR_DATA_TYPE = 28;

    public BaseRecyclerViewAdapter(Context context, List<T> tList, int dataType) {
        this.mContext = context;
        this.mList = tList;
        this.dataType = dataType;
    }

    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (dataType == SAME_STYLE_CAR_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_same_style_car, null));
        } else if (dataType == TOTAL_PROPERTY_DATA_TYPE) {
            viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_total_property, null));
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
        } else if (dataType == CHE_HANG_DATA_TYPE) {
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
    public void onBindViewHolder(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        if (dataType == NEW_CAR_DATA_TYPE) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = (ArrayList<CarInfoBean>) mList;
            Glide.with(mContext)
                    .load(carInfoBeanArrayList.get(position).getFiles().get(0).getFile_url())
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_item1);
            holder.tv_item1.setText(carInfoBeanArrayList.get(position).getCar_name());
            holder.tv_item2.setText(String.valueOf(carInfoBeanArrayList.get(position).getPrice()) + " 万");
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
                        DataManager.getInstance().setData1(videoBean);
                        ((BaseActivity) mContext).gotoPager(VideoDetailFragment.class, null);
                    }
                });
                if (videoBean.getPost_content() != null) {
                    holder.tv_item1.setText(videoBean.getPost_content());
                }
            }
        } else if (dataType == CHE_HANG_DATA_TYPE) {
            ArrayList<CheHangBean> cheHangBeanArrayList = (ArrayList<CheHangBean>) mList;

            if (EmptyUtils.isNotEmpty(cheHangBeanArrayList)) {
                CheHangBean cheHangBean = cheHangBeanArrayList.get(position);
                if (cheHangBean.getAvatar() != null) {
                    Glide.with(mContext)
                            .load(cheHangBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                }
                if (cheHangBean.getNickname() != null) {
                    holder.tv_item1.setText(cheHangBean.getNickname());
                }
                if (cheHangBean.getSignature() != null) {
                    holder.tv_item2.setText(cheHangBean.getSignature());
                }
                holder.tv_item3.setText("在售" + cheHangBean.getSaling_num() + "辆丨已售" + cheHangBean.getSold_num() + "辆");
            }
        } else if (dataType == CAR_RENT_DATA_TYPE) {
            ArrayList<CarRentInfoBean> carRentInfoBeanArrayList = (ArrayList<CarRentInfoBean>) mList;

            if (EmptyUtils.isNotEmpty(carRentInfoBeanArrayList)) {
                CarRentInfoBean carRentInfoBean = carRentInfoBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(carRentInfoBean.getFiles().get(0).getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
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

                Glide.with(mContext)
                        .load(topicInfoBean.getPost_file())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(topicInfoBean.getTheme());
            }
        } else if (dataType == MY_TOPIC_DATA_TYPE) {
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.user_photo)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(fuLiBean.getWho());
                holder.tv_item2.setText(fuLiBean.getDesc());
                holder.tv_item3.setText(fuLiBean.getDesc());
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
            ArrayList<CarInfoBeanForCarCenter> carInfoBeanForCarCenterArrayList = (ArrayList<CarInfoBeanForCarCenter>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanForCarCenterArrayList)) {
                CarInfoBeanForCarCenter carInfoBeanForCarCenter = carInfoBeanForCarCenterArrayList.get(position);

                Glide.with(mContext)
                        .load(carInfoBeanForCarCenter.getFiles().get(0).getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(carInfoBeanForCarCenter.getCar_name());
                if (carInfoBeanForCarCenter.getCar_type() == 0) {
                    holder.tv_item2.setText(mContext.getResources().getString(R.string.car_sales_volume, String.valueOf(carInfoBeanForCarCenter.getSales_volume())));
                } else {
                    holder.tv_item2.setText(mContext.getResources().getString(R.string.time_wan_kilo, carInfoBeanForCarCenter.getBoard_time(), String.valueOf(carInfoBeanForCarCenter.getMileage())));
                }
                holder.tv_item3.setText(mContext.getResources().getString(R.string._wan, String.valueOf(carInfoBeanForCarCenter.getPrice())));
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
                        .load(carRentRecommendCarBean.getFiles().get(0).getFile_url())
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
                holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(articleCommentBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

                holder.tv_item5.setText(articleCommentBean.getComment_content());//评论的内容

                ArrayList<ArticleCommentBean.HotListBean.ListBean> listBeanList = (ArrayList<ArticleCommentBean.HotListBean.ListBean>) articleCommentBean.getHot_list().getList();
                if (EmptyUtils.isNotEmpty(listBeanList)) {
                    holder.linearLayout1.setVisibility(View.VISIBLE);
                    int reply_size = listBeanList.size();
                    if (reply_size <= 3) {
                        //如果回复数小于等于3
                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, listBeanList, ARTICLE_COMMENT_REPLY_DATA_TYPE);
                        holder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    }

                    holder.tv_item6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //查看更多回复
                            DataManager.getInstance().setData1(articleCommentBean);
                            ((BaseActivity) mContext).gotoPager(CommentDetailFragment.class, null);
                        }
                    });
                } else {
                    holder.linearLayout1.setVisibility(View.GONE);
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
            ArrayList<ArticleCommentBean.HotListBean.ListBean> listBeanList = (ArrayList<ArticleCommentBean.HotListBean.ListBean>) mList;

            if (EmptyUtils.isNotEmpty(listBeanList)) {
                final ArticleCommentBean.HotListBean.ListBean listBean = listBeanList.get(position);

                Glide.with(mContext)
                        .load(listBean.getFrom_user().getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into(holder.iv_item1);
                holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort(listBean.getFrom_user().getUser_id() + "");
                    }
                });
                holder.tv_item1.setText(listBean.getFrom_user().getNickname());
                holder.tv_item2.setText(TimeUtils.date2String(new Date(Long.valueOf(listBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));
                holder.tv_item3.setText(listBean.getComment_content());
            }
        } else if (dataType == SELLING_CAR_DATA_TYPE) {
            ArrayList<CheHangBean.CarListBeanX.CarListBean> carListBeanArrayList = (ArrayList<CheHangBean.CarListBeanX.CarListBean>) mList;

            if (EmptyUtils.isNotEmpty(carListBeanArrayList)) {
                CheHangBean.CarListBeanX.CarListBean carListBean = carListBeanArrayList.get(position);
                CheHangBean.CarListBeanX.CarListBean.CarInfoBean carInfoBean = carListBean.getCar_info();

                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    Glide.with(mContext)
                            .load(carInfoBean.getFiles().get(0).getFile_url())
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
            ArrayList<CarInfoBeanForCarDetail> carInfoBeanForCarDetailArrayList = (ArrayList<CarInfoBeanForCarDetail>) mList;

            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetailArrayList)) {
                CarInfoBeanForCarDetail carInfoBeanForCarDetail = carInfoBeanForCarDetailArrayList.get(position);

                if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail)) {
                    Glide.with(mContext)
                            .load(carInfoBeanForCarDetail.getFiles().get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(holder.iv_item1);
                    holder.tv_item1.setText(carInfoBeanForCarDetail.getCar_name());
                    holder.tv_item2.setText(carInfoBeanForCarDetail.getPrice() + "万");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_item1;
        private ImageView iv_item2;
        private ImageView iv_item3;

        private TextView tv_item1;
        private TextView tv_item2;
        private TextView tv_item3;
        private TextView tv_item4;
        private TextView tv_item5;
        private TextView tv_item6;
        private TextView tv_item7;

        private RecyclerView recyclerView;

        private LinearLayout linearLayout1;
        private LinearLayout linearLayout2;

        private RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
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
            } else if (dataType == CHE_HANG_DATA_TYPE) {
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
                iv_item3 = (ImageView) itemView.findViewById(R.id.iv_comment);
                tv_item4 = (TextView) itemView.findViewById(R.id.tv_comment);

                tv_item5 = (TextView) itemView.findViewById(R.id.tv_comment_content);//评论的内容

                linearLayout1 = (LinearLayout) itemView.findViewById(R.id.ll_comment_reply);
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
}
