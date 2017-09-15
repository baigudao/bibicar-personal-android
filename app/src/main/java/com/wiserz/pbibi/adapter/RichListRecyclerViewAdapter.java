package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.RichBean;
import com.wiserz.pbibi.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/9/4 15:00.
 * QQ : 971060378
 * Used as : 财富列表页面的适配器
 */
public class RichListRecyclerViewAdapter extends RecyclerView.Adapter implements BaseRecyclerViewAdapter.OnItemClickListener {

    private Context mContext;
    private JSONObject jsonObjectData;

    private int currentType = TOP;

    private static final int TOP = 0;
    private static final int SECOND = 1;
    private static final int OTHER = 2;

    private String avatar;
    private String nickname;

    private int size;
    private int rank;
    private double total_money;

    public RichListRecyclerViewAdapter(Context mContext, JSONObject jsonObjectData) {
        this.mContext = mContext;
        this.jsonObjectData = jsonObjectData;

        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");
            if (EmptyUtils.isNotEmpty(jsonArray)) {
                size = jsonArray.length();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TOP) {
            viewHolder = new TopViewHolder(View.inflate(mContext, R.layout.item_rich_list_top, null));
        } else if (viewType == SECOND) {
            viewHolder = new SecondViewHolder(View.inflate(mContext, R.layout.item_rich_list_second, null));
        } else if (viewType == OTHER) {
            viewHolder = new OtherViewHolder(View.inflate(mContext, R.layout.item_total_property, null));
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (currentType == TOP) {
            TopViewHolder topViewHolder = (TopViewHolder) holder;

            getTopAndSecondData(jsonObjectData);

            if (EmptyUtils.isNotEmpty(total_money)) {
                topViewHolder.tv_total_property.setText("您当前资产为" + total_money + "万");
            }
        } else if (currentType == SECOND) {
            SecondViewHolder secondViewHolder = (SecondViewHolder) holder;

            getTopAndSecondData(jsonObjectData);

            if (EmptyUtils.isNotEmpty(avatar)) {
                Glide.with(mContext)
                        .load(avatar)
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into(secondViewHolder.iv_circle_image);
            }
            if (EmptyUtils.isNotEmpty(nickname)) {
                secondViewHolder.tv_user_name.setText(nickname);
            }
        } else if (currentType == OTHER) {
            OtherViewHolder otherViewHolder = (OtherViewHolder) holder;

            ArrayList<RichBean> richBeanArrayList = getRichListData(jsonObjectData);

            if (EmptyUtils.isNotEmpty(richBeanArrayList) && richBeanArrayList.size() != 0) {
                final RichBean richBean = richBeanArrayList.get(position - 2);

                if (EmptyUtils.isNotEmpty(richBean)) {
                    if (position == 2) {
                        otherViewHolder.iv_image_rich.setImageResource(R.drawable.v5_one3x);
                    } else if (position == 3) {
                        otherViewHolder.iv_image_rich.setImageResource(R.drawable.v5_two3x);
                    } else if (position == 4) {
                        otherViewHolder.iv_image_rich.setImageResource(R.drawable.v5_three3x);
                    } else {
                        otherViewHolder.iv_image_rich.setVisibility(View.INVISIBLE);
                        otherViewHolder.tv_sort_rick.setVisibility(View.VISIBLE);
                        otherViewHolder.tv_sort_rick.setText(String.valueOf(position - 1));
                    }
                    Glide.with(mContext)
                            .load(richBean.getAvatar())
                            .placeholder(R.drawable.user_photo)
                            .error(R.drawable.user_photo)
                            .into(otherViewHolder.iv_circle_image);
                    otherViewHolder.iv_circle_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("用户" + richBean.getUser_id());
                        }
                    });
                    otherViewHolder.tv_user_name.setText(richBean.getNickname());
                    otherViewHolder.tv_user_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("用户" + richBean.getUser_id());
                        }
                    });
                    otherViewHolder.tv_num.setText(richBean.getTotal_money() + "万");
                    otherViewHolder.tv_like_num.setText(String.valueOf(richBean.getSort()));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return 2 + size;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TOP:
                currentType = TOP;
                break;
            case SECOND:
                currentType = SECOND;
                break;
            default:
                currentType = OTHER;
                break;
        }
        return currentType;
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_total_property;

        TopViewHolder(View itemView) {
            super(itemView);
            tv_total_property = (TextView) itemView.findViewById(R.id.tv_total_property);
        }
    }

    private class SecondViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_circle_image;
        private TextView tv_user_name;
        private TextView tv_user_sort;
        private TextView tv_user_like_num;
        private ImageView iv_like_or_no;

        SecondViewHolder(View itemView) {
            super(itemView);
            iv_circle_image = (CircleImageView) itemView.findViewById(R.id.iv_circle_image);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_user_sort = (TextView) itemView.findViewById(R.id.tv_user_sort);
            tv_user_like_num = (TextView) itemView.findViewById(R.id.tv_user_like_num);
            iv_like_or_no = (ImageView) itemView.findViewById(R.id.iv_like_or_no);
        }
    }

    private class OtherViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_image_rich;
        private TextView tv_sort_rick;
        private CircleImageView iv_circle_image;
        private TextView tv_user_name;
        private TextView tv_like_num;
        private ImageView iv_like_image;
        private TextView tv_num;

        OtherViewHolder(View itemView) {
            super(itemView);
            iv_image_rich = (ImageView) itemView.findViewById(R.id.iv_image_rich);
            tv_sort_rick = (TextView) itemView.findViewById(R.id.tv_sort_rick);
            iv_circle_image = (CircleImageView) itemView.findViewById(R.id.iv_circle_image);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_like_num = (TextView) itemView.findViewById(R.id.tv_like_num);
            iv_like_image = (ImageView) itemView.findViewById(R.id.iv_like_image);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }

    private void getTopAndSecondData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObject = jsonObjectData.optJSONObject("user_info");
            if (EmptyUtils.isNotEmpty(jsonObject)) {
                total_money = jsonObject.optDouble("total_money");
                avatar = jsonObject.optString("avatar");
                nickname = jsonObject.optString("nickname");
                rank = jsonObject.optInt("rank");
            }
        }
    }

    private ArrayList<RichBean> getRichListData(JSONObject jsonObjectData) {
        ArrayList<RichBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONArray("list");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<RichBean>>() {
            }.getType());
        }
        return list;
    }

    @Override
    public void onItemClick(Object data, int position) {

    }
}
