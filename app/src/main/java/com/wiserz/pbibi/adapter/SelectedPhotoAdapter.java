package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.fragment.EditPhotoFragment;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;

import java.io.File;
import java.util.Map;

/**
 * Created by gigabud on 17-11-8.
 */

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder> {

    private Context mContext;
    private Map<Integer,File> mDataList; //Integer代表的是图片类型file_type,从1开始
    private OnPhotoOperator mOnPhotoOperator;
    private int[] imgArray = {R.drawable.photo_bg1,R.drawable.photo_bg2,R.drawable.photo_bg2,R.drawable.photo_bg4,R.drawable.photo_bg5,R.drawable.photo_bg6,
                              R.drawable.photo_bg7,R.drawable.photo_bg8,R.drawable.photo_bg9,R.drawable.photo_bg10,R.drawable.photo_bg11,R.drawable.photo_bg12};
    private int selectIndex = 0;//当前选择的位置

    public SelectedPhotoAdapter(Context context) {
        mContext = context;
    }

    public int getSelectIndex(){
        return selectIndex;
    }

    public void setSelectIndex(int index){
        selectIndex = index;
        if(index<0)
            selectIndex = 0;
        if(index>11)
            selectIndex = 11;

    }

    public void setDataList(Map<Integer,File> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setOnPhotoDelete(OnPhotoOperator onPhotoOperator) {
        mOnPhotoOperator = onPhotoOperator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_selected_photo, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return 12;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView ivClose;
        TextView tvPos;
        RelativeLayout rlSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_image_all);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
            tvPos = (TextView) itemView.findViewById(R.id.tvPos);
            rlSelect = (RelativeLayout) itemView.findViewById(R.id.rlSelect);
        }

        public void setData(int position) {
            if(EmptyUtils.isNotEmpty(mDataList) &&
                    mDataList.get(position+1)!=null){

                CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(mDataList.get(position+1)), ivPhoto);
                ivClose.setVisibility(View.VISIBLE);
            }else{
                Glide.with(mContext).load(imgArray[position]).error(R.drawable.default_bg_ratio_1).into(ivPhoto);
                ivClose.setVisibility(View.GONE);
            }

            if(position == selectIndex){//选中状态
               rlSelect.setVisibility(View.VISIBLE);
            }else{
               rlSelect.setVisibility(View.GONE);
            }

            tvPos.setText(mContext.getResources().getStringArray(R.array.car_img_type)[position]);
            ivPhoto.setTag(R.id.tag, position);
            ivClose.setTag(R.id.tag, position);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    if(selectIndex == pos) {//点击已选中
                        File file = mDataList.remove(pos+1);
                        file.delete();
                        notifyDataSetChanged();
                        if (mOnPhotoOperator != null) {
                            mOnPhotoOperator.onDelete(pos);
                        }
                    }else{
                        selectIndex = pos;
                        notifyDataSetChanged();
                    }

                }
            });

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    if(selectIndex == pos){//点击已选中
                        if(mDataList.get(pos+1)!=null){ //该位置有照片
                            DataManager.getInstance().setObject(mDataList.get(pos+1).getAbsolutePath());
                            DataManager.getInstance().setData1(pos);
                            DataManager.getInstance().setData2(12);
                            DataManager.getInstance().setData3(pos+1);//key
                            ((BaseActivity) mContext).gotoPager(EditPhotoFragment.class,null,true);
                        }
                    }else{//点击未选中
                        if (mOnPhotoOperator != null) {
                            mOnPhotoOperator.onItemClick(pos);
                        }
                        selectIndex = pos;
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public interface OnPhotoOperator {
        void onDelete(int position);
        void onItemClick(int position);
    }


}
