package com.wiserz.pbibi.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.AlbumFragment;
import com.wiserz.pbibi.util.CommonUtil;

/**
 * Created by gigabud on 16-2-19.
 */
public class ShowMediasAdapter extends BaseAdapter {

    private AlbumFragment.MediaFileInfo mMediaFileInfo;
    private int mItemWidth;
    private AlbumFragment mAlbumFragment;

    public ShowMediasAdapter(AlbumFragment albumFragment) {
        mAlbumFragment = albumFragment;
    }

    public void setItemWidth(int itemWidth) {
        mItemWidth = itemWidth;
    }

    public void resetMediaFileInfo(AlbumFragment.MediaFileInfo mediaFileInfo) {
        mMediaFileInfo = mediaFileInfo;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mMediaFileInfo == null ? 0 : mMediaFileInfo.mediaInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMediaFileInfo == null ? null : mMediaFileInfo.mediaInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mAlbumFragment.getActivity())
                    .inflate(R.layout.file_picture_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            viewHolder.rlSelect = convertView.findViewById(R.id.rlSelect);
            viewHolder.tvPhotoIndex = (TextView) convertView.findViewById(R.id.tvPhotoIndex);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.iv.getLayoutParams();
            lp.width = mItemWidth;
            lp.height = mItemWidth;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AlbumFragment.MediaInfo mediaInfo = mMediaFileInfo.mediaInfoList.get(position);
        CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(mediaInfo.mediaFile), viewHolder.iv);
        viewHolder.iv.setTag(R.id.tag, mediaInfo);
        if (mediaInfo.isSelected) {
            viewHolder.rlSelect.setVisibility(View.VISIBLE);
            viewHolder.tvPhotoIndex.setText(String.valueOf(mediaInfo.index + 1));
        } else {
            viewHolder.rlSelect.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        View rlSelect;
        TextView tvPhotoIndex;
    }
}
