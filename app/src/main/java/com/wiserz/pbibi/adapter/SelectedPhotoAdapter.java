package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.fragment.EditPhotoFragment;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by gigabud on 17-11-8.
 */

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<File> mDataList;
    private OnPhotoOperator mOnPhotoOperator;

    public SelectedPhotoAdapter(Context context) {
        mContext = context;
    }

    public void setDataList(ArrayList<File> dataList) {
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
        holder.setData(position, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView ivClose;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_image_all);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
        }

        public void setData(int position, final File photoPath) {
            CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(photoPath), ivPhoto);
            ivPhoto.setTag(R.id.tag, position);
            ivClose.setTag(R.id.tag, position);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    File file = mDataList.remove(pos);
                    file.delete();
                    notifyDataSetChanged();
                    if (mOnPhotoOperator != null) {
                        mOnPhotoOperator.onDelete(pos);
                    }
                }
            });

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    DataManager.getInstance().setObject(mDataList.get(pos).getAbsolutePath());
                    DataManager.getInstance().setData1(pos);
                    DataManager.getInstance().setData2(mDataList.size());
                    ((BaseActivity) mContext).gotoPager(EditPhotoFragment.class,null,true);
                }
            });
        }
    }

    public interface OnPhotoOperator {
        public void onDelete(int position);
    }


}
