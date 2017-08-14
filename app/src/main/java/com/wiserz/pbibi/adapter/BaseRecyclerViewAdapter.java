package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarInfoBean;
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
