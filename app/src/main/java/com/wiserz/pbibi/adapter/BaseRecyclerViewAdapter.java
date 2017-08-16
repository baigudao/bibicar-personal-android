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
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.CarInfoBeanForCarCenter;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.bean.CheHangBean;
import com.wiserz.pbibi.bean.FuLiBean;
import com.wiserz.pbibi.bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

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

    public BaseRecyclerViewAdapter(Context context, List<T> tList, int dataType) {
        this.mContext = context;
        this.mList = tList;
        this.dataType = dataType;
    }

    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (dataType == NEW_CAR_DATA_TYPE) {
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
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(fuLiBean.getDesc());
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
        } else if (dataType == ARTICLE_LIST_DATA_TYPE) {
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(fuLiBean.getWho());
                holder.tv_item2.setText(fuLiBean.getDesc());
            }
        } else if (dataType == VIDEO_LIST_DATA_TYPE) {
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("开始播放");
                    }
                });
                holder.tv_item1.setText(fuLiBean.getWho());
                holder.tv_item2.setText(fuLiBean.getDesc());
                holder.tv_item3.setText(fuLiBean.getDesc());
            }
        } else if (dataType == CHE_HANG_LIST_DATA_TYPE) {
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                final FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.user_photo)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(fuLiBean.getWho());
                holder.tv_item2.setText(fuLiBean.getDesc());
                holder.tv_item3.setText(fuLiBean.getDesc());

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort(fuLiBean.getWho());
                    }
                });

                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, fuLiBeanArrayList, CHE_HANG_LIST_ITEM_DATA_TYPE);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object data, int position) {
                        if (data.getClass().getSimpleName().equals("FuLiBean")) {
                            FuLiBean fuLiBean1 = (FuLiBean) data;
                            ToastUtils.showShort(fuLiBean1.getWho());
                        }
                    }
                });

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
            ArrayList<FuLiBean> fuLiBeanArrayList = (ArrayList<FuLiBean>) mList;

            if (EmptyUtils.isNotEmpty(fuLiBeanArrayList)) {
                FuLiBean fuLiBean = fuLiBeanArrayList.get(position);

                Glide.with(mContext)
                        .load(fuLiBean.getUrl())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(holder.iv_item1);
                holder.tv_item1.setText(fuLiBean.getDesc());
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
